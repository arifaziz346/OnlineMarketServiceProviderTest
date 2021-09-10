package com.example.onlinemarketserviceprovider.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Constant
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import androidx.annotation.NonNull as NonNull1

class register : AppCompatActivity() {
     private var Gender:Int?=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


       binding.btnSignUp.setOnClickListener(View.OnClickListener {
           if (validation(binding)){
            registerShop(binding)
               Toast.makeText(this,"Inside Button SignUp",Toast.LENGTH_LONG).show()
           }
       })

      binding.radioFemale.setOnClickListener(View.OnClickListener {
          Gender =0
      })

    }

    //This function is use to get data from user and register in database using Volley
    private fun registerShop(binding: ActivityRegisterBinding) {
        val shopRegisterUrl =Constant.RegisterShop
        val queue = Volley.newRequestQueue(this)
            //Json Object to send data to database
        val joRegisterShop:JSONObject = JSONObject()
        joRegisterShop.put("name",binding.ShopNameSignUp.getText().toString())
        joRegisterShop.put("email",binding.EmailSignUp.getText().toString())
        joRegisterShop.put("password",binding.PasswordSignUp.getText().toString())
        joRegisterShop.put("number",binding.ShopPhoneSignUp.getText().toString())
        joRegisterShop.put("address",binding.AddressSignUp.getText().toString())
        joRegisterShop.put("city",binding.CitySignUp.getText().toString())
        joRegisterShop.put("ShopType",binding.ShopTypeSignUp.getText().toString())
        joRegisterShop.put("gender",Gender)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, shopRegisterUrl,joRegisterShop,
            Response.Listener { response ->
                Toast.makeText(this,"Success:"+response,Toast.LENGTH_LONG).show()

            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                Toast.makeText(this,"Error:"+error.printStackTrace(),Toast.LENGTH_LONG).show()
            }
        )

// Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest)
    }


    //Use to Validate during registration of shop
   private fun validation(binding: ActivityRegisterBinding):Boolean{

       if(binding.ShopNameSignUp.getText().toString()==""){
       binding.txtLayoutShopNameSignUp.error="Plz Enter Shop Name"
   return false
   }
       if(binding.EmailSignUp.getText().toString()==""){
           binding.txtLayoutEmailSignUp.error="Plz Enter Your Email"
           return false
       }

       if(binding.PasswordSignUp.getText().toString()==""){
           binding.txtLayoutPasswordSignUp.error="Plz Enter Your Password"
           return false
       }
       if(binding.ShopPhoneSignUp.getText().toString()==""){
           binding.txtLayoutShopPhoneSignUp.error="Plz Enter Your Phone Number"
           return false
       }
       if(binding.CitySignUp.getText().toString()==""){
           binding.txtLayoutCitySignUp.error="Plz Enter Your City Name"
           return false
       }
       if(binding.AddressSignUp.getText().toString()==""){
           binding.txtLayoutAddressSignUp.error="Plz Enter Your Business Address"
           return false
       }
       if(binding.ShopTypeSignUp.getText().toString()==""){
           binding.txtLayoutShopTypeSignUp.error="Plz Enter Your Business Type"
           return false
       }
       return true
   }
}