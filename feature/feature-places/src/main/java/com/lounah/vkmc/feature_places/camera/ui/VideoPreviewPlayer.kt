package com.lounah.vkmc.feature_places.camera.ui

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes
import java.io.File

internal class VideoPreviewPlayer(
    private val context: Context,
    private val playerView: PlayerView
) {

    private var player: Player? = null

    init {
        initPlayer()
    }

    fun play(video: File) {
        player?.setMediaItem(buildMediaItem(video))
        player?.play()
    }

    fun clear() {
        player?.release()
        initPlayer()
    }

    private fun initPlayer() {
        val trackSelector = DefaultTrackSelector(context)
        trackSelector.setParameters(
            trackSelector
                .buildUponParameters()
                .setMaxVideoSizeSd()
        )
        player = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .apply {
                playWhenReady = true
                prepare()
                playerView.player = this
            }
    }

    private fun buildMediaItem(from: File) =
        MediaItem.Builder()
            .setUri(Uri.fromFile(from))
            .setMimeType(MimeTypes.VIDEO_MP4)
            .build()
}
