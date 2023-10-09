package com.example.wear_study.watchface

import android.content.ContentValues.TAG
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
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
        val h_width = bounds.width() / 2f
        val h_height = bounds.height() / 2f
        val p_black = Paint()

        canvas.drawColor(Color.WHITE)

        p_black.color = Color.BLACK
        p_black.textAlign = Paint.Align.CENTER
        p_black.textSize = 100f
        p_black.strokeWidth = 40f

        val timeText = zonedDateTime.hour.toString() + ":" + zonedDateTime.minute.toString()
        val textBounds = Rect()
        p_black.getTextBounds(timeText, 0, timeText.length, textBounds)
        canvas.drawText(timeText, h_width, h_height-textBounds.exactCenterY(), p_black)
    }

}

class DualAssets: Renderer.SharedAssets {
    override fun onDestroy() {

    }

}