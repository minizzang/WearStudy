package com.example.wear_study.watchface

import android.graphics.Canvas
import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

class DualCanvasRenderer(
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState, canvasType: Int,
    clearWithBackgroundTintBeforeRenderingHighlightLayer: Boolean
) : Renderer.CanvasRenderer2<DualAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    interactiveDrawModeUpdateDelayMillis = 16L,
    clearWithBackgroundTintBeforeRenderingHighlightLayer
) {
    override suspend fun createSharedAssets(): DualAssets {
        return DualAssets()
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DualAssets
    ) {
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DualAssets
    ) {
    }
}

class DualAssets: Renderer.SharedAssets {
    override fun onDestroy() {
        TODO("Not yet implemented")
    }

}