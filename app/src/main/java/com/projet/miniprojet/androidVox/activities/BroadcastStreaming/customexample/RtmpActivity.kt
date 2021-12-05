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
package com.projet.miniprojet.androidVox.activities.BroadcastStreaming.customexample

import androidx.appcompat.app.AppCompatActivity
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import android.view.View.OnTouchListener
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.os.Bundle
import com.projet.miniprojet.androidVox.R
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.pedro.encoder.input.video.CameraOpenException
import com.pedro.encoder.input.video.CameraHelper
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.utils.PathUtils
import kotlinx.android.synthetic.main.activity_custom.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RtmpActivity : AppCompatActivity(), View.OnClickListener, ConnectCheckerRtmp,
    SurfaceHolder.Callback, OnTouchListener {
    private val orientations = arrayOf(0, 90, 180, 270)
    private var rtmpCamera1: RtmpCamera1? = null
    private var bStartStop: Button? = null
    private var bRecord: Button? = null
    private var etUrl: EditText? = null
    private var currentDateAndTime = ""
    private var folder: File? = null

    //options menu
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var rgChannel: RadioGroup? = null
    private var spResolution: Spinner? = null
    private var cbEchoCanceler: CheckBox? = null
    private var cbNoiseSuppressor: CheckBox? = null
    private var etVideoBitrate: EditText? = null
    private var etFps: EditText? = null
    private var etAudioBitrate: EditText? = null
    private var etSampleRate: EditText? = null
    private var lastVideoBitrate: String? = null
    private var tvBitrate: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_custom)
        folder = PathUtils.getRecordPath(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        surfaceView.holder.addCallback(this)
        surfaceView.setOnTouchListener(this)
        rtmpCamera1 = RtmpCamera1(surfaceView, this)
        prepareOptionsMenuViews()
        tvBitrate = findViewById(R.id.tv_bitrate)
        etUrl = findViewById(R.id.et_rtp_url)
        et_rtp_url.setHint(R.string.hint_rtmp)
        bStartStop = findViewById(R.id.b_start_stop)
        b_start_stop.setOnClickListener(this)
        bRecord = findViewById(R.id.b_record)
        b_record.setOnClickListener(this)
        val switchCamera = findViewById<Button>(R.id.switch_camera)
        switchCamera.setOnClickListener(this)
    }

    private fun prepareOptionsMenuViews() {
        drawerLayout = findViewById(R.id.activity_custom)
        navigationView = findViewById(R.id.nv_rtp)
        nv_rtp.inflateMenu(R.menu.options_rtmp)
        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayout, R.string.rtmp_streamer,
            R.string.rtmp_streamer
        ) {
            override fun onDrawerOpened(drawerView: View) {
                actionBarDrawerToggle!!.syncState()
                lastVideoBitrate = etVideoBitrate!!.text.toString()
            }

            override fun onDrawerClosed(view: View) {
                actionBarDrawerToggle!!.syncState()
                if (lastVideoBitrate != null && lastVideoBitrate != etVideoBitrate!!.text.toString() && rtmpCamera1!!.isStreaming) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        val bitrate = etVideoBitrate!!.text.toString().toInt() * 1024
                        rtmpCamera1!!.setVideoBitrateOnFly(bitrate)
                        Toast.makeText(
                            this@RtmpActivity,
                            "New bitrate: $bitrate",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@RtmpActivity, "Bitrate on fly ignored, Required min API 19",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        activity_custom.addDrawerListener(actionBarDrawerToggle as ActionBarDrawerToggle)
        //checkboxs
        cbEchoCanceler =
            nv_rtp.menu.findItem(R.id.cb_echo_canceler).actionView as CheckBox
        cbNoiseSuppressor =
            nv_rtp.menu.findItem(R.id.cb_noise_suppressor).actionView as CheckBox
        //radiobuttons
        val rbTcp = nv_rtp.menu.findItem(R.id.rb_tcp).actionView as RadioButton
        rgChannel = nv_rtp.menu.findItem(R.id.channel).actionView as RadioGroup
        rbTcp.isChecked = true
        //spinners
        spResolution = nv_rtp.menu.findItem(R.id.sp_resolution).actionView as Spinner
        val orientationAdapter =
            ArrayAdapter<Int>(this, R.layout.support_simple_spinner_dropdown_item)
        orientationAdapter.addAll(*orientations)
        val resolutionAdapter =
            ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item)
        val list: MutableList<String> = ArrayList()
        for (size in rtmpCamera1!!.resolutionsBack) {
            list.add(size.width.toString() + "X" + size.height)
        }
        resolutionAdapter.addAll(list)
        spResolution!!.adapter = resolutionAdapter
        //edittexts
        etVideoBitrate =
            nv_rtp.menu.findItem(R.id.et_video_bitrate).actionView as EditText
        etFps = nv_rtp.menu.findItem(R.id.et_fps).actionView as EditText
        etAudioBitrate =
            nv_rtp.menu.findItem(R.id.et_audio_bitrate).actionView as EditText
        etSampleRate = nv_rtp.menu.findItem(R.id.et_samplerate).actionView as EditText
        etVideoBitrate!!.setText("2500")
        etFps!!.setText("30")
        etAudioBitrate!!.setText("128")
        etSampleRate!!.setText("44100")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle!!.syncState()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (!drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout!!.openDrawer(GravityCompat.START)
                } else {
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                true
            }
            R.id.microphone -> {
                if (!rtmpCamera1!!.isAudioMuted) {
                    item.icon = resources.getDrawable(R.drawable.icon_microphone_off)
                    rtmpCamera1!!.disableAudio()
                } else {
                    item.icon = resources.getDrawable(R.drawable.icon_microphone)
                    rtmpCamera1!!.enableAudio()
                }
                true
            }
            else -> false
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.b_start_stop -> {
                Log.d("TAG_R", "b_start_stop: ")
                if (!rtmpCamera1!!.isStreaming) {
                    bStartStop!!.text = resources.getString(R.string.stop_button)
                    if (rtmpCamera1!!.isRecording || prepareEncoders()) {
                        rtmpCamera1!!.startStream(etUrl!!.text.toString())
                    } else {
                        //If you see this all time when you start stream,
                        //it is because your encoder device dont support the configuration
                        //in video encoder maybe color format.
                        //If you have more encoder go to VideoEncoder or AudioEncoder class,
                        //change encoder and try
                        Toast.makeText(
                            this, "Error preparing stream, This device cant do it",
                            Toast.LENGTH_SHORT
                        ).show()
                        bStartStop!!.text = resources.getString(R.string.start_button)
                    }
                } else {
                    bStartStop!!.text = resources.getString(R.string.start_button)
                    rtmpCamera1!!.stopStream()
                }
            }
            R.id.b_record -> {
                Log.d("TAG_R", "b_start_stop: ")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (!rtmpCamera1!!.isRecording) {
                        try {
                            if (!folder!!.exists()) {
                                folder!!.mkdir()
                            }
                            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                            currentDateAndTime = sdf.format(Date())
                            if (!rtmpCamera1!!.isStreaming) {
                                if (prepareEncoders()) {
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
            }
            R.id.switch_camera -> try {
                rtmpCamera1!!.switchCamera()
            } catch (e: CameraOpenException) {
                Toast.makeText(this@RtmpActivity, e.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    private fun prepareEncoders(): Boolean {
        val resolution = rtmpCamera1!!.resolutionsBack[spResolution!!.selectedItemPosition]
        val width = resolution.width
        val height = resolution.height
        return rtmpCamera1!!.prepareVideo(
            width, height, etFps!!.text.toString().toInt(),
            etVideoBitrate!!.text.toString().toInt() * 1024,
            CameraHelper.getCameraOrientation(this)
        ) && rtmpCamera1!!.prepareAudio(
            etAudioBitrate!!.text.toString().toInt() * 1024, etSampleRate!!.text.toString().toInt(),
            rgChannel!!.checkedRadioButtonId == R.id.rb_stereo, cbEchoCanceler!!.isChecked,
            cbNoiseSuppressor!!.isChecked
        )
    }

    override fun onConnectionStartedRtmp(rtmpUrl: String) {}
    override fun onConnectionSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this@RtmpActivity, "Connection success", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionFailedRtmp(reason: String) {
        runOnUiThread {
            Toast.makeText(this@RtmpActivity, "Connection failed. $reason", Toast.LENGTH_SHORT)
                .show()
            rtmpCamera1!!.stopStream()
            bStartStop!!.text = resources.getString(R.string.start_button)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && rtmpCamera1!!.isRecording
            ) {
                rtmpCamera1!!.stopRecord()
                bRecord!!.setText(R.string.start_record)
                Toast.makeText(
                    this@RtmpActivity,
                    "file " + currentDateAndTime + ".mp4 saved in " + folder!!.absolutePath,
                    Toast.LENGTH_SHORT
                ).show()
                currentDateAndTime = ""
            }
        }
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
        runOnUiThread { tvBitrate!!.text = "$bitrate bps" }
    }

    override fun onDisconnectRtmp() {
        runOnUiThread {
            Toast.makeText(this@RtmpActivity, "Disconnected", Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && rtmpCamera1!!.isRecording
            ) {
                rtmpCamera1!!.stopRecord()
                bRecord!!.setText(R.string.start_record)
                Toast.makeText(
                    this@RtmpActivity,
                    "file " + currentDateAndTime + ".mp4 saved in " + folder!!.absolutePath,
                    Toast.LENGTH_SHORT
                ).show()
                currentDateAndTime = ""
            }
        }
    }

    override fun onAuthErrorRtmp() {
        runOnUiThread { Toast.makeText(this@RtmpActivity, "Auth error", Toast.LENGTH_SHORT).show() }
    }

    override fun onAuthSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this@RtmpActivity, "Auth success", Toast.LENGTH_SHORT).show()
        }
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        drawerLayout!!.openDrawer(GravityCompat.START)
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        rtmpCamera1!!.startPreview()
        // optionally:
        //rtmpCamera1.startPreview(CameraHelper.Facing.BACK);
        //or
        //rtmpCamera1.startPreview(CameraHelper.Facing.FRONT);
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
            bStartStop!!.text = resources.getString(R.string.start_button)
        }
        rtmpCamera1!!.stopPreview()
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val action = motionEvent.action
        if (motionEvent.pointerCount > 1) {
            if (action == MotionEvent.ACTION_MOVE) {
                rtmpCamera1!!.setZoom(motionEvent)
            }
        } else if (action == MotionEvent.ACTION_DOWN) {
            rtmpCamera1!!.tapToFocus(view, motionEvent)
        }
        return true
    }
}