package com.example.myapplication


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!permission()){
            requestPermission(this)
            return
        }
    }
    private fun permission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        return result== PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(context: Context){
        val activity = context as Activity
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(this, "A music player needs audio file permissions. Please allow access.", Toast.LENGTH_SHORT)
        }
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 123 )
    }

}