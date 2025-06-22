package ru.auskov.fbkotlin.utils.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.data.Favorite
import ru.auskov.fbkotlin.main.utils.Categories
import javax.inject.Singleton

@Singleton
class FirestoreManagerPaging(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    var categoryIndex = Categories.FANTASY

    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection("books").limit(pageSize)
        val favKeysList = getFavoriteIdsList()

        query = when (categoryIndex) {
            Categories.FAVORITES -> {
                query.whereIn(FieldPath.documentId(), favKeysList)
            }
            else -> {
                query.whereEqualTo("categoryIndex", categoryIndex)
            }
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
}