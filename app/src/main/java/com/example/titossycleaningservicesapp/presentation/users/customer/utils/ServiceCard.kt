package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem
import com.example.titossycleaningservicesapp.domain.models.ui_models.Service
import com.example.titossycleaningservicesapp.domain.models.ui_models.ServiceAddOn


@Composable
fun ServiceCardInCart(
    modifier: Modifier = Modifier,
    cartItem: CartItem,
    onRemove: (CartItem) -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        onClick = {},
        elevation = CardDefaults.elevatedCardElevation(),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    contentScale = ContentScale.Crop,
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(cartItem.thumbnail)
                        .crossfade(true)
                        .placeholder(R.drawable.cleaning1)
                        .error(R.drawable.errorimg)
                        .build()
                ),
                contentDescription = cartItem.name,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(60.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Column(
                modifier = modifier
                    .wrapContentSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    modifier = modifier
                        .padding(bottom = 8.dp),
                    text = cartItem.name,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally),
                    text = cartItem.formattedPrice,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }

            Column(
                modifier = modifier
                    .wrapContentSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Text(
                    modifier = modifier
                        .padding(bottom = 8.dp),
                    text = "Qty",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = "${cartItem.quantity}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }

            TextButton(
                modifier = Modifier,
                onClick = { onRemove(cartItem) }
            ) {
                Text(
                    text = "Remove",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
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
            .width(220.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            val painter = rememberAsyncImagePainter(
                model = serviceAddOn.image,
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
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = serviceAddOn.description,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
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
                    text = "Add",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CustomServiceCard(
    service: Service,
    onServiceClick: (Service) -> Unit
) {
    Card(
        onClick = { onServiceClick(service) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(service.image)
                        .crossfade(true)
                        .placeholder(R.drawable.cleaning1)
                        .error(R.drawable.errorimg)
                        .build(),
                    contentScale = ContentScale.Crop
                ),
                contentDescription = service.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = service.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.formattedPrice,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}
