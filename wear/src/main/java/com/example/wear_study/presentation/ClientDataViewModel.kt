package com.example.wear_study.presentation

import android.app.Application
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent

class ClientDataViewModel(application: Application) : MessageClient.OnMessageReceivedListener {
    override fun onMessageReceived(messageEvent: MessageEvent) {

    }
}