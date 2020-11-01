package com.lounah.vkmc.feature_places.camera.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lounah.vkmc.core.extensions.*
import com.lounah.vkmc.core.ui.camera.CameraButton
import com.lounah.vkmc.feature.feature_places.R
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Flash.OFF
import com.otaliastudios.cameraview.controls.Flash.TORCH
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.util.*
import kotlin.LazyThreadSafetyMode.NONE

internal class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val player by lazy(NONE) { VideoPreviewPlayer(requireContext(), playerView) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initCameraButton()
        initCameraSurface()
        initClickListeners()
    }

    private fun initCameraButton() {
        with(cameraButton) {
            enablePhotoTaking(false)
            enableVideoRecording(true)
            actionListener = object : CameraButton.ActionListener {
                override fun onStartRecord() {
                    cameraSurface.takeVideo(File.createTempFile(UUID.randomUUID().toString(), ".mp4"))
                }
                override fun onEndRecord() = cameraSurface.stopVideo()
                override fun onCancelled() = cameraSurface.stopVideo()
                override fun onDurationTooShortError() = Unit
                override fun onSingleTap() = Unit
            }
        }
    }

    // я знаю что это все неправильно, некрасиво и тд
    // но я пишу уже в режиме хакатона, и на проде такого бы не было
    var file: File? = null

    private fun initCameraSurface() {
        with(cameraSurface) {
            setLifecycleOwner(viewLifecycleOwner)
            addCameraListener(object : CameraListener() {
                override fun onVideoTaken(result: VideoResult) {
                    file = result.file
                    enableVideoPreviewMode(result.file)
                }
            })
        }
    }

    private fun initClickListeners() {
        switchCamera.setOnClickListener { cameraSurface.toggleFacing() }
        flash.setOnClickListener { toggleFlash() }
        back.setOnClickListener { requireActivity().onBackPressed() }
        cancel.setOnClickListener { enableVideoRecordingMode() }
        next.setOnClickListener {
            StorySettingsDialog.newInstance(file?.path.orEmpty())
                .show(childFragmentManager, null)
        }
    }

    private fun enableVideoPreviewMode(video: File) {
        playerView.show()
        controls.animateTranslationY(controls.height)
        cancel.animateAlpha(1)
        next.animateTranslationY(0)
        player.play(video)
    }

    private fun enableVideoRecordingMode() {
        playerView.hide()
        next.animateTranslationY(next.height + 20.dp(requireContext()), duration = 150)
        controls.animateTranslationY(0, duration = 150L)
        cancel.animateAlpha(0, duration = 150L)
        player.clear()
    }

    private fun toggleFlash() {
        cameraSurface.flash = when (cameraSurface.flash) {
            TORCH -> OFF
            else -> TORCH
        }
    }
}
