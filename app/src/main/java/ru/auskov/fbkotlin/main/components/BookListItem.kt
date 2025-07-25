package ru.auskov.fbkotlin.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ru.auskov.fbkotlin.data.Book
import ru.auskov.fbkotlin.main.utils.Categories
import ru.auskov.fbkotlin.utils.toBitmap

@Preview(showBackground = true)
@Composable
fun BookListItem(
    isAdminState: Boolean = true,
    book: Book = Book(
        name = "Title",
        description = "Description",
        categoryIndex = Categories.FANTASY,
        price = 100
    ),
    onEditBook: (Book) -> Unit = {},
    onDeleteBook: (Book) -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onBookClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onBookClick()
            }
    ) {
        AsyncImage(
            model = book.imageUrl.toBitmap() ?: book.imageUrl,
            contentDescription = "Bonevtik Potujniy",
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = book.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = book.description,
            fontSize = 13.sp,
            color = Color.Gray,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = book.price.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            IconButton(
                onClick = {
                    onFavoriteClick()
                }
            ) {
                Icon(
                    if (book.isFavorite)
                        Icons.Default.Favorite
                    else
                        Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }

        if (isAdminState) Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    onEditBook(book)
                }
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }

            IconButton(
                onClick = {
                    onDeleteBook(book)
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}