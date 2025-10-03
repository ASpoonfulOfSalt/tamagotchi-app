package com.cse.tamagotchi

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cse.tamagotchi.ui.HomeScreen
import com.cse.tamagotchi.ui.InventoryScreen
import com.cse.tamagotchi.ui.StoreScreen
import com.cse.tamagotchi.ui.theme.TamagotchiTheme
import com.cse.tamagotchi.viewmodel.HomeViewModel
import com.cse.tamagotchi.viewmodel.StoreViewModel
import com.cse.tamagotchi.viewmodel.StoreViewModelFactory

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

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val application = LocalContext.current.applicationContext as Application
    val homeViewModel: HomeViewModel = viewModel()
    val storeViewModel: StoreViewModel = viewModel(factory = StoreViewModelFactory(application))

    val storeUiState by storeViewModel.uiState.collectAsState()
    val userCoins = storeUiState.userCoins

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(storeUiState.purchaseMessage) {
        storeUiState.purchaseMessage?.let {
            snackbarHostState.showSnackbar(it)
            storeViewModel.onPurchaseMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { AppBottomNavigation(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(
                    coins = userCoins,
                    onFeedClick = { storeViewModel.addCoins(10) }
                )
            }
            composable("store") {
                StoreScreen(
                    viewModel = storeViewModel,
                    paddingValues = innerPadding
                )
            }
            composable("inventory") {
                InventoryScreen(
                    viewModel = storeViewModel,
                    paddingValues = innerPadding
                )
            }
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Store") },
            label = { Text("Store") },
            selected = currentRoute == "store",
            onClick = {
                navController.navigate("store") {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Backpack, contentDescription = "Inventory") },
            label = { Text("Inventory") },
            selected = currentRoute == "inventory",
            onClick = {
                navController.navigate("inventory") {
                    launchSingleTop = true
                    restoreState = true
                }
            }
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
