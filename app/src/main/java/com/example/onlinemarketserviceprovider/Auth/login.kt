package com.example.onlinemarketserviceprovider.Auth

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.onlinemarketserviceprovider.Constant
import com.example.onlinemarketserviceprovider.Helper.Network
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class login : AppCompatActivity() {
    private   var txtEmail:TextInputEditText?=null
    private   var txtPassword:TextInputEditText?=null
    private   var emailLayout:TextInputLayout?=null
    private   var passwordLayout: TextInputLayout?=null
    private var btnSignIn:Button?=null
//    private var sharedPreferences:SharedPreferences?=null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var  binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        txtEmail = binding.txtEmailSignIn
        emailLayout = binding.txtLayoutEmailSignIn
        txtPassword = binding.txtPasswordSignIn
        passwordLayout = binding.txtLayoutPasswordSignIn
//        btnSignIn =findViewById<Button>(R.id.btnSignIn) as Button
        btnSignIn = binding.btnSignIn


        btnSignIn!!.setOnClickListener {
            if (validate()) {
                logIn(txtEmail!!.getText().toString(),txtPassword!!.getText().toString())
//                if(Network.isOnline(this)){
//                    logIn(txtEmail!!.getText().toString(),txtEmail!!.getText().toString())
//                }else{
//                    messageError()
//                }

            }
        }
}

//    fun signUp(view: View) {
//        var intent = Intent(this,register::class.java)
//        startActivity(intent)
//    }

    //Use to Validate Email and Password
    fun validate():Boolean{
        if(txtEmail!!.getText().toString()==""){
            emailLayout!!.error ="Plz Enter Your Email"
            return false
        }
        else if (txtPassword!!.getText().toString()==""){
            passwordLayout!!.error ="Plz Enter Your Password"
            return false
        }

    return true
    }

    private  fun logIn(email:String,password:String){
      var requestQuee = Volley.newRequestQueue(this)
      var  joLogIn:JSONObject = JSONObject()
        joLogIn.put("email",email)
        joLogIn.put("password",password)

      var UrlLogin = Constant.LoginShop


        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "http://192.168.43.193:8080/api/loginShop",joLogIn,
            Response.Listener {response->

               try {

                  if(response.getBoolean("success")){
                      val JSONObject = response.getJSONObject("Shops")
//                      Toast.makeText(this,"WellCome: "+response.get("Shops"),Toast.LENGTH_SHORT).show()

                      Toast.makeText(this,"ShopName: "+JSONObject.get("ShopName"),Toast.LENGTH_SHORT).show()
                      var  sharedPreferences = getSharedPreferences("ShopDetail", MODE_PRIVATE)
                      var editor =sharedPreferences!!.edit()
                      editor.putString("Token",response.get("token").toString())
                      editor.putString("ShopID",JSONObject.get("id").toString())
                      editor.putString("ShopName",JSONObject.get("ShopName").toString())
                      editor.putString("ShopPhoneNumber",JSONObject.get("id").toString())
                      editor.putString("ShopType",JSONObject.get("ShopType").toString())
                      editor.putString("City",JSONObject.get("City").toString())
                      editor.putString("Province",JSONObject.get("Province").toString())
                      editor.putString("ShopType",JSONObject.get("ShopType").toString())
                      editor.putBoolean("register",true)
                      editor.commit()

                      val  intent =Intent(this,MainActivity::class.java)
                      startActivity(intent)

                  }else{
                      Toast.makeText(this,"Sorry! your account has been blocked.",Toast.LENGTH_SHORT).show()
                  }

//                  var  sharedPreferences = getSharedPreferences("ShopDetail", MODE_PRIVATE)
//                   var editor =sharedPreferences!!.edit()
//                   editor

               }catch (e:JSONException ){
//                   Toast.makeText(this,"Error: "+e.printStackTrace(),Toast.LENGTH_SHORT).show()
                   Toast.makeText(this,"Invalid Login!"+e.toString(),Toast.LENGTH_SHORT).show()
               }

            },Response.ErrorListener { error ->
                // TODO: Handle error
                error.printStackTrace()
                Toast.makeText(this,error.printStackTrace().toString(),Toast.LENGTH_SHORT).show()
            })



        requestQuee.add(jsonObjectRequest)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    //Display Message of internet failure
    private fun messageError(){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("No Internet")
        //set message for alert dialog
        builder.setMessage("Network Error!")
        builder.setIcon(R.drawable.no_wifi)

        //performing cancel action
        builder.setNeutralButton("Retry"){dialogInterface , which ->
            if (validate()) {
                if(Network.isOnline(this)){
                    logIn(txtEmail!!.getText().toString(),txtEmail!!.getText().toString())
                }else{
                    messageError()
                }

            }

        }
//        //performing negative action
        builder.setNegativeButton("Close App"){dialogInterface, which ->
            finishAffinity()
            System.exit(0)
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }



}