package com.example.onlinemarketserviceprovider.Auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.onlinemarketserviceprovider.Helper.FCMToken
import com.example.onlinemarketserviceprovider.Helper.LoadingDialog
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.Helper.Network
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class login : AppCompatActivity() {
    private   var txtEmail:TextInputEditText?=null
    private   var txtPassword:TextInputEditText?=null
    private   var emailLayout:TextInputLayout?=null
    private   var passwordLayout: TextInputLayout?=null
    private var btnSignIn:Button?=null
    private  var loadingDialog: LoadingDialog = LoadingDialog()
    private var fcm_token:String=""
    private var fcmToken=FCMToken()
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

//        Firebase.messaging.isAutoInitEnabled = true
        //this is used to generate Token to  FirebaseMessaging.getInstance().se
        try{
//            FirebaseMessaging.getInstance().deleteToken()
//            FirebaseInstallations.getInstance().delete()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener
        {
            if(!it.isSuccessful){

            }
            fcm_token= it.result.toString()
            Toast.makeText(this, "token:"+fcm_token, Toast.LENGTH_SHORT).show()
        })}
        catch (ex:Exception){
            Toast.makeText(this, "token:"+ex.message, Toast.LENGTH_SHORT).show()
        }


        binding.txtSignUp.setOnClickListener(View.OnClickListener {
            var intent = Intent(this,register::class.java)
            startActivity(intent)
        })


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



    //Use to Validate Email and Password
    fun validate():Boolean{
        if(txtEmail!!.getText().toString()==""){
            emailLayout!!.error ="Plz Enter Your Email"
            return false
        }
        else if("@" !in txtEmail!!.getText().toString()){
            emailLayout!!.error="Invalid Email!"
            return false
        }
        else if("." !in txtEmail!!.getText().toString().substringAfter('@')){
            emailLayout!!.error=".com is missing!"
            return false}

        else if (txtPassword!!.getText().toString()==""){
            passwordLayout!!.error ="Plz Enter Your Password"
            return false
        }


    return true
    }

    private  fun logIn(email:String,password:String){
        loadingDialog.startLoad(this)
      var requestQuee = Volley.newRequestQueue(this)
      var  joLogIn:JSONObject = JSONObject()
        joLogIn.put("email",email)
        joLogIn.put("password",password)

      var UrlLogin = UrlConstant.LoginShop


        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "http://192.168.43.193:8080/api/loginShop",joLogIn,
            {response->

               try {

                  if(response.getBoolean("success")){
                      loadingDialog.isDismiss()
                      val JSONObject = response.getJSONObject("Shops")
//                      Toast.makeText(this,"WellCome: "+response.get("Shops"),Toast.LENGTH_SHORT).show()

//                      Toast.makeText(this,"ShopName: "+JSONObject.get("ShopName"),Toast.LENGTH_SHORT).show()
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

                      //Gnerate FCM token and saved in database

                      fcmToken.generateFCMToken(response.get("token").toString(),JSONObject.get("id").toString(),
                      fcm_token,this)


                      val  intent =Intent(this,MainActivity::class.java)
                      startActivity(intent)

                  }else{
                      loadingDialog.isDismiss()
                      Toast.makeText(this,"Sorry! your account has been blocked.",Toast.LENGTH_SHORT).show()
                  }

//                  var  sharedPreferences = getSharedPreferences("ShopDetail", MODE_PRIVATE)
//                   var editor =sharedPreferences!!.edit()
//                   editor

               }catch (e:JSONException ){
//                   Toast.makeText(this,"Error: "+e.printStackTrace(),Toast.LENGTH_SHORT).show()
                   Toast.makeText(this,"Invalid Login!Plz check your Email and password",Toast.LENGTH_SHORT).show()
                   loadingDialog.isDismiss()
               }

            },
            { error ->
                // TODO: Handle error
                error.printStackTrace()
                Toast.makeText(this,error.printStackTrace().toString(),Toast.LENGTH_SHORT).show()
                loadingDialog.isDismiss()
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