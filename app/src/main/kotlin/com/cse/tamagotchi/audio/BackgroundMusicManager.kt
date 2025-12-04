package com.cse.tamagotchi.audio

import android.content.Context
import android.media.MediaPlayer
import com.cse.tamagotchi.R
import com.cse.tamagotchi.repository.UserPreferencesRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlin.random.Random

object BackgroundMusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentVolume = 0f
    private var musicList: List<Int> = emptyList()
    private var lastPlayedIndex: Int? = null

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun start(context: Context) {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) return

        scope.launch {
            // Load user preferences
            val userPrefs = UserPreferencesRepository(context.applicationContext)
            currentVolume = userPrefs.musicVolume.first().coerceIn(0f, 1f)

            // Load all music files dynamically from R.raw
            loadMusicList()

            if (musicList.isEmpty()) {
                // Fallback to your original song if no music_* files exist
                musicList = listOf(R.raw.music_soft_lofi_bg_1)
            }

            playRandomSong(context)
        }
    }

    private fun loadMusicList() {
        val rawClass = R.raw::class.java

        musicList = rawClass.fields
            .filter { it.name.startsWith("music_") } // Only load files named music_*
            .map { it.getInt(null) } // Convert reflection field → resource ID
    }

    private fun playRandomSong(context: Context) {
        val nextIndex = getNextRandomIndex()
        lastPlayedIndex = nextIndex
        val resId = musicList[nextIndex]

        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = false
            setVolume(currentVolume, currentVolume)
            setOnCompletionListener {
                playRandomSong(context) // Auto-queue next song
            }
            start()
        }
    }

    private fun getNextRandomIndex(): Int {
        if (musicList.size == 1) return 0

        val last = lastPlayedIndex
        var index: Int

        do {
            index = Random.nextInt(musicList.size)
        } while (index == last)   // Do not repeat same song

        return index
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
