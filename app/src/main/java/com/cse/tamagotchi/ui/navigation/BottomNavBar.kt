/**
 * Bottom navigation bar that syncs with the pager.
 * Each item corresponds to a page index:
 *   0 = Home
 *   1 = Tasks
 *   2 = Store
 *
 * When adding a new page, update both `pages` in MainScreen
 * and extend the icon mapping here.
 */
package com.cse.tamagotchi.ui.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.pager.PagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HabitGotchiBottomNav(
    pages: List<String>,
    pagerState: PagerState,
    scope: CoroutineScope
) {
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