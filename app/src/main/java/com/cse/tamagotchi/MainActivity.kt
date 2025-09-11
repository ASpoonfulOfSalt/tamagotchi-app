package com.cse.tamagotchi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import com.cse.tamagotchi.ui.theme.TamagotchiTheme
import com.cse.tamagotchi.ui.HomeScreen
import com.cse.tamagotchi.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val coins = homeViewModel.coins.collectAsState().value
            HomeScreen(
                coins = coins,
                onFeedClick = { homeViewModel.addCoins(10) }
            )
        }
    }
}