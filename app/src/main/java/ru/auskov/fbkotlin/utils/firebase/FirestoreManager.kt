package ru.auskov.fbkotlin.utils.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.data.Favorite
import javax.inject.Singleton

@Singleton
class FirestoreManager(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private fun getFavoritesCategoryReference(): CollectionReference {
        return db.collection("users")
            .document(auth.uid ?: "")
            .collection("favorites")
    }

    private fun getFavoriteIdsList(
        onSuccess: (List<String>) -> Unit
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
                Log.d("MyLog", error.message.toString())
            }
    }

    fun getBooksList(
        category: String,
        onChangeState: (List<Book>) -> Unit
    ) {
        getFavoriteIdsList {listIds ->
            db.collection("books")
                .whereEqualTo("category", category)
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
                    Log.d("MyLog", error.message.toString())
                }
        }
    }

    fun getFavoritesBooksList(
        onChangeState: (List<Book>) -> Unit
    ) {
        getFavoriteIdsList {listIds ->
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
                        Log.d("MyLog", error.message.toString())
                    }
            } else {
                onChangeState(emptyList())
            }
        }
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
        onSuccess: () -> Unit
    ) {
        db.collection("books")
            .document(book.key)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {

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
}