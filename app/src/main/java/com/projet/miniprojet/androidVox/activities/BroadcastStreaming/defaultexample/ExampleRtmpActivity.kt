/*
 * Copyright (C) 2021 pedroSG94.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.projet.miniprojet.androidVox.activities.BroadcastStreaming.defaultexample

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.pedro.encoder.input.video.CameraOpenException
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.utils.PathUtils
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import kotlinx.android.synthetic.main.activity_example.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * More documentation see:
 * [com.pedro.rtplibrary.base.Camera1Base]
 * [com.pedro.rtplibrary.rtmp.RtmpCamera1]
 */
class ExampleRtmpActivity : AppCompatActivity(), ConnectCheckerRtmp, View.OnClickListener,
    SurfaceHolder.Callback {
    private val PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var rtmpCamera1: RtmpCamera1? = null
    private var button: Button? = null
    private val bRecord: Button? = null
    private var etUrl: EditText? = null
    private var currentDateAndTime = ""
    private var folder: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_example)
        folder = PathUtils.getRecordPath(this)
        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        button = findViewById(R.id.b_start_stop)
        b_start_stop.setOnClickListener(this)
        val switchCamera = findViewById<Button>(R.id.switch_camera)
        switchCamera.setOnClickListener(this)
        etUrl = findViewById(R.id.et_rtp_url)
        et_rtp_url.setHint(R.string.hint_rtmp)
        rtmpCamera1 = RtmpCamera1(surfaceView, this)
        rtmpCamera1!!.setReTries(10)
        surfaceView.holder.addCallback(this)
        et_rtp_url.setText("rtmp://${getLocalIpAddress()}/live/live")
        quitBtnStreaming.setOnClickListener {
            goBackHome()
        }

    }

    private fun goBackHome() {
        startActivity(Intent(this,HomePage::class.java))
        finish()
    }

    override fun onConnectionStartedRtmp(rtmpUrl: String) {}
    override fun onConnectionSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(
                this@ExampleRtmpActivity,
                "Connection success",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onConnectionFailedRtmp(reason: String) {
        runOnUiThread {
            if (rtmpCamera1!!.reTry(5000, reason)) {
                Toast.makeText(this@ExampleRtmpActivity, "Retry", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this@ExampleRtmpActivity,
                    "Connection failed. $reason",
                    Toast.LENGTH_SHORT
                )
                    .show()
                rtmpCamera1!!.stopStream()
                button!!.setText(R.string.start_button)
            }
        }
    }

    override fun onNewBitrateRtmp(bitrate: Long) {}
    override fun onDisconnectRtmp() {
        runOnUiThread {
            Toast.makeText(this@ExampleRtmpActivity, "Disconnected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAuthErrorRtmp() {
        runOnUiThread {
            Toast.makeText(this@ExampleRtmpActivity, "Auth error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAuthSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this@ExampleRtmpActivity, "Auth success", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getLocalIpAddress(): String? {
        try {

            val wifiManager: WifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            return ipToString(wifiManager.connectionInfo.ipAddress)
        } catch (ex: Exception) {
            Log.e("IP Address", ex.toString())
        }

        return null
    }

    private fun ipToString(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)

    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.b_start_stop -> if (!rtmpCamera1!!.isStreaming) {
                if (rtmpCamera1!!.isRecording
                    || rtmpCamera1!!.prepareAudio() && rtmpCamera1!!.prepareVideo()
                ) {
                    button!!.setText(R.string.stop_button)
                    rtmpCamera1!!.startStream(etUrl!!.text.toString())
                } else {
                    Toast.makeText(
                        this, "Error preparing stream, This device cant do it",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                button!!.setText(R.string.start_button)
                rtmpCamera1!!.stopStream()
            }
            R.id.switch_camera -> try {
                rtmpCamera1!!.switchCamera()
            } catch (e: CameraOpenException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
            R.id.b_record -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (!rtmpCamera1!!.isRecording) {
                    try {
                        if (!folder!!.exists()) {
                            folder!!.mkdir()
                        }
                        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                        currentDateAndTime = sdf.format(Date())
                        if (!rtmpCamera1!!.isStreaming) {
                            if (rtmpCamera1!!.prepareAudio() && rtmpCamera1!!.prepareVideo()) {
                                rtmpCamera1!!.startRecord(
                                    folder!!.absolutePath + "/" + currentDateAndTime + ".mp4"
                                )
                                bRecord!!.setText(R.string.stop_record)
                                Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    this, "Error preparing stream, This device cant do it",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            rtmpCamera1!!.startRecord(
                                folder!!.absolutePath + "/" + currentDateAndTime + ".mp4"
                            )
                            bRecord!!.setText(R.string.stop_record)
                            Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: IOException) {
                        rtmpCamera1!!.stopRecord()
                        bRecord!!.setText(R.string.start_record)
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    rtmpCamera1!!.stopRecord()
                    bRecord!!.setText(R.string.start_record)
                    Toast.makeText(
                        this,
                        "file " + currentDateAndTime + ".mp4 saved in " + folder!!.absolutePath,
                        Toast.LENGTH_SHORT
                    ).show()
                    currentDateAndTime = ""
                }
            } else {
                Toast.makeText(
                    this, "You need min JELLY_BEAN_MR2(API 18) for do it...",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {}
    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        rtmpCamera1!!.startPreview()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && rtmpCamera1!!.isRecording) {
            rtmpCamera1!!.stopRecord()
            bRecord!!.setText(R.string.start_record)
            Toast.makeText(
                this,
                "file " + currentDateAndTime + ".mp4 saved in " + folder!!.absolutePath,
                Toast.LENGTH_SHORT
            ).show()
            currentDateAndTime = ""
        }
        if (rtmpCamera1!!.isStreaming) {
            rtmpCamera1!!.stopStream()
            button!!.text = resources.getString(R.string.start_button)
        }
        rtmpCamera1!!.stopPreview()
    }
    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}