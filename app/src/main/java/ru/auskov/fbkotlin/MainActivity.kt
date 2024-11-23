package ru.auskov.fbkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.ui.theme.FbKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FbKotlinTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val fs = Firebase.firestore

    val list = remember {
        mutableStateOf(emptyList<Book>())
    }

//    fs.collection("books").get().addOnCompleteListener {
//        if (it.isSuccessful) {
//            list.value = it.result.toObjects(Book::class.java)
//        }
//    }

    fs.collection("books").addSnapshotListener { value, error ->
        list.value = value?.toObjects(Book::class.java) ?: emptyList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            items(list.value) { book ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                    Text(book.name, modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize())
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), onClick = {
            fs.collection("books").document().set(
                Book(
                    "My book",
                    "bla-bla",
                    "fuck",
                    "100",
                    "url"
                )
            )
        }) {
            Text(
                text = "Add book",
            )
        }
    }

}