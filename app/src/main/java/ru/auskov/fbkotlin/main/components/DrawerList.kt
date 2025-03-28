package ru.auskov.fbkotlin.main.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.auskov.fbkotlin.R
import ru.auskov.fbkotlin.ui.theme.LightGreen

@Composable
fun DrawerList(
    onAdminClick: () -> Unit
) {
    val listItems = listOf(
        "Favourites",
        "Fantasy",
        "Drama",
        "Bestsellers"
    )

    val isAdminState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        isAdmin { isAdmin ->
            isAdminState.value = isAdmin
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Image(
            painter = painterResource(R.drawable.login_bg),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            alpha = 0.3f,
            contentScale = ContentScale.Crop
        )

        Column {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(listItems) { item ->
                    DrawerListItem(name = item) {

                    }
                }
            }

            if (isAdminState.value) Button(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(LightGreen),
                onClick = {
                    onAdminClick()
                }
            ) {
                Text("Admin Panel", color = Color.Blue)
            }
        }
    }
}

fun isAdmin(onAdmin: (Boolean) -> Unit) {
    val uid = Firebase.auth.currentUser!!.uid

    Firebase.firestore.collection("admin")
        .document(uid).get().addOnSuccessListener {
            Log.d("MyLog", "is admin: ${it.get("isAdmin")}")

            onAdmin(it.get("isAdmin") as Boolean)
        }
}