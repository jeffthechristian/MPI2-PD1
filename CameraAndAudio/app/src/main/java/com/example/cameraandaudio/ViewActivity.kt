package com.example.cameraandaudio


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import java.io.File


@Suppress("DEPRECATION")
class ViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity)

        displayImages()

        val butt = findViewById<Button>(R.id.back)

        butt.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }


    private fun displayImages(){

        try {
            val imagePaths = mutableListOf<String>()

            val dir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"")
            val files = dir.listFiles()
            for (file in files) {
                if (file.isFile) {
                    imagePaths.add(file.absolutePath)
                }
            }

            val viewPager = findViewById<ViewPager>(R.id.viewPager)
            val adapter = ImagePagerAdapter(this, imagePaths)
            viewPager.adapter = adapter
        }
        catch (e: Exception){
            Toast.makeText(this@ViewActivity, "error", Toast.LENGTH_SHORT).show()
        }


    }


}