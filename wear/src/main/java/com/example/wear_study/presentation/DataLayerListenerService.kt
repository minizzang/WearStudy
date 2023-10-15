package com.example.wear_study.presentation

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class DataLayerListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        if (messageEvent.path == DUAL_COUNT_PATH) {
            Log.d("received count: ", messageEvent.data.toString())
        }
    }

    companion object {
        private const val DUAL_COUNT_PATH = "/dual_count"
    }
}