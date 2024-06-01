package com.example.titossycleaningservicesapp.presentation.users.finance.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.titossycleaningservicesapp.R
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Finance profile screen")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyCard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 8.dp,
                pressedElevation = 10.dp,
                focusedElevation = 6.dp,
                hoveredElevation = 10.dp,
                disabledElevation = 0.dp,
                draggedElevation = 10.dp
            )
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(154.dp),
                painter = painterResource(id = R.drawable.onboarding5),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                text = "Home Cleaning",
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color.White.copy(alpha = 0.9f),
                contentColor = Color.Black
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 8.dp,
                pressedElevation = 10.dp,
                focusedElevation = 6.dp,
                hoveredElevation = 10.dp,
                disabledElevation = 0.dp,
                draggedElevation = 10.dp
            )
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(154.dp),
                painter = painterResource(id = R.drawable.onboarding3),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                text = "Home Cleaning",
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PagerPreview() {
    Column {
        val cities = listOf("Nairobi", "Kampala", "Dodoma")
        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { 3 }
        )

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator()
            }
        ) {
            cities.forEachIndexed { index, title ->
                val selected = pagerState.currentPage == index
                Tab(
                    text = { Text(text = title) },
                    selected = selected,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        }

        HorizontalPager(state = pagerState, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ) {page ->
            when(page) {
                0 -> {}
                1 -> {}
            }
        }
    }
}