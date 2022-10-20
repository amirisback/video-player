package com.frogobox.kickstart.mvvm.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.frogobox.kickstart.core.BaseViewModel
import com.frogobox.kickstart.source.ProjectDataRepository

class VideoViewModel(
    private val context: Application,
    private val repository: ProjectDataRepository,
) : BaseViewModel(context, repository) {

    private var _simpleVideoList = MutableLiveData<MutableList<Video>>()
    var simpleVideoList: LiveData<MutableList<Video>> = _simpleVideoList


    fun getSimpleVideoList() {
        val data = mutableListOf<Video>()
        for (i in 1..16) {
            data.add(Video(i, "Episode $i", getBaseUrlVideo(if (i < 9) "0$i" else "$i")))
        }
        _simpleVideoList.postValue(data)
    }

    private fun getBaseUrlVideo(episode: String): String {
        return "https://ia802802.us.archive.org/30/items/mahabharat2013dubbingindo/0${episode}.mp4"
    }

}