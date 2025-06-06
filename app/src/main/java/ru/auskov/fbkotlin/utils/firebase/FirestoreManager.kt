package ru.auskov.fbkotlin.utils.firebase

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
                    .whereEqualTo("category", categoryIndex)
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
}