package com.example.titossycleaningservicesapp.presentation.users.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.titossycleaningservicesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartSection() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val count = listOf(1..10)
    BottomSheetScaffold(
        modifier = Modifier,
        sheetContent = {
            LazyColumn {
                items(count) {
                    it.forEach {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Hello $it")
                        }
                    }
                }
            }
        },
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LazyRow(
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(count) { item ->
                    item.forEach { itm ->
                        BookingFrequency(itm)
                    }
                }
            }
        }
    }
}

@Composable
fun BookingFrequency(
    days: Int = 1,
    isSelected: Boolean = false,
    onSelected: (() -> Unit)? = null
) {
    val checked by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(shape = CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.White)
            .clickable { onSelected!! },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$days",
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else Color.Black,
            fontSize = 20.sp
        )
    }
}