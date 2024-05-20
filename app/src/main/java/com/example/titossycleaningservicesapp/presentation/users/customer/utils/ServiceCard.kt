package com.example.titossycleaningservicesapp.presentation.users.customer.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.data.remote.api.ApiConstants.BASE_URL
import com.example.titossycleaningservicesapp.domain.models.ui_models.Service
import com.example.titossycleaningservicesapp.domain.models.ui_models.ServiceAddOn
import java.math.BigDecimal
import java.util.UUID

@Composable
fun ServiceCardInCart(
    service: Service,
    onServiceClick: (Service) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(4.dp),
        onClick = { onServiceClick(service) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    contentScale = ContentScale.Crop,
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("$BASE_URL${service.image}")
                        .crossfade(true)
                        .placeholder(R.drawable.cleaning1)
                        .error(R.drawable.errorimg)
                        .build()

                ),
                contentDescription = service.name,
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
                    text = service.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = service.description,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Kshs. ${service.price}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary
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
                    .height(150.dp)
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
            Spacer(modifier = Modifier.height(16.dp))

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
fun ServiceCardPreview() {
    val sampleService = Service(
        id = UUID.fromString("f9092838-991a-457c-91d4-e449fc4ef809"),
        name = "Living room cleaning",
        description = "This is a home cleaning service specifically deep kitchen cleaning",
        price = BigDecimal("1294.00"),
        image = "http://127.0.0.1:8080/api/uploads/service/me.jpg",
        addOns = listOf(
            ServiceAddOn(
                id = UUID.fromString("1167a073-08c1-4fae-85d3-dfeb4dd6293b"),
                serviceId = UUID.fromString("f9092838-991a-457c-91d4-e449fc4ef809"),
                name = "Balcony",
                description = "This is balcony cleaning",
                price = BigDecimal("2390.00"),
                image = "http://127.0.0.1:8080/api/uploads/addon/people.png",
            )
        )
    )
    ServiceCardInCart(service = sampleService, onServiceClick = {})
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
            .padding(8.dp)
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
                text = "Kshs. ${service.formattedPrice}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}


@Composable
fun BottomRow(
    navController: NavHostController,
    showBottomRow: MutableState<Boolean>,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Total")
            Text("100.00")
        }
        Spacer(modifier = modifier.weight(1f))
        Button(
            onClick = { navController.navigate(DetailsRoutes.BookingDetails.route)
            }
        ) {
            Text("Checkout")
        }
    }
    showBottomRow.value = true
}


@Composable
fun CheckoutScreen(navController: NavHostController, showBottomRow: MutableState<Boolean>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Checkout Screen",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            // Simulate checkout completion
            showBottomRow.value = true
            navController.navigate("home")
        }) {
            Text("Complete Checkout")
        }
    }
}

