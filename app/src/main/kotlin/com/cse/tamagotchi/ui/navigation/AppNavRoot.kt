package com.cse.tamagotchi.ui.navigation

import android.app.Activity
import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cse.tamagotchi.data.AppDatabase
import com.cse.tamagotchi.repository.StoreRepository
import com.cse.tamagotchi.repository.TamagotchiRepository
import com.cse.tamagotchi.repository.TaskRepository
import com.cse.tamagotchi.repository.UserPreferencesRepository
import com.cse.tamagotchi.ui.HomeScreen
import com.cse.tamagotchi.ui.InventoryScreen
import com.cse.tamagotchi.ui.SettingsScreen
import com.cse.tamagotchi.ui.StoreScreen
import com.cse.tamagotchi.ui.TaskScreen
import com.cse.tamagotchi.viewmodel.SettingsViewModel
import com.cse.tamagotchi.viewmodel.SettingsViewModelFactory
import com.cse.tamagotchi.viewmodel.StoreViewModel
import com.cse.tamagotchi.viewmodel.StoreViewModelFactory
import com.cse.tamagotchi.viewmodel.TamagotchiViewModel
import com.cse.tamagotchi.viewmodel.TamagotchiViewModelFactory
import com.cse.tamagotchi.viewmodel.TaskViewModel
import com.cse.tamagotchi.viewmodel.TaskViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppNavRoot() {
    val pagerState = rememberPagerState(initialPage = 2, pageCount = { 5 })
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as? Activity
    val application = LocalContext.current.applicationContext as Application
    val database = AppDatabase.getDatabase(application)
    val userPrefs = UserPreferencesRepository(application)
    val storeDao = database.storeItemDao()
    val storeRepository = StoreRepository(storeDao)
    val taskDao = database.taskDao()
    val taskRepository = TaskRepository(application, taskDao)
    val tamagotchiRepository = TamagotchiRepository(application)

    // Create VMs here with factories
    val tamagotchiViewModel: TamagotchiViewModel = viewModel(factory = TamagotchiViewModelFactory(tamagotchiRepository, storeRepository))
    val storeViewModel: StoreViewModel = viewModel(factory = StoreViewModelFactory(storeRepository, tamagotchiRepository))
    val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(taskRepository, tamagotchiRepository, userPrefs))
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(
        userPrefs, database, taskRepository, tamagotchiRepository))

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
            val uiState by tamagotchiViewModel.uiState.collectAsState()
            HabitGotchiTopBar(uiState.tamagotchi.currency)
        },
        bottomBar = { HabitGotchiBottomNav(pagerState, scope) }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> StoreScreen(viewModel = storeViewModel)
                1 -> InventoryScreen(viewModel = storeViewModel)
                2 -> HomeScreen(viewModel = tamagotchiViewModel)
                3 -> TaskScreen(viewModel = taskViewModel)
                4 -> SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }
}