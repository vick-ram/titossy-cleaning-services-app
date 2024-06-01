package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.BASE_URL
import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem
import com.example.titossycleaningservicesapp.domain.models.ui_models.Service
import com.example.titossycleaningservicesapp.domain.models.ui_models.ServiceAddOn
import java.math.BigDecimal
import java.util.UUID


@Composable
fun ServiceCardInCart(
    cartItem: CartItem,
    onRemove: (CartItem) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(4.dp),
        onClick = {},
        elevation = CardDefaults.elevatedCardElevation(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val itemThumbnail = cartItem.thumbnail
            val itemName = cartItem.name
            val itemPrice = cartItem.price

            Image(
                painter = rememberAsyncImagePainter(
                    contentScale = ContentScale.Crop,
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("$BASE_URL$itemThumbnail")
                        .crossfade(true)
                        .placeholder(R.drawable.cleaning1)
                        .error(R.drawable.errorimg)
                        .build()
                ),
                contentDescription = itemName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = MaterialTheme.typography.titleSmall.fontWeight
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Kshs. $itemPrice",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Button(
                modifier = Modifier,
                onClick = { onRemove(cartItem) }
            ) {
                Text(text = "Remove")
            }
        }
    }
}

@Composable
fun ServiceAddOnCard(
    serviceAddOn: ServiceAddOn,
    addToCart: (ServiceAddOn) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            val painter = rememberAsyncImagePainter(
                model = "https://imgs.search.brave.com/zv3kPmZMX67fLNV_qoWTx0ZRB07suBYxvB2n_bO-4sY/rs:fit:500:0:0/g:ce/aHR0cHM6Ly90NC5m/dGNkbi5uZXQvanBn/LzAxLzY1LzkxLzI5/LzM2MF9GXzE2NTkx/Mjk2MF9zQXN1ZzJC/eExFRlcwOUh0UklS/U3NVOVVOeFlWZkJR/Ty5qcGc",
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painter,
                contentDescription = serviceAddOn.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = serviceAddOn.name,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = serviceAddOn.description,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = serviceAddOn.formattedPrice,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { addToCart(serviceAddOn) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "Add to cart",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceAddPreview() {
    val serviceAddOnSample = ServiceAddOn(
        id = UUID.fromString("1167a073-08c1-4fae-85d3-dfeb4dd6293b"),
        serviceId = UUID.fromString("f9092838-991a-457c-91d4-e449fc4ef809"),
        name = "Balcony",
        description = "This is balcony cleaning",
        price = BigDecimal("2390.00"),
        image = "http://127.0.0.1:8080/api/uploads/addon/people.png"
    )

    ServiceAddOnCard(serviceAddOn = serviceAddOnSample, addToCart = {})
}


@Composable
fun CustomServiceCard(
    service: Service,
    onServiceClick: (Service) -> Unit
) {
    ElevatedCard(
        onClick = { onServiceClick(service) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("$BASE_URL${service.image}")
                        .crossfade(true)
                        .placeholder(R.drawable.cleaning1)
                        .error(R.drawable.errorimg)
                        .build(),
                    contentScale = ContentScale.Crop
                ),
                contentDescription = service.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = service.name,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = service.description,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = service.formattedPrice,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun SelectableCircularBox(
    value: Any,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else MaterialTheme.colorScheme.surface

    val borderStrokeColor = if (isSelected) {
        MaterialTheme.colorScheme.surface
    } else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .padding(end = 16.dp)
            .size(70.dp)
            .clip(CircleShape)
            .border(
                BorderStroke(1.dp, borderStrokeColor),
                shape = CircleShape
            )
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            fontSize = 24.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CardPreview() {
    var selected by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            for (i in 1..4) {
                SelectableCircularBox(
                    value = i,
                    isSelected = selected == i,
                    onClick = {
                        selected = i
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "$selected",
            fontSize = 26.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

}

@Composable
fun CustomCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = backgroundColor,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
        shadowElevation = 4.dp
    ) {
        Column {
            content(this)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCardPreview(modifier: Modifier = Modifier) {
    CustomCard(
        onClick = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        content = {
            Image(
                painter = painterResource(id = R.drawable.onboarding3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
                    .height(154.dp)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = "Hello",
                fontSize = 20.sp,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }
    )
}


