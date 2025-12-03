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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cse.tamagotchi.data.AppDatabase
import com.cse.tamagotchi.repository.*
import com.cse.tamagotchi.ui.*
import com.cse.tamagotchi.ui.onboarding.OnboardingScreen
import com.cse.tamagotchi.ui.theme.TamagotchiTheme
import com.cse.tamagotchi.viewmodel.*
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

    val storeRepository = StoreRepository(database.storeItemDao())
    val taskRepository = TaskRepository(application, database.taskDao())
    val tamagotchiRepository = TamagotchiRepository(application)
    val statsRepository = StatsRepository(application)

    val tamagotchiViewModel: TamagotchiViewModel =
        viewModel(factory = TamagotchiViewModelFactory(tamagotchiRepository, storeRepository))

    val storeViewModel: StoreViewModel =
        viewModel(factory = StoreViewModelFactory(storeRepository, tamagotchiRepository))

    val taskViewModel: TaskViewModel =
        viewModel(factory = TaskViewModelFactory(taskRepository, tamagotchiRepository, userPrefs, statsRepository))

    val settingsViewModel: SettingsViewModel =
        viewModel(factory = SettingsViewModelFactory(userPrefs, database, taskRepository, tamagotchiRepository))

    val statsViewModel: StatsViewModel = viewModel(
        factory = StatsViewModelFactory(
            statsRepository = statsRepository,
            userPrefs = userPrefs,
            storeRepository = storeRepository,
            tamagotchiRepository = tamagotchiRepository
        )
    )


    // Hoist dark mode state
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState(initial = false)
    val hasSeenOnboarding by userPrefs.hasSeenOnboarding.collectAsState(initial = true)

    BackHandler {
        if (pagerState.currentPage != 2) {
            scope.launch { pagerState.animateScrollToPage(2) }
        } else {
            activity?.moveTaskToBack(true)
        }
    }

    TamagotchiTheme(darkTheme = isDarkMode) {
        if (!hasSeenOnboarding) {
            OnboardingScreen { petName, prefersDark ->
                scope.launch {
                    userPrefs.setOnboardingComplete()
                    userPrefs.toggleTheme(prefersDark)
                    tamagotchiViewModel.renamePet(petName)
                }
            }
        } else {
            Scaffold(
                containerColor = Color.Transparent,
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
                        0 -> StatsScreen(viewModel = statsViewModel)

                        1 -> StoreScreen(viewModel = storeViewModel, isDarkMode = isDarkMode)

                        2 -> HomeScreen(
                            tamagotchiViewModel = tamagotchiViewModel,
                            isDarkMode = isDarkMode,
                            userPreferencesRepository = userPrefs,
                            taskViewModel = taskViewModel
                        )

                        3 -> TaskScreen(viewModel = taskViewModel, isDarkMode = isDarkMode)

                        4 -> SettingsScreen(viewModel = settingsViewModel)
                    }
                }
            }
        }
    }
}
