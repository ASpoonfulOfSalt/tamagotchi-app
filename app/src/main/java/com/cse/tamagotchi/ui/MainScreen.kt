/**
 * MainScreen is the root composable that ties together:
 * - TopBar (persistent app header)
 * - BottomNavBar (navigation between pages)
 * - HorizontalPager (swipeable pages: Home, Tasks, Store)
 *
 * This should stay fairly "lightweight": it only wires components together.
 * Keep page-specific UI logic in their own files.
 */

package com.cse.tamagotchi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.cse.tamagotchi.ui.navigation.HabitGotchiBottomNav
import com.cse.tamagotchi.ui.navigation.HabitGotchiTopBar
import com.cse.tamagotchi.ui.navigation.PagerContent
import com.cse.tamagotchi.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    coins: Int,
    tasks: List<Task>,
    onFeedClick: () -> Unit,
    onTaskClick: (String) -> Unit
) {
    val pages = listOf("Home", "Tasks", "Store") // Use a list for page indexing
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { HabitGotchiTopBar(coins) },
        bottomBar = { HabitGotchiBottomNav(pages, pagerState, scope) }
    ) { innerPadding ->
        PagerContent(
            pagerState = pagerState,
            coins = coins,
            tasks = tasks,
            onFeedClick = onFeedClick,
            onTaskClick = onTaskClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            pageSpacing = 0.dp
        )
    }
}
