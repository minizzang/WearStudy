package com.example.wear_study

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.wearable.Wearable
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findWearableNode(this)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Button(onClick = {
                        Log.d("PhoneApp", connectedNodeID.toString())
                        sendMessageToWearable(this, "/count", "hello".toByteArray())
                    }) {
                        Text("Send")
                    }
                }
            }
        }
    }
}

var connectedNodeID: String? = null

fun findWearableNode(context: Context) {
    Wearable.getNodeClient(context).connectedNodes
        .addOnSuccessListener { nodes ->
            connectedNodeID = nodes.find { it.isNearby }?.id
        }
        .addOnFailureListener { e ->
            Log.e("PhoneApp", "Failed to retrieve nodes", e)
        }
}

fun sendMessageToWearable(context: Context, path: String, data: ByteArray) {
    connectedNodeID?.let { nodeId ->
        Wearable.getMessageClient(context).sendMessage(nodeId, path, data)
            .addOnSuccessListener {
                Log.d("PhoneApp", "Message sent successfully")
            }
            .addOnFailureListener { e ->
                Log.e("PhoneApp", "Failed to send message", e)
            }
    } ?: run {
        Log.e("PhoneApp", "No connected node found")
    }
}