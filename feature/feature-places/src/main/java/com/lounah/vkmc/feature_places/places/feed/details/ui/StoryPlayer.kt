package com.lounah.vkmc.feature_places.places.feed.details.ui

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes.VIDEO_MP4
import com.google.firebase.storage.StorageReference
import com.lounah.vkmc.feature_places.places.preview.ui.recycler.StoryUi
import java.io.File

private const val TEMP_FILE_PREFIX = "local_story"
private const val TEMP_FILE_SUFFIX = ".mp4"

internal interface StoryPlayer {
    fun play(story: StoryUi, playerView: PlayerView, onError: (Throwable) -> Unit = {})
    fun release()
}

internal class StoryPlayerImpl(
    private val context: Context,
    private val storageReference: StorageReference,
    onVideoViewed: () -> Unit
) : StoryPlayer {

    private val trackSelector = DefaultTrackSelector(context).apply {
        setParameters(buildUponParameters().setMaxVideoSizeSd())
    }
    private var player: Player = initPlayer(onVideoViewed)

    override fun play(story: StoryUi, playerView: PlayerView, onError: (Throwable) -> Unit) {
        val localStory = File.createTempFile(TEMP_FILE_PREFIX + story.id, TEMP_FILE_SUFFIX)
        storageReference.child(story.uri).getFile(localStory)
            .addOnSuccessListener {
                player.setMediaItem(buildMediaItem(localStory))
                player.prepare()
                playerView.player = player
                player.play()
            }
            .addOnFailureListener(onError)
    }

    override fun release() {
        player.release()
    }

    private fun buildMediaItem(from: File) =
        MediaItem.Builder()
            .setUri(Uri.fromFile(from))
            .setMimeType(VIDEO_MP4)
            .build()

    private fun initPlayer(onVideoViewed: () -> Unit): Player {
        return SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .build()
            .apply {
                playWhenReady = true
                addListener(object : Player.EventListener {
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_ENDED) {
                            release()
                            player = initPlayer(onVideoViewed)
                            onVideoViewed()
                        }
                    }
                })
            }
    }
}
