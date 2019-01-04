package com.dimovsoft.photoframe

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_fullscreen.*
import org.jetbrains.anko.doAsync
import java.io.File

const val TIME_PER_SLIDE = 4

class FullscreenActivity : AppCompatActivity() {
    private val imagesList = ArrayList<String>()
    lateinit var currentFrame: ImageView
    private var currentPhotoIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        currentFrame = frame1

        supportActionBar?.hide()

        setupPermissions()

        root_view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 456)
        else permissionGranted()
    }

    private fun permissionGranted() {
        DataManager.getImages(this) {
            if (it.size==0) {
                Toast.makeText(this,"No images. Can't continue",Toast.LENGTH_LONG)
                finish()
            }
            imagesList.clear()
            imagesList.addAll(it)

            doAsync { runner() }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            456 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "No permission. Can't continue", Toast.LENGTH_LONG)
                    finish()
                } else
                    permissionGranted()
            }
        }
    }

    private fun runner(): Runnable {
        var timeLeft = 0
        while (true) {
            val startTime = System.currentTimeMillis()

            if (timeLeft==0) {
                runOnUiThread { updatePhoto() }
                timeLeft = 3
            }

            timeLeft--

            runOnUiThread { watch.invalidate() }
            val endTime = System.currentTimeMillis()
            val leftTime = 1000-(endTime-startTime)
            if (leftTime>0)
                Thread.sleep(leftTime) // overall 1sec execution time
        }
    }

    private fun updatePhoto() {
        if (currentPhotoIndex>=imagesList.size) currentPhotoIndex=0

        val backFrame = if (currentFrame==frame1) frame2 else frame1
        backFrame.setImageURI(Uri.fromFile(File(imagesList[currentPhotoIndex])))

        backFrame.scaleX=1.0f
        backFrame.scaleY=1.0f

        backFrame.animate().alpha(1.0f).setDuration(500).withEndAction {
            backFrame.animate().scaleX(1.1f).scaleY(1.1f).setDuration(TIME_PER_SLIDE*1000L).start()
        } .start()
        currentFrame.animate().alpha(0.0f).setDuration(500).start()

        currentFrame = backFrame
        currentPhotoIndex++

    }

}
