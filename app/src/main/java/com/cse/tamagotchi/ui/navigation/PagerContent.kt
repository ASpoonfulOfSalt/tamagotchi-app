/**
 * HorizontalPager that holds the main screens.
 *
 * Each index corresponds to a specific page:
 *   0 -> HomeScreen
 *   1 -> TaskScreen
 *   2 -> StoreScreen (TODO: implement later)
 *
 * Keep this file as the single source of truth
 * for "what lives on each page".
 */
package com.cse.tamagotchi.ui.navigation

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.cse.tamagotchi.model.Task
import com.cse.tamagotchi.ui.HomeScreen
import com.cse.tamagotchi.ui.TaskScreen

@Composable
fun PagerContent(
    pagerState: PagerState,
    coins: Int,
    tasks: List<Task>,
    onFeedClick: () -> Unit,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    pageSpacing: Dp
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        pageSpacing = pageSpacing
    ) { page ->
        when (page) {
            0 -> HomeScreen(coins = coins, onFeedClick = onFeedClick)
            1 -> TaskScreen(tasks = tasks, onTaskClick = onTaskClick)
            // 2 -> StoreScreen() // Placeholder
        }
    }
}
