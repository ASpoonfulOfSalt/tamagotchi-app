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

import android.app.Activity
import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cse.tamagotchi.ui.HomeScreen
import com.cse.tamagotchi.ui.InventoryScreen
import com.cse.tamagotchi.ui.StoreScreen
import com.cse.tamagotchi.ui.TaskScreen
import com.cse.tamagotchi.viewmodel.HomeViewModel
import com.cse.tamagotchi.viewmodel.StoreViewModel
import com.cse.tamagotchi.viewmodel.StoreViewModelFactory
import com.cse.tamagotchi.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppNavRoot() {
    val pagerState = rememberPagerState(initialPage = 2, pageCount = { 5 })
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as? Activity

    // Create VMs here with factories (for now, keep store only)
    val storeViewModel: StoreViewModel = viewModel(factory = StoreViewModelFactory(LocalContext.current.applicationContext as Application))
    val taskViewModel: TaskViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    // Settings VM will come later

    // Back button behavior
    BackHandler {
        if (pagerState.currentPage != 2) {
            scope.launch { pagerState.animateScrollToPage(2) }
        } else {
            activity?.moveTaskToBack(true)
        }
    }

    Scaffold(
        topBar = {
            val uiState by storeViewModel.uiState.collectAsState()
            HabitGotchiTopBar(uiState.userCoins)
                 },
        bottomBar = { HabitGotchiBottomNav(pagerState, scope) }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> TaskScreen(viewModel = taskViewModel)
                1 -> StoreScreen(viewModel = storeViewModel)
                2 -> HomeScreen(viewModel = storeViewModel)
                3 -> InventoryScreen(viewModel = storeViewModel)
                4 -> SettingsScreen() // stub for now
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Settings screen coming soon!")
    }
}
