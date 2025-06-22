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
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

//    fun deleteBook(
//        book: Book,
//        onSuccess: () -> Unit,
//        onFailure: (message: String) -> Unit
//    ) {
//        db.collection("books")
//            .document(book.key)
//            .delete()
//            .addOnSuccessListener {
//                onSuccess()
//            }
//            .addOnFailureListener { error ->
//                onFailure(error.message ?: "Error")
//            }
//    }

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