package com.example.titossycleaningservicesapp.presentation.users.supervisor.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import com.example.titossycleaningservicesapp.domain.models.BookingStatus
import com.example.titossycleaningservicesapp.domain.viewmodel.BookingViewModel
import com.example.titossycleaningservicesapp.presentation.users.supervisor.util.BookingRoutes

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues
) {

    val bookingViewModel: BookingViewModel = hiltViewModel()
    val bookingUiState by bookingViewModel.bookingUiState.collectAsStateWithLifecycle()
    val allBookings = bookingUiState.bookings?.size ?: 0
    val pendingBookings =
        bookingUiState.bookings?.filter { it.bookingStatus == BookingStatus.PENDING }?.size ?: 0
    val approvedBookings =
        bookingUiState.bookings?.filter { it.bookingStatus == BookingStatus.APPROVED }?.size ?: 0
    val inProgressBookings =
        bookingUiState.bookings?.filter { it.bookingStatus == BookingStatus.IN_PROGRESS }?.size ?: 0
    val completedBookings =
        bookingUiState.bookings?.filter { it.bookingStatus == BookingStatus.COMPLETED }?.size ?: 0
    val cancelledBookings =
        bookingUiState.bookings?.filter { it.bookingStatus == BookingStatus.CANCELLED }?.size ?: 0
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        bookingViewModel.fetchBookings()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(state = scrollState)
    ) {
        Spacer(modifier = modifier.height(16.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                modifier = modifier
                    .weight(1f),
                onClick = { navController.navigate(BookingRoutes.AllBookings.route) },
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 6.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    modifier = modifier
                        .padding(top = 4.dp)
                        .size(60.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "all bookings",
                    tint = colorResource(id = R.color.all_bookings)
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "$allBookings",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "ALL",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
            ElevatedCard(
                modifier = modifier
                    .weight(1f),
                onClick = { navController.navigate(BookingRoutes.PendingBookings.route) },
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 6.dp
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    modifier = modifier
                        .padding(top = 4.dp)
                        .size(60.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.HourglassEmpty,
                    contentDescription = "pending bookings",
                    tint = colorResource(id = R.color.pending)
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "$pendingBookings",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "PENDING",
                    color = colorResource(id = R.color.pending),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = modifier.height(16.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                modifier = modifier
                    .weight(1f),
                onClick = {
                    navController.navigate(BookingRoutes.ApprovedBookings.route)
                },
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 6.dp
                ),
                colors = CardDefaults.elevatedCardColors(),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    modifier = modifier
                        .padding(top = 4.dp)
                        .size(60.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "approved bookings",
                    tint = colorResource(id = R.color.approved)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = approvedBookings.toString(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "APPROVED",
                    color = colorResource(id = R.color.approved),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
            ElevatedCard(
                modifier = modifier
                    .weight(1f),
                onClick = {
                    navController.navigate(BookingRoutes.InProgressBookings.route)
                },
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 6.dp
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    modifier = modifier
                        .padding(top = 4.dp)
                        .size(60.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.Sync,
                    contentDescription = "in progress bookings",
                    tint = colorResource(id = R.color.in_progress)
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "$inProgressBookings",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "IN PROGRESS",
                    color = colorResource(id = R.color.in_progress),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = modifier.height(16.dp))

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                modifier = modifier
                    .weight(1f),
                onClick = {
                    navController.navigate(BookingRoutes.CompletedBookings.route)
                },
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 6.dp
                ),
                colors = CardDefaults.elevatedCardColors(),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    modifier = modifier
                        .padding(top = 4.dp)
                        .size(60.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.CheckBox,
                    contentDescription = "completed bookings",
                    tint = colorResource(id = R.color.completed)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "$completedBookings",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "COMPLETED",
                    color = colorResource(id = R.color.completed),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
            ElevatedCard(
                modifier = modifier
                    .weight(1f),
                onClick = {
                    navController.navigate(BookingRoutes.CancelledBookings.route)
                },
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 6.dp
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    modifier = modifier
                        .padding(top = 4.dp)
                        .size(60.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.Close,
                    contentDescription = "cancelled bookings",
                    tint = Color.Red
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "$cancelledBookings",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "CANCELLED",
                    color = Color.Red,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

