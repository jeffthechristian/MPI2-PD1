package com.example.cameraandaudio

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class AudioActivity : AppCompatActivity() {
    private val TAG = "AudioActivity"
    private lateinit var recorder: MediaRecorder
    private lateinit var audioFile: File
    private lateinit var audioList: ArrayList<String>
    private lateinit var audioListAdapter: ArrayAdapter<String>
    private lateinit var audioListView: ListView
    private var isRecording = false
    var path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        audioList = ArrayList()
        audioListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, audioList)
        audioListView = findViewById<ListView>(R.id.audioList)
        audioListView.adapter = audioListAdapter

        if (checkPermission()) {
            initRecorder()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                111
            )
        }

        val button = findViewById<Button>(R.id.startButton)
        button.setOnClickListener {
            toggleRecording()
        }
        audioListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(selectedItem)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }

    }

    private fun initRecorder() {
        recorder = MediaRecorder()
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        audioFile = File(getExternalFilesDir(null), "${System.currentTimeMillis()}.3gp")
        recorder.setOutputFile(audioFile.absolutePath)
    }

    private fun startRecording() {
        initRecorder()
        recorder.prepare()
        recorder.start()
        isRecording = true
        val butto = findViewById<Button>(R.id.startButton)
        butto.text = "Stop"
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
    }

    private fun stopRecording() {
        recorder.stop()
        recorder.release()
        audioList.add(audioFile.absolutePath)
        path = audioFile.parentFile.path
        audioListAdapter.notifyDataSetChanged()
        isRecording = false
        val butt = findViewById<Button>(R.id.startButton)
        butt.text = "Rec"
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }
    private fun toggleRecording() {
        if (isRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }
    private fun deleteDirectory() {
        val directory = File(path)
        directory.listFiles()
            .filterNot { it.isDirectory }
            .forEach { it.delete() }
    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_audio, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.image ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.delete ->{

                audioList.clear()
                audioListAdapter.notifyDataSetChanged()
                deleteDirectory()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

