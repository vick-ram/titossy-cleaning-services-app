package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.domain.viewmodel.AuthViewModel
import com.example.titossycleaningservicesapp.presentation.users.customer.utils.ServiceCard
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val currentUser = viewModel.currentUser
    var searchText by remember { mutableStateOf("") }
    val num = Random.nextInt()
    val cardList = listOf(
        CardItem(
            R.drawable.download_house_cleaning_services___cleaning_service_cartoon_png_png_image_with_no_backgroud___pngkey_com,
            title = "$num",
            price = 0.0f
        ),
        CardItem(
            R.drawable.download_house_cleaning_services___cleaning_service_cartoon_png_png_image_with_no_backgroud___pngkey_com,
            title = "$num",
            price = 0.0f
        ),
        CardItem(
            R.drawable.download_house_cleaning_services___cleaning_service_cartoon_png_png_image_with_no_backgroud___pngkey_com,
            title = "$num",
            price = 0.0f
        ),
        CardItem(
            R.drawable.download_house_cleaning_services___cleaning_service_cartoon_png_png_image_with_no_backgroud___pngkey_com,
            title = "$num",
            price = 0.0f
        ),
        CardItem(
            R.drawable.download_house_cleaning_services___cleaning_service_cartoon_png_png_image_with_no_backgroud___pngkey_com,
            title = "$num",
            price = 0.0f
        ),
        CardItem(
            R.drawable.download_house_cleaning_services___cleaning_service_cartoon_png_png_image_with_no_backgroud___pngkey_com,
            title = "$num",
            price = 0.0f
        )
    )

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column {
                        Text(text = "Welcome ${currentUser?.displayName}")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier
                            .size(24.dp)
                            .clickable { }
                        ) {
                            IconButton(
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "notifications"
                                )
                            }
                            Badge(modifier = Modifier.align(Alignment.TopEnd))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "faqs", modifier = Modifier.clickable { })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "help", modifier = Modifier.clickable { })
                    }
                }
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    title = {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 8.dp),
                            shape = RoundedCornerShape(35.dp),
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text(text = "search") },
                            singleLine = true,
                            maxLines = 1,
                        )
                    }
                )
            }
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(it)
        ) {
            items(cardList) { item ->
                ServiceCard(
                    image = item.image,
                    title = item.title,
                    price = item.price
                )
            }
        }

    }

}

data class CardItem(
    val image: Int,
    val title: String,
    val price: Float
)