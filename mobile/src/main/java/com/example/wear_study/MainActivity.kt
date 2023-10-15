package com.example.wear_study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.util.concurrent.Future
import kotlin.concurrent.thread
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.log

private const val COUNT_KEY = "com.wearstudy.key.count"
private const val DUAL_CAPABILITY_NAME  = "dual_viewer_watch"
const val DUAL_COUNT_PATH = "/dual_count"

class MainActivity : AppCompatActivity(), OnClickListener {
    private var count = 0
    lateinit var txtCount: TextView

    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy {Wearable.getMessageClient(this)}
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnMinus = findViewById<Button>(R.id.btnMinus)
        txtCount = findViewById<TextView>(R.id.txtCounter)

        btnPlus.setOnClickListener(this)
        btnMinus.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnPlus -> count += 1
            R.id.btnMinus -> count -= 1
        }
        txtCount.setText(count.toString())

        sendDataToWearable()
    }

    private fun sendDataToWearable() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("this is new thread", "sdf")
            val nodes = Tasks.await(capabilityClient.getCapability(DUAL_CAPABILITY_NAME, CapabilityClient.FILTER_REACHABLE)).nodes
            val targetNodeId = nodes.first().id

            messageClient.sendMessage(targetNodeId, DUAL_COUNT_PATH, byteArrayOf(count.toByte()))
        }
//        lifecycleScope.launch {
//
//        }
//        val putDataReq = PutDataMapRequest.create("/count").run {
//            dataMap.putInt(COUNT_KEY, count)
//            asPutDataRequest()
//        }
//        val putDataTask = dataClient.putDataItem(putDataReq)
    }


}