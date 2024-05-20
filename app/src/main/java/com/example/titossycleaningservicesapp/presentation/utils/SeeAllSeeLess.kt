package com.example.titossycleaningservicesapp.presentation.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview(showBackground = true)
fun SeeAllSeeLess() {
    val items = listOf("item1", "item2", "item3", "item4", "item5", "item6", "item7")
    var expanded by remember { mutableStateOf(false) }

    LazyColumn(state = rememberLazyListState(), modifier = Modifier.fillMaxSize()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { expanded = !expanded },
                ) {
                    Text(text = if (expanded) "See less" else "See all")
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                        contentDescription = "view more or less"
                    )
                }
            }
        }
        items(if (expanded) items else items.take(3)) { item ->
            AnimatedVisibility(visible = expanded || items.indexOf(item) < 3) {
                IteCard(item = item)
            }
        }
    }
}

@Composable
fun IteCard(item: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item)
        }
    }
}