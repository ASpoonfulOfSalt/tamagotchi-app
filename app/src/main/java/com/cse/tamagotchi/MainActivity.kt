package com.cse.tamagotchi

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cse.tamagotchi.ui.HomeScreen
import com.cse.tamagotchi.ui.InventoryScreen
import com.cse.tamagotchi.ui.StoreScreen
import com.cse.tamagotchi.ui.theme.TamagotchiTheme
import com.cse.tamagotchi.viewmodel.StoreViewModel
import com.cse.tamagotchi.viewmodel.StoreViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TamagotchiTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainApp() {
    val application = LocalContext.current.applicationContext as Application
    val storeViewModel: StoreViewModel = viewModel(factory = StoreViewModelFactory(application))

    val storeUiState by storeViewModel.uiState.collectAsState()
    val userCoins = storeUiState.userCoins

    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = { 3 })

    LaunchedEffect(storeUiState.purchaseMessage) {
        storeUiState.purchaseMessage?.let {
            snackbarHostState.showSnackbar(it)
            storeViewModel.onPurchaseMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { AppBottomNavigation(pagerState = pagerState) }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding)
        ) {
            when (it) {
                0 -> HomeScreen(
                    coins = userCoins,
                    onFeedClick = { storeViewModel.addCoins(10) }
                )
                1 -> StoreScreen(viewModel = storeViewModel)
                2 -> InventoryScreen(viewModel = storeViewModel)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppBottomNavigation(pagerState: androidx.compose.foundation.pager.PagerState) {
    val scope = rememberCoroutineScope()
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = pagerState.currentPage == 0,
            onClick = { scope.launch { pagerState.animateScrollToPage(0) } }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Store") },
            label = { Text("Store") },
            selected = pagerState.currentPage == 1,
            onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Backpack, contentDescription = "Inventory") },
            label = { Text("Inventory") },
            selected = pagerState.currentPage == 2,
            onClick = { scope.launch { pagerState.animateScrollToPage(2) } }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    TamagotchiTheme {
        MainApp()
    }
}
