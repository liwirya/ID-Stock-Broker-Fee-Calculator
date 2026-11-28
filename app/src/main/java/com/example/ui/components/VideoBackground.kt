package com.example.ui.components

import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun MutedLoopingVideoBackground(
    @RawRes videoResId: Int,
    modifier: Modifier = Modifier,
    scrimAlpha: Float = 0.24f
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoUri = remember(videoResId) {
        Uri.parse("android.resource://${context.packageName}/$videoResId")
    }

    val holder = remember { VideoPlayerHolder() }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> holder.resume()
                Lifecycle.Event.ON_PAUSE -> holder.pause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            holder.release()
        }
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF070B14))) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                TextureView(ctx).apply {
                    isOpaque = false
                    surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                        override fun onSurfaceTextureAvailable(
                            surfaceTexture: SurfaceTexture,
                            width: Int,
                            height: Int
                        ) {
                            holder.attachAndStart(
                                context = ctx.applicationContext,
                                textureView = this@apply,
                                surfaceTexture = surfaceTexture,
                                videoUri = videoUri,
                                viewWidth = width,
                                viewHeight = height
                            )
                        }

                        override fun onSurfaceTextureSizeChanged(
                            surfaceTexture: SurfaceTexture,
                            width: Int,
                            height: Int
                        ) {
                            holder.updateCenterCropTransform(this@apply, width, height)
                        }

                        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                            holder.release()
                            return true
                        }

                        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) = Unit
                    }
                }
            }
        )

        if (scrimAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = scrimAlpha))
            )
        }
    }
}

private class VideoPlayerHolder {
    private var mediaPlayer: MediaPlayer? = null
    private var surface: Surface? = null
    private var currentVideoWidth: Int = 0
    private var currentVideoHeight: Int = 0
    private var isPrepared: Boolean = false

    fun attachAndStart(
        context: android.content.Context,
        textureView: TextureView,
        surfaceTexture: SurfaceTexture,
        videoUri: Uri,
        viewWidth: Int,
        viewHeight: Int
    ) {
        release()
        try {
            val createdSurface = Surface(surfaceTexture)
            surface = createdSurface
            val player = MediaPlayer().apply {
                setDataSource(context, videoUri)
                setSurface(createdSurface)
                isLooping = true
                setVolume(0f, 0f)
                setOnVideoSizeChangedListener { _, w, h ->
                    currentVideoWidth = w
                    currentVideoHeight = h
                    updateCenterCropTransform(
                        textureView,
                        textureView.width.takeIf { it > 0 } ?: viewWidth,
                        textureView.height.takeIf { it > 0 } ?: viewHeight
                    )
                }
                setOnPreparedListener { mp ->
                    isPrepared = true
                    mp.setVolume(0f, 0f)
                    mp.isLooping = true
                    currentVideoWidth = mp.videoWidth
                    currentVideoHeight = mp.videoHeight
                    updateCenterCropTransform(
                        textureView,
                        textureView.width.takeIf { it > 0 } ?: viewWidth,
                        textureView.height.takeIf { it > 0 } ?: viewHeight
                    )
                    mp.start()
                }
                setOnErrorListener { _, _, _ -> true }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (_: Exception) {
            release()
        }
    }

    fun updateCenterCropTransform(textureView: TextureView, viewWidth: Int, viewHeight: Int) {
        if (currentVideoWidth <= 0 || currentVideoHeight <= 0 || viewWidth <= 0 || viewHeight <= 0) return
        val viewRatio = viewWidth.toFloat() / viewHeight.toFloat()
        val videoRatio = currentVideoWidth.toFloat() / currentVideoHeight.toFloat()

        val scaleX: Float
        val scaleY: Float
        if (videoRatio > viewRatio) {
            scaleX = videoRatio / viewRatio
            scaleY = 1f
        } else {
            scaleX = 1f
            scaleY = viewRatio / videoRatio
        }

        val matrix = Matrix().apply {
            setScale(scaleX, scaleY, viewWidth / 2f, viewHeight / 2f)
        }
        textureView.setTransform(matrix)
    }

    fun resume() {
        val player = mediaPlayer ?: return
        if (isPrepared && !player.isPlaying) {
            try {
                player.setVolume(0f, 0f)
                player.start()
            } catch (_: Exception) {
            }
        }
    }

    fun pause() {
        val player = mediaPlayer ?: return
        if (isPrepared && player.isPlaying) {
            try {
                player.pause()
            } catch (_: Exception) {
            }
        }
    }

    fun release() {
        isPrepared = false
        try {
            mediaPlayer?.stop()
        } catch (_: Exception) {
        }
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {
        }
        mediaPlayer = null
        try {
            surface?.release()
        } catch (_: Exception) {
        }
        surface = null
    }
}
