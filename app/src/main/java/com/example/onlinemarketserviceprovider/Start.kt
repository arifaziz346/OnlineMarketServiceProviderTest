package com.example.onlinemarketserviceprovider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Display
import android.widget.Toast
import com.example.onlinemarketserviceprovider.Auth.register
import com.example.onlinemarketserviceprovider.databinding.ActivityStartBinding
import render.animations.Bounce
import render.animations.*

class Start : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
         var sharedPreference = getSharedPreferences("ShopDetail", MODE_PRIVATE)
         var edit = sharedPreference.edit()
         var registerShop = sharedPreference.getBoolean("register", false)


        //Animination
        val zoomLogo =Render(this)
        zoomLogo.setAnimation(Zoom().In(binding.logo).setDuration(3000))
        zoomLogo.start()

        val textOMSP =Render(this)
        textOMSP.setAnimation(Zoom().In(binding.txtOMSP))
        textOMSP.start()

        if(!registerShop)
        {
            Handler(Looper.getMainLooper()).postDelayed({
                /* Create an Intent that will start the Menu-Activity. */
//                edit.putBoolean("status",true)
//                edit.commit()

                val mainIntent = Intent(this,register::class.java)
                startActivity(mainIntent)
                finish()
            }, 3000)
        }else{

          var intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }


}

