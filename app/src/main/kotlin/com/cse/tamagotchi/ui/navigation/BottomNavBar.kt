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

import android.view.SoundEffectConstants
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HabitGotchiBottomNav(
    pagerState: PagerState,
    scope: CoroutineScope
) {
    val view = LocalView.current
    NavigationBar(
        containerColor = Color.Transparent
    ) {
        val items = listOf(
            "Stats" to Icons.Filled.CalendarMonth,
            "Store" to Icons.Filled.ShoppingCart,
            "Home" to Icons.Filled.Home,
            "Daily" to Icons.Filled.Task,
            "Settings" to Icons.Filled.Settings
        )
        items.forEachIndexed { index, (label, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                selected = pagerState.currentPage == index,
                onClick = { 
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    scope.launch { pagerState.animateScrollToPage(index) } 
                }
            )
        }
    }
}