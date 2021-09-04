package com.example.onlinemarketserviceprovider.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.databinding.ActivityRegisterBinding

class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_register)


    }
    fun signIn(view: View){
        var intent = Intent(this,login::class.java)
        startActivity(intent)
    }

    fun signUp(view:View){
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}