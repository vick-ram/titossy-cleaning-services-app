package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.titossycleaningservicesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCard(
    image: Int,
    title: String,
    price: Float
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        onClick = { /*TODO*/ },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = image),
                contentDescription = title
            )

            Text(text = "Kshs. $price", modifier = Modifier.align(Alignment.BottomStart))

            OutlinedButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Buy Now")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServicePreview() {
    ServiceCard(
        image = R.drawable.download_house_cleaning_services___cleaning_service_cartoon_png_png_image_with_no_backgroud___pngkey_com,
        title = "home cleaning",
        price = 200f
    )
}

