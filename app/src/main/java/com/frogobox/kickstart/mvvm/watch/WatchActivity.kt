package com.frogobox.kickstart.mvvm.watch

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.frogobox.kickstart.R
import com.frogobox.kickstart.core.BaseActivity
import com.frogobox.kickstart.databinding.ActivityWatchBinding
import com.frogobox.kickstart.util.Constant
import com.frogobox.kickstart.util.setSingleMediaExt
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.util.Util


class WatchActivity : BaseActivity<ActivityWatchBinding>() {

    companion object {
        val TAG = WatchActivity::class.java.simpleName
    }

    private var isFullScreen = false

    private val mainViewModel: MainViewModel by viewModels()

    override fun setupViewBinding(): ActivityWatchBinding {
        return ActivityWatchBinding.inflate(layoutInflater)
    }

    private val videoTitle: String by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra(Constant.Extra.EXTRA_VIDEO_TITLE) ?: ""
    }

    private val videoUrl: String by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra(Constant.Extra.EXTRA_VIDEO_URL) ?: ""
    }

    private val playbackStateListener: Player.Listener = playbackStateListener()
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val btnCloseVideo = binding.videoView.findViewById<ImageView>(R.id.iv_close_video)
        val tvTitleVideo = binding.videoView.findViewById<TextView>(R.id.tv_title_video)
        val fullscreenButton = binding.videoView.findViewById<ImageView>(R.id.exo_fullscreen_icon)

        tvTitleVideo.text = videoTitle

        btnCloseVideo.setOnClickListener {
            finish()
        }

        fullscreenButton.setOnClickListener {
            if (isFullScreen) {
                supportActionBar?.show()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                isFullScreen = false
                fullscreenButton.setImageResource(R.drawable.ic_fullscreen)
            } else {
                supportActionBar?.hide()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                isFullScreen = true
                fullscreenButton.setImageResource(R.drawable.ic_fullscreen_exit)
            }
        }
    }

    private fun setupExoPlayerByViewModel(exoPlayer: ExoPlayer, listener: Player.Listener) {
        mainViewModel.apply {
            playWhenReady.observe(this@WatchActivity) {
                exoPlayer.playWhenReady = it
            }
            seekExoPlayer.observe(this@WatchActivity) {
                exoPlayer.seekTo(it.currentItem, it.playBackPosition)
            }
        }
        exoPlayer.addListener(listener)
        exoPlayer.addAnalyticsListener(object : AnalyticsListener {
            override fun onPlaybackStateChanged(
                eventTime: AnalyticsListener.EventTime,
                state: Int,
            ) {
                super.onPlaybackStateChanged(eventTime, state)
                Log.d(TAG, "onPlaybackStateChanged: $state")
            }
        })
        exoPlayer.prepare()
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {

        player = ExoPlayer.Builder(this).build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer

                // Setup Media Single Video
                // exoPlayer.setMediaItemExt(getString(R.string.media_url_mp4))

                // Setup Media Multiple Video
                // exoPlayer.setMediaSourceExt(getString(R.string.media_url_jwp_2))

                // Setup Media Youtube
                // exoPlayer.setMediaYoutubeExt(this, getString(R.string.media_url_youtube_link_1))

                // Setup Media Single Video Youtube Url
                // exoPlayer.setMediaYoutubeDashExt(getString(R.string.media_url_dash))

                // Setup Handling Media Video
                exoPlayer.setSingleMediaExt(this, videoUrl)

                // Default setup
                setupExoPlayerByViewModel(exoPlayer, playbackStateListener)
            }

    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            mainViewModel.setSeekExoPlayer(
                SeekExoPlayer(
                    exoPlayer.currentPeriodIndex,
                    exoPlayer.currentPosition
                )
            )
            mainViewModel.setPlayWhenReady(exoPlayer.playWhenReady)
            exoPlayer.removeListener(playbackStateListener)
            exoPlayer.release()
        }
        player = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onBackPressed() {
        val position = (player?.currentPosition ?: 0)
        Log.d(TAG, "Position : $position")
        super.onBackPressed()
    }

    private fun playbackStateListener() = object : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {

            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {
                    Log.d(TAG, "ExoPlayer.STATE_IDLE")
                    Log.d(TAG, "Duration : ${player?.duration}")
                    val position = (player?.currentPosition ?: 0)
                    Log.d(TAG, "Position : $position")
                }
                ExoPlayer.STATE_BUFFERING -> {
                    Log.d(TAG, "ExoPlayer.STATE_BUFFERING")
                    Log.d(TAG, "Duration : ${player?.duration}")
                    val position = (player?.currentPosition ?: 0)
                    Log.d(TAG, "Position : $position")
                    binding.progressBar.visibility = View.VISIBLE
                }
                ExoPlayer.STATE_READY -> {
                    Log.d(TAG, "ExoPlayer.STATE_READY")
                    Log.d(TAG, "Duration : ${player?.duration}")
                    val position = (player?.currentPosition ?: 0)
                    Log.d(TAG, "Position : $position")
                    binding.progressBar.visibility = View.GONE
                }
                ExoPlayer.STATE_ENDED -> {
                    Log.d(TAG, "ExoPlayer.STATE_ENDED")
                    Log.d(TAG, "Duration : ${player?.duration}")
                    val position = (player?.currentPosition ?: 0)
                    Log.d(TAG, "Position : $position")
                }
                else -> {
                    Log.d(TAG, "Unknown state")
                }

            }

        }

    }

}