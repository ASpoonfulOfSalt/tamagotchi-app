package com.cse.tamagotchi.audio

import android.content.Context
import android.media.MediaPlayer
import com.cse.tamagotchi.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object BackgroundMusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentVolume: Float = 0.00f

    fun start(context: Context) {
        if (mediaPlayer == null) {
            val userPrefs = UserPreferencesRepository(context.applicationContext)
            CoroutineScope(Dispatchers.Main).launch {
                val savedVolume = userPrefs.musicVolume.first().coerceIn(0f, 1f)
                currentVolume = savedVolume

                mediaPlayer = MediaPlayer.create(context, com.cse.tamagotchi.R.raw.soft_lofi_bg).apply {
                    isLooping = true
                    setVolume(currentVolume, currentVolume) // Set volume **before starting**
                    start()
                }
            }
        } else if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }


    fun pause() {
        mediaPlayer?.pause()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(currentVolume, currentVolume)
    }

    fun getVolume(): Float = currentVolume
}
