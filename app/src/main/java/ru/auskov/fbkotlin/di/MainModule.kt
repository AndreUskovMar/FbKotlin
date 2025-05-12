package ru.auskov.fbkotlin.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.auskov.fbkotlin.utils.firebase.AuthManager
import ru.auskov.fbkotlin.utils.firebase.FirestoreManager
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
    fun provideFirebaseManager(
        auth: FirebaseAuth,
        db: FirebaseFirestore
    ): FirestoreManager {
        return FirestoreManager(auth, db)
    }

    @Provides
    @Singleton
    fun provideAuthManager(
        auth: FirebaseAuth
    ): AuthManager {
        return AuthManager(auth)
    }

}