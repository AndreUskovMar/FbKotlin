package ru.auskov.fbkotlin.utils.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.main.utils.Categories
import javax.inject.Singleton

@Singleton
class FirestoreManagerPaging(
    private val db: FirebaseFirestore
) {
    var categoryType: CategoryType = CategoryType.CategoryByIndex(Categories.FAVORITES)

    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection("books").limit(pageSize)

        query = when(categoryType) {
            is CategoryType.Favorites -> {query}
            is CategoryType.CategoryByIndex -> {
                query.whereEqualTo("categoryIndex", (categoryType as CategoryType.CategoryByIndex).index)
            }
        }

        if (currentKey != null) {
            query = query.startAfter(currentKey)
        }

        val querySnapshot = query.get().await()
        val books = querySnapshot.toObjects(Book::class.java)

        return Pair(querySnapshot, books)
    }

    sealed class CategoryType {
        data object Favorites: CategoryType()
        data class CategoryByIndex(val index: Int): CategoryType()
    }
}