package com.example.wear_study.watchface

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.TapEvent
import androidx.wear.watchface.TapType
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

class DualCanvasRenderer(
    context: Context,
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
), WatchFace.TapListener {
    private val sharedPrefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            invalidate() // Invalidate the renderer to trigger a redraw
        }

    private val sharedPref = context.getSharedPreferences("WatchFacePreferences", MODE_PRIVATE)

    override fun onDestroy() {
        sharedPref.unregisterOnSharedPreferenceChangeListener(sharedPrefChangeListener)
    }

    init {
        // Register the listener
        sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefChangeListener)
    }

    var isTapped = false;
    val bg_light = Color.parseColor("#f8dfb6");
    val bg_dark = Color.parseColor("#46698c");

    override suspend fun createSharedAssets(): DualAssets {
        return DualAssets()
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DualAssets
    ) {
        Log.d(TAG, "renderHighlightLayer: rendered")
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DualAssets
    ) {
        val message = sharedPref.getString("message", null) ?: "No message received"
        val h_width = bounds.width() / 2f
        val h_height = bounds.height() / 2f
        val p_black = Paint()
        val p_message = Paint()

        p_message.color = Color.BLACK
        p_message.textSize = 50f

        if (isTapped) {
            canvas.drawColor(bg_light)
            p_black.color = Color.BLACK
        } else {
            canvas.drawColor(bg_dark)
            p_black.color = Color.WHITE
        }

        p_black.textAlign = Paint.Align.CENTER
        p_black.textSize = 100f
        p_black.strokeWidth = 40f

        val minute =
            if (zonedDateTime.minute < 10) "0" + zonedDateTime.minute.toString() else zonedDateTime.minute.toString()
        val timeText = zonedDateTime.hour.toString() + ":" + minute

        val textBounds = Rect()
        p_black.getTextBounds(timeText, 0, timeText.length, textBounds)
        canvas.drawText(timeText, h_width, h_height-textBounds.exactCenterY(), p_black)

        canvas.drawText(message, 75f, 37f, p_message)
    }

    override fun onTapEvent(tapType: Int, tapEvent: TapEvent, complicationSlot: ComplicationSlot?) {
        if (tapType == TapType.UP) {
            isTapped = !isTapped;
        }
    }


}

class DualAssets : Renderer.SharedAssets {
    override fun onDestroy() {

    }

}