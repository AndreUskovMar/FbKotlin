package ru.auskov.fbkotlin.utils.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.data.Favorite
import javax.inject.Singleton

@Singleton
class FirestoreManager(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    private fun getFavoritesCategoryReference(): CollectionReference {
        return db.collection("users")
            .document(auth.uid ?: "")
            .collection("favorites")
    }

    private fun getFavoriteIdsList(
        onSuccess: (List<String>) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        getFavoritesCategoryReference()
            .get()
            .addOnSuccessListener { task ->
                val idsList = arrayListOf<String>()
                val favsList = task.toObjects(Favorite::class.java)
                favsList.forEach {
                    idsList.add(it.key)
                }
                onSuccess(idsList)
            }
            .addOnFailureListener { error ->
                onFailure(error.message ?: "Error")
            }
    }

    fun getBooksList(
        categoryIndex: Int,
        onChangeState: (List<Book>) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        getFavoriteIdsList(
            onSuccess = {listIds ->
                db.collection("books")
                    .whereEqualTo("categoryIndex", categoryIndex)
                    .get()
                    .addOnSuccessListener { task ->
                        val booksList = task.toObjects(Book::class.java).map { book ->
                            if (listIds.contains(book.key)) {
                                book.copy(isFavorite = true)
                            } else {
                                book
                            }
                        }
                        onChangeState(booksList)
                    }
                    .addOnFailureListener { error ->
                        onFailure(error.message ?: "Error")
                    }
            },
            onFailure = {
                onFailure(it)
            }
        )
    }

    fun getFavoritesBooksList(
        onChangeState: (List<Book>) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        getFavoriteIdsList(
            onSuccess = {listIds ->
                if (listIds.isNotEmpty()) {
                    db.collection("books")
                        .whereIn(FieldPath.documentId(), listIds)
                        .get()
                        .addOnSuccessListener { task ->
                            val booksList = task.toObjects(Book::class.java).map { book ->
                                book.copy(isFavorite = true)
                            }
                            onChangeState(booksList)
                        }
                        .addOnFailureListener { error ->
                            onFailure(error.message ?: "Error")
                        }
                } else {
                    onChangeState(emptyList())
                }
            },
            onFailure = {
                onFailure(it)
            }
        )
    }

    private fun addToFavorites(
        favorite: Favorite,
        isFav: Boolean,
    ) {
        val favoritesDocumentRef =  getFavoritesCategoryReference().document(favorite.key)

        if (isFav) {
            favoritesDocumentRef.set(favorite)
        } else {
            favoritesDocumentRef.delete()
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

    fun saveBookImage(
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
                book = book.copy(imageUrl = prevImageUrl.toString()),
                onSuccess = {
                    onSuccess()
                },
                onError = { message ->
                    onError(message)
                }
            )

            return
        }

        val uploadTask = storageRef.putFile(uri)

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
}