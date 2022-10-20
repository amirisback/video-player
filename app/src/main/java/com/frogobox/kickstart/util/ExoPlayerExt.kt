package com.frogobox.kickstart.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.SparseArray
import com.frogobox.kickstart.util.lib.youtube.VideoMeta
import com.frogobox.kickstart.util.lib.youtube.YouTubeExtractor
import com.frogobox.kickstart.util.lib.youtube.YtFile
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes


/**
 * Created by Faisal Amir on 10/08/22
 * -----------------------------------------
 * E-mail   : faisalamircs@gmail.com
 * Github   : github.com/amirisback
 * -----------------------------------------
 * Copyright (C) Frogobox ID / amirisback
 * All rights reserved
 */


const val TYPE_MP4 = ".mp4"
const val TYPE_M3U8 = ".m3u8"
const val TYPE_YOUTUBE = "https://www.youtube.com/"
const val TYPE_YOUTUBE_DASH = "![CDATA["

fun ExoPlayer.setMediaItemExt(uriMedia: String) {
    val mediaItem = MediaItem.fromUri(uriMedia)
    setMediaItem(mediaItem)
}

fun ExoPlayer.setMediaItemsExt(uriMedia: List<String>) {
    val mediaItems = mutableListOf<MediaItem>()
    uriMedia.forEach {
        mediaItems.add(MediaItem.fromUri(it))
    }
    setMediaItems(mediaItems)

}

fun ExoPlayer.setMediaItemsExt(uriMedia: List<String>, position: Int) {
    Log.d("ExoPlayer", "uri Media Size: ${uriMedia.size}")
    Log.d("ExoPlayer", "uri Media Position: $position")
    Log.d("ExoPlayer", "uri Media: ${uriMedia[position]}")


    // Initiate MediaItems
    val mediaItems = mutableListOf<MediaItem>()
    uriMedia.forEach {
        mediaItems.add(MediaItem.fromUri(it))
    }

    setMediaItems(mediaItems, position, 0L)

    Log.d("ExoPlayer", "MediaItem: $mediaItemCount")

}

fun ExoPlayer.setMediaSourceExt(uriMedia: String) {

    // Create a data source factory.
    val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    // Create a HLS media source pointing to a playlist uri.
    val hlsMediaSource =
        HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(uriMedia))

    // Create a player instance.
    setMediaSource(hlsMediaSource)

}

fun ExoPlayer.setMediaSourcesExt(uriMedia: List<String>) {

    // Create a data source factory.
    val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    // Create a HLS media source pointing to a playlist uri.
    val mediaSources = mutableListOf<MediaSource>()

    uriMedia.forEach {
        mediaSources.add(
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(it))
        )
    }

    // Create a player instance.
    setMediaSources(mediaSources)

}

fun ExoPlayer.setMediaSourcesExt(uriMedia: List<String>, position: Int) {

    // Create a data source factory.
    val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    // Create a HLS media source pointing to a playlist uri.
    val mediaSources = mutableListOf<MediaSource>()

    uriMedia.forEach {
        mediaSources.add(
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(it))
        )
    }

    // Create a player instance.
    setMediaSources(mediaSources, position, 0L)

}

fun ExoPlayer.setMediaYoutubeDashExt(urlYoutube: String) {
    val mediaItem = MediaItem.Builder()
        .setUri(urlYoutube)
        .setMimeType(MimeTypes.APPLICATION_MPD)
        .build()
    setMediaItem(mediaItem)
}

fun ExoPlayer.setMediaYoutubeExt(context: Context, urlYoutube: String) {

    extractYoutubeUrl(context, urlYoutube) { sparseArray ->
        val iTag = 137//tag of video 1080
        val audioTag = 140 //tag m4a audio
        // 720, 1080, 480
        var videoUrl = ""
        val iTags: List<Int> = listOf(22, 137, 18)
        for (i in iTags) {
            val ytFile = sparseArray.get(i)
            if (ytFile != null) {
                val downloadUrl = ytFile.url
                if (downloadUrl != null && downloadUrl.isNotEmpty()) {
                    videoUrl = downloadUrl
                }
            }
        }
        if (videoUrl == "")
            videoUrl = sparseArray[iTag].url
        val audioUrl = sparseArray[audioTag].url
        val audioSource: MediaSource = ProgressiveMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(audioUrl))
        val videoSource: MediaSource = ProgressiveMediaSource
            .Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(videoUrl))
        this.setMediaSource(MergingMediaSource(true, videoSource, audioSource), true)
    }

}

fun ExoPlayer.setSingleMediaExt(context: Context, resource: String) {
    // Setup Media Single Video
    if (resource.contains(TYPE_MP4)) {
        this.setMediaItemExt(resource)
    } else if (resource.contains(TYPE_M3U8)) {
        // Setup Media HLS
        this.setMediaSourceExt(resource)
    } else if (resource.contains(TYPE_YOUTUBE)) {
        // Setup Media Youtube
        this.setMediaYoutubeExt(context, resource)
    } else if (resource.contains(TYPE_YOUTUBE_DASH)) {
        this.setMediaYoutubeDashExt(resource)
    }
}


private fun extractYoutubeUrl(
    context: Context,
    urlYoutube: String,
    onPlay: (ytFile: SparseArray<YtFile>) -> Unit
) {
    @SuppressLint("StaticFieldLeak") val mExtractor: YouTubeExtractor =
        object : YouTubeExtractor(context) {
            override fun onExtractionComplete(
                sparseArray: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (sparseArray != null) {
                    onPlay(sparseArray)
                }
            }
        }
    mExtractor.extract(urlYoutube)
}