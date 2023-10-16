package com.example.wear_study

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.wearable.Wearable
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {
    private val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findWearableNode(this)
        setContentView(R.layout.activity_main)
        setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var count by remember { mutableStateOf(0) }
                        Text("Count: $count")
                        Row() {
                            Button(
                                onClick = {
                                    count = 0
                                    sendMessageToWearable(
                                        context,
                                        "/count",
                                        "0".toByteArray()
                                    )
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Initialize")
                            }
                            Button(
                                onClick = {
                                    count++
                                    sendMessageToWearable(
                                        context,
                                        "/count",
                                        count.toString().toByteArray()
                                    )
                                },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Add count")
                            }
                        }
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