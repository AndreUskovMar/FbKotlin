package ru.auskov.fbkotlin.main.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import jakarta.inject.Inject
import okio.IOException
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.utils.firebase.FirestoreManagerPaging

class BookFactoryPaging @Inject constructor(
    private val firestoreManagerPaging: FirestoreManagerPaging
) : PagingSource<DocumentSnapshot, Book>() {
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Book>): DocumentSnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Book> {
        try {
            val currentPage = params.key

            Log.d("MyLog", "Current page: ${params.key}")
            Log.d("MyLog", "Page size: ${params.loadSize}")
            val (snapshot, books) = firestoreManagerPaging.nextPage(
                pageSize = params.loadSize.toLong(),
                currentKey = currentPage
            )

            val prevKey = if (currentPage != null)
                    snapshot.documents.firstOrNull()
                else null

            val nextKey = snapshot.documents.lastOrNull()

            Log.d("MyLog", "Prev key: ${prevKey?.id}")
            Log.d("MyLog", "Next key: ${nextKey?.id}")

            return LoadResult.Page(
                data = books,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}