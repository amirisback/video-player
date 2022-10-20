package com.frogobox.kickstart.mvvm.watch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// Created by (M. Faisal Amir) on 09/08/22.


class MainViewModel : ViewModel() {

    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }

    private var _seekExoPlayer = MutableLiveData<SeekExoPlayer>()
    var seekExoPlayer: LiveData<SeekExoPlayer> = _seekExoPlayer

    private var _playWhenReady = MutableLiveData<Boolean>().apply {
        postValue(true)
    }

    var playWhenReady: LiveData<Boolean> = _playWhenReady

    fun setPlayWhenReady(playWhenReady: Boolean) {
        _playWhenReady.value = playWhenReady
    }

    fun setSeekExoPlayer(seekExoPlayer: SeekExoPlayer) {
        _seekExoPlayer.postValue(seekExoPlayer)
        Log.d(TAG, "setSeekExoPlayer - Current Item : $seekExoPlayer")
        Log.d(TAG, "setSeekExoPlayer - Play Back Position : $seekExoPlayer")
    }

}

data class SeekExoPlayer(val currentItem: Int = 0, var playBackPosition: Long = 0L)