package com.cse.tamagotchi.ui.home

import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.*

@Composable
fun produceIsNightState(): State<Boolean> {
    val isNight = remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    fun updateTime() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        isNight.value = (hour >= 21 || hour < 9)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) updateTime()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        updateTime()
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    return isNight
}
