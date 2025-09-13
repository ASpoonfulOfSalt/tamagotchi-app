// ui/MainScreen.kt
package com.cse.tamagotchi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    coins: Int,
    tasks: List<com.cse.tamagotchi.model.Task>,
    onFeedClick: () -> Unit,
    onTaskClick: (String) -> Unit
) {
    val pages = listOf("Home", "Tasks", "Store") // Use a list for page indexing
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HabitGotchi") },
                actions = {
                    Text("Coins: $coins", style = MaterialTheme.typography.bodyLarge)
                }
            )
        },
        bottomBar = {
            NavigationBar {
                pages.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        label = { Text(label) },
                        icon = {
                            when (label) {
                                "Home" -> Icon(Icons.Default.Home, contentDescription = label)
                                "Tasks" -> Icon(Icons.Default.List, contentDescription = label)
                                "Store" -> Icon(Icons.Default.ShoppingCart, contentDescription = label)
                                else -> Icon(Icons.Default.Home, contentDescription = label)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            pageSpacing = 0.dp
        ) { page ->
            when (page) {
                0 -> HomeScreen(coins = coins, onFeedClick = onFeedClick)
                1 -> TaskScreen(tasks = tasks, onTaskClick = onTaskClick)
//                2 -> StoreScreen() // Placeholder for now
            }
        }
    }
}
