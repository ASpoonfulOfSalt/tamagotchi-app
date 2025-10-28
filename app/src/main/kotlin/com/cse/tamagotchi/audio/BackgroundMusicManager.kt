package com.cse.tamagotchi.audio

import android.content.Context
import android.media.MediaPlayer

object BackgroundMusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentVolume: Float = 0.25f

    fun start(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, com.cse.tamagotchi.R.raw.soft_lofi_bg).apply {
                isLooping = true
                setVolume(0.25f, 0.25f) // Gentle volume
                start()
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
