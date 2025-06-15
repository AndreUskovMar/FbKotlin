package ru.auskov.fbkotlin.utils.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import ru.auskov.fbkotlin.data.Book
import javax.inject.Singleton

@Singleton
class FirestoreManagerPaging(
    private val db: FirebaseFirestore
) {
    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection("books").limit(pageSize)

        if (currentKey != null) {
            query = query.startAfter(currentKey)
        }

        val querySnapshot = query.get().await()
        val books = querySnapshot.toObjects(Book::class.java)

        return Pair(querySnapshot, books)
    }
}