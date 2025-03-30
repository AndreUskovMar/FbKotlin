package ru.auskov.fbkotlin.add_book_screen

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
// import com.google.firebase.storage.FirebaseStorage
import ru.auskov.fbkotlin.components.RoundedButton
import ru.auskov.fbkotlin.components.RoundedDropDownMenu
import ru.auskov.fbkotlin.components.RoundedTextInput
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.ui.theme.Purple40
import android.util.Base64
import androidx.compose.ui.platform.LocalContext
import ru.auskov.fbkotlin.add_book_screen.data.AddBookScreenObject

@Composable
fun AddBookScreen(
    navData: AddBookScreenObject = AddBookScreenObject(),
    onSavedSuccess: () -> Unit = {}
) {
    var selectedCategory = navData.category

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

//    val storage = remember {
//        FirebaseStorage.getInstance()
//    }

    val cr = LocalContext.current.contentResolver

    val title = remember {
        mutableStateOf(navData.name)
    }

    val description = remember {
        mutableStateOf(navData.description)
    }

    val price = remember {
        mutableStateOf(navData.price)
    }

    val imageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri.value = uri
    }

    Image(
        painter = rememberAsyncImagePainter(model = imageUri.value),
        contentDescription = "Add Book",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.2f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Dogs by Nature",
            style = TextStyle(
                fontSize = 46.sp,
                lineHeight = 50.sp,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                )
            ),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Purple40,
            modifier = Modifier.padding(bottom = 50.dp),
        )

        RoundedDropDownMenu(
            category = navData.category,
            onOptionSelected = { category ->
                selectedCategory = category
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        RoundedTextInput(
            label = "Title",
            value = title.value
        ) {
            title.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedTextInput(
            label = "Description",
            value = description.value,
            singleLine = false,
            maxLines = 5
        ) {
            description.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedTextInput(
            label = "Price",
            value = price.value
        ) {
            price.value = it
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RoundedButton(name = "Add Books Image") {
                imageLauncher.launch("image/*")
            }

            Spacer(modifier = Modifier.height(10.dp))

            RoundedButton(name = "Save New Book") {
                saveBookToFireStore(
                    firestore,
                    book = Book(
                        name = title.value,
                        description = description.value,
                        price = price.value,
                        category = selectedCategory,
                        imageUrl = convertImageToBase64(
                            imageUri.value as Uri,
                            cr
                        )
                    ),
                    onSuccess = {
                        onSavedSuccess()
                    },
                    onError = {

                    }
                )
            }
        }
    }
}

private fun convertImageToBase64(uri: Uri, contentResolver: ContentResolver): String {
    val inputStream = contentResolver.openInputStream(uri)

    val bytes = inputStream?.readBytes()

    return bytes?.let {
        Base64.encodeToString(it, Base64.DEFAULT)
    } ?: ""
}

/**
private fun saveBookImage(
    uri: Uri,
    book: Book,
    storage: FirebaseStorage,
    firestore: FirebaseFirestore,
    onSuccess: () -> Unit,
    onError: () -> Unit,
) {
    val timestamp = System.currentTimeMillis()
    val storageRef = storage.reference
        .child("book_images")
        .child("image_$timestamp.png")

    val uploadTask = storageRef.putFile(uri)

    uploadTask.addOnCompleteListener {
        storageRef.downloadUrl.addOnSuccessListener { url ->
            saveBookToFireStore(
                firestore = firestore,
                url = url.toString(),
                book = book,
                onSuccess = {
                    onSuccess()
                },
                onError = {
                    onError()
                }
            )
        }
    }
}
**/

private fun saveBookToFireStore(
    firestore: FirebaseFirestore,
    book: Book,
    onSuccess: () -> Unit,
    onError: () -> Unit,
) {
    val db = firestore.collection("books")
    val key = db.document().id

    db.document(key)
        .set(
            book.copy(key = key)
        )
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener {
            onError()
        }
}