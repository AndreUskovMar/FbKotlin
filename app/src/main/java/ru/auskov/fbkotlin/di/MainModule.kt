package ru.auskov.fbkotlin.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.auskov.fbkotlin.utils.firebase.AuthManager
import ru.auskov.fbkotlin.utils.firebase.FirestoreManager
import ru.auskov.fbkotlin.utils.firebase.FirestoreManagerPaging
import ru.auskov.fbkotlin.utils.store.StoreManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }

    @Provides
    @Singleton
    fun provideFirebasePaging(
        db: FirebaseFirestore
    ): FirestoreManagerPaging {
        return FirestoreManagerPaging(db)
    }

    @Provides
    @Singleton
    fun provideFirebaseManager(
        auth: FirebaseAuth,
        db: FirebaseFirestore,
        storage: FirebaseStorage
    ): FirestoreManager {
        return FirestoreManager(auth, db, storage)
    }

    @Provides
    @Singleton
    fun provideAuthManager(
        auth: FirebaseAuth
    ): AuthManager {
        return AuthManager(auth)
    }

    @Provides
    @Singleton
    fun provideStoreManager(
        app: Application
    ): StoreManager {
        return StoreManager(app)
    }

}