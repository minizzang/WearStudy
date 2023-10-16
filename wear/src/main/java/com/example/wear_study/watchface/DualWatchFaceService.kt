package com.example.wear_study.watchface

import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable

private const val DUAL_COUNT_PATH = "/dual_count"
class DualWatchFaceService: WatchFaceService(), MessageClient.OnMessageReceivedListener {
    private val messageClient by lazy { Wearable.getMessageClient(this)}

    override fun createUserStyleSchema(): UserStyleSchema {
        return super.createUserStyleSchema()
    }

    override fun createComplicationSlotsManager(currentUserStyleRepository: CurrentUserStyleRepository): ComplicationSlotsManager {
        return super.createComplicationSlotsManager(currentUserStyleRepository)
    }
    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        messageClient.addListener(this)

        // Creates class that renders the watch face.
        val renderer = DualCanvasRenderer(
            //context = applicationContext,
            surfaceHolder = surfaceHolder,
            watchState = watchState,
            //complicationSlotsManager = complicationSlotsManager,
            currentUserStyleRepository = currentUserStyleRepository,
            canvasType = CanvasType.HARDWARE,
            clearWithBackgroundTintBeforeRenderingHighlightLayer = true,
        )

        // Creates the watch face.
        return WatchFace(
            watchFaceType = WatchFaceType.ANALOG,
            renderer = renderer
        ).setTapListener(tapListener = renderer)
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == DUAL_COUNT_PATH) {
            Log.d("received count: ", messageEvent.data[0].toInt().toString())
        }
    }
}