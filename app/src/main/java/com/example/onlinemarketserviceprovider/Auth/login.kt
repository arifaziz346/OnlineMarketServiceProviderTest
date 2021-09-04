package com.example.onlinemarketserviceprovider.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class login : AppCompatActivity() {
    private   var txtEmail:TextInputEditText?=null
    private   var txtPassword:TextInputEditText?=null
    private   var emailLayout:TextInputLayout?=null
    private   var passwordLayout: TextInputLayout?=null
    private var btnSignIn:Button?=null


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
                var intent = Intent(this,register::class.java)
                startActivity(intent)
            }
        }




//        binding.btnSignIn.setOnClickListener(View.OnClickListener {
//            Toast.makeText(this,"Btn SignIn!",Toast.LENGTH_SHORT).show()
//        })

//        binding.btnSignIn.setOnClickListener(View.OnClickListener {
////            if (validate()){
////                Toast.makeText(this,"Fine!",Toast.LENGTH_SHORT).show()
////            }else{
////                Toast.makeText(this,"Sorry!",Toast.LENGTH_SHORT).show()
////            }
//            Toast.makeText(this,"Btn SignIn!",Toast.LENGTH_SHORT).show()
//        })





    }

    fun signUp(view: View) {
        var intent = Intent(this,register::class.java)
        startActivity(intent)
    }

    fun validate():Boolean{
        if(txtEmail!!.getText().toString()==""){
            emailLayout!!.error ="Plz Enter Your Email"
            return false
        }
        else if (txtPassword!!.getText().toString()==""){
            passwordLayout!!.error ="Plz Enter Your Password"
            return false
        }

    return true}


}