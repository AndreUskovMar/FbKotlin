package ru.auskov.fbkotlin.utils.firebase

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.data.Favorite
import ru.auskov.fbkotlin.main.utils.Categories
import ru.auskov.fbkotlin.utils.ImageUtils
import javax.inject.Singleton

const val IS_BASE64 = true

@Singleton
class FirestoreManagerPaging(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val contentResolver: ContentResolver
) {
    var categoryIndex = Categories.FANTASY
    var searchText = ""
    var minPrice = 0
    var maxPrice = 0

    var isPriceFilterType = false

    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection("books").limit(pageSize).orderBy("name")
        val favKeysList = getFavoriteIdsList()

        query = when (categoryIndex) {
            Categories.FAVORITES -> {
                // query.whereIn(FieldPath.documentId(), favKeysList)
                query.whereIn(FieldPath.of("key"), favKeysList)
            }

            else -> {
                query.whereEqualTo("categoryIndex", categoryIndex)
            }
        }

        if (searchText.isNotEmpty()) {
            Log.d("MyLog", searchText)
            query = query
                .whereGreaterThanOrEqualTo("searchName", searchText.lowercase())
                .whereLessThan("searchName", "${searchText.lowercase()}\uF7FF")
        }

        if (isPriceFilterType) {
            query = query.whereGreaterThanOrEqualTo("price", minPrice)
                .whereLessThanOrEqualTo("price", maxPrice)
        }

        if (currentKey != null) {
            query = query.startAfter(currentKey)
        }

        val querySnapshot = query.get().await()
        val books = querySnapshot.toObjects(Book::class.java)

        val updatedBooks = books.map { book ->
            if (favKeysList.contains(book.key)) {
                book.copy(isFavorite = true)
            } else {
                book
            }
        }

        return Pair(querySnapshot, updatedBooks)
    }

    private suspend fun getFavoriteIdsList(): List<String> {
        val snapshot = getFavoritesCategoryReference().get().await()
        val favsList = snapshot.toObjects(Favorite::class.java)

        val idsList = arrayListOf<String>()

        favsList.forEach {
            idsList.add(it.key)
        }

        return if (idsList.isEmpty()) listOf("-1") else idsList
    }

    private fun getFavoritesCategoryReference(): CollectionReference {
        return db.collection("users")
            .document(auth.uid ?: "")
            .collection("favorites")
    }

    private fun addToFavorites(
        favorite: Favorite,
        isFav: Boolean,
    ) {
        val favoritesDocumentRef = getFavoritesCategoryReference().document(favorite.key)

        if (isFav) {
            favoritesDocumentRef.set(favorite)
        } else {
            favoritesDocumentRef.delete()
        }
    }

    fun changeFavoriteState(books: List<Book>, book: Book): List<Book> {
        return books.map {
            if (book.key == it.key) {
                addToFavorites(
                    Favorite(it.key),
                    !it.isFavorite,
                )

                it.copy(isFavorite = !it.isFavorite)
            } else {
                it
            }
        }
    }

    private fun saveBookToFireStore(
        book: Book,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val db = db.collection("books")
        val key = book.key.ifEmpty {
            db.document().id
        }

        db.document(key)
            .set(
                book.copy(key = key)
            )
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error saving book")
            }
    }

    private fun uploadImageToStorage(
        prevImageUrl: String,
        uri: Uri?,
        book: Book,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val timestamp = System.currentTimeMillis()
        val storageRef = if (prevImageUrl.isEmpty()) {
            storage.reference
                .child("book_images")
                .child("image_$timestamp.png")
        } else {
            storage.getReferenceFromUrl(prevImageUrl)
        }

        if (uri == null) {
            saveBookToFireStore(
                book = book.copy(imageUrl = prevImageUrl),
                onSuccess = {
                    onSuccess()
                },
                onError = { message ->
                    onError(message)
                }
            )

            return
        }

        // with optimization
        val imageBytes = ImageUtils.convertUriToBytesArray(uri, contentResolver)
        val uploadTask = storageRef.putBytes(imageBytes)

        // without optimization
        // val uploadTask = storageRef.putFile(uri)

        uploadTask.addOnCompleteListener {
            storageRef.downloadUrl.addOnSuccessListener { url ->
                saveBookToFireStore(
                    book = book.copy(imageUrl = url.toString()),
                    onSuccess = {
                        onSuccess()
                    },
                    onError = { message ->
                        onError(message)
                    }
                )
            }
        }
    }

    fun saveBookImage(
        prevImageUrl: String,
        uri: Uri?,
        book: Book,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        if (IS_BASE64) {
            saveBookToFireStore(
                book,
                onSuccess = {
                    onSuccess()
                },
                onError = { message ->
                    onError(message)
                }
            )
        } else {
            uploadImageToStorage(
                prevImageUrl = prevImageUrl,
                uri = uri,
                book = book,
                onSuccess = {
                    onSuccess()
                },
                onError = { message ->
                    onError(message)
                }
            )
        }
    }

    fun deleteBook(
        book: Book,
        onSuccess: () -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        db.collection("books")
            .document(book.key)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onFailure(error.message ?: "Error")
            }
    }
}