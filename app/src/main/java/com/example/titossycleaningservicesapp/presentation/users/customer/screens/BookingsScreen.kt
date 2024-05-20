package com.example.titossycleaningservicesapp.presentation.users.customer.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.LineStyle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookingsScreen(navController: NavHostController, paddingValues: PaddingValues) {

    val tabList = listOf("All", "Pending", "Completed", "Cancelled")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { tabList.size }
    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Bookings",
                        )
                    }
                })
                TabRow(
                    selectedTabIndex = selectedTabIndex
                ) {
                    tabList.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title, maxLines = 1, overflow = TextOverflow.Clip) },
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index }
                        )
                    }
                }
            }
        }
    ) {
        when (selectedTabIndex) {
            0 -> AllBookingsScreen(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(it)
            )

            1 -> PendingBookingsScreen(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(it)
            )

            2 -> CompletedBookingsScreen(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(it)
            )

            3 -> CancelledBookingsScreen(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(it)
            )
        }
    }

}

@Composable
fun AllBookingsScreen(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "All Bookings Screen", fontSize = MaterialTheme.typography.titleLarge.fontSize)
    }
}

@Composable
fun PendingBookingsScreen(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Pending Bookings Screen",
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
    }
}

@Composable
fun CompletedBookingsScreen(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Completed Bookings Screen",
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
    }
}

@Composable
fun CancelledBookingsScreen(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cancelled Bookings Screen",
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
    }
}

sealed class BookingTabs(
    val title: String,
    val icon: ImageVector
) {
    data object AllBookings : BookingTabs("All", Icons.Outlined.LineStyle)
    data object PendingBookings : BookingTabs("Pending", Icons.Outlined.AccessTime)
    data object CompletedBookings : BookingTabs("Completed", Icons.Outlined.CheckBox)
    data object CancelledBookings : BookingTabs("Cancelled", Icons.Outlined.Cancel)
}