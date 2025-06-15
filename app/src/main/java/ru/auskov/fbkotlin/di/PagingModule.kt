package ru.auskov.fbkotlin.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.main.data.BookFactoryPaging
import ru.auskov.fbkotlin.utils.firebase.FirestoreManagerPaging

@Module
@InstallIn(ViewModelComponent::class)
object PagingModule {
    @Provides
    @ViewModelScoped
    fun providesPagingFlow(
        firestoreManagerPaging: FirestoreManagerPaging
    ): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                BookFactoryPaging(firestoreManagerPaging)
            }
        ).flow
    }
}