package com.example.onlinemarketserviceprovider.Auth


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Helper.LoadingDialog
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.databinding.ActivityRegisterBinding
import org.json.JSONException
import org.json.JSONObject

class register : AppCompatActivity() {
     private var Gender:Int?=1
    private  var loadingDialog: LoadingDialog = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //use to move Login
        binding.txtSignIn.setOnClickListener(View.OnClickListener {
            val intent =Intent(this,login::class.java)
            startActivity(intent)
        })

            //Btn use to SignUP
       binding.btnSignUp.setOnClickListener(View.OnClickListener {
           if (validation(binding)){
            registerShop(binding)

           }
       })

        //Btn to select male or female
      binding.radioFemale.setOnClickListener(View.OnClickListener {
          Gender =0
      })

    }

    //This function is use to get data from user and register in database using Volley
    private fun registerShop(binding: ActivityRegisterBinding) {
        loadingDialog.startLoad(this)
        val shopRegisterUrl =UrlConstant.RegisterShop
        val queue = Volley.newRequestQueue(this)

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
                try {



                    if(response.getBoolean("success")){
                        loadingDialog.isDismiss()
//                        Toast.makeText(this,"Registerd successfullry:",Toast.LENGTH_LONG).show()
//                        Toast.makeText(this,"token="+response.getString("token"),Toast.LENGTH_LONG).show()
                        val JSONObject = response.getJSONObject("Shops")
                        var  sharedPreferences = getSharedPreferences("ShopDetail", MODE_PRIVATE)
                        var editor =sharedPreferences!!.edit()
                        editor.putString("Token",response.getString("token"))
                        editor.putString("ShopID",JSONObject.get("id").toString())
                        editor.putString("ShopName",JSONObject.get("ShopName").toString())
                        editor.putString("ShopPhoneNumber",JSONObject.get("ShopPhoneNumber").toString())
                        editor.putString("ShopType",JSONObject.get("ShopType").toString())
                        editor.putString("City",JSONObject.get("City").toString())
                        editor.putString("Province",JSONObject.get("Province").toString())
                        editor.putString("ShopType",JSONObject.get("ShopType").toString())
                        editor.putBoolean("register",true)
                        editor.commit()

                        val  intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                        }catch(js:JSONException) {
                        loadingDialog.isDismiss()
                        var error = response.getJSONObject("fail").getJSONArray("errorInfo")
                        val phoneNumber: String =
                            error[2].toString().substringAfter("'").substringBefore("'")
                        Toast.makeText(this, "error:" + phoneNumber, Toast.LENGTH_LONG).show()



                        if ("@" !in phoneNumber) {
                            binding.txtLayoutShopPhoneSignUp.error =
                                "This number is already registered!"
                            loadingDialog.isDismiss()
                        } else {
                            loadingDialog.isDismiss()
                            binding.txtLayoutEmailSignUp.error = "This email is already registered!"

                        }



                }


            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                Toast.makeText(this,"Error:"+error.printStackTrace(),Toast.LENGTH_LONG).show()
                loadingDialog.isDismiss()
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
      else if(binding.EmailSignUp.getText().toString()==""){
           binding.txtLayoutEmailSignUp.error="Plz Enter Your Email"
           return false
       }
       else if("@" !in binding.EmailSignUp.getText().toString()){
           binding.txtLayoutEmailSignUp.error="Invalid Email!"
           return false
       }
       else if("." !in binding.EmailSignUp.getText().toString().substringAfter('@')){
           binding.txtLayoutEmailSignUp.error=".com is missing!"
           return false}

        else if(binding.PasswordSignUp.getText().toString()==""){
           binding.txtLayoutPasswordSignUp.error="Plz Enter Your Password"
           return false
       }
        else if(binding.ShopPhoneSignUp.getText().toString()==""){
           binding.txtLayoutShopPhoneSignUp.error="Plz Enter Your Phone Number"
           return false
       }

//       else if(binding.ShopPhoneSignUp.getText().toString().toIntOrNull()==null){
//           binding.txtLayoutShopPhoneSignUp.error="Phone should be a number!"
//           return false
//       }
        else if(binding.ShopPhoneSignUp.getText().toString().count()>11){
            binding.txtLayoutShopPhoneSignUp.error="Phone Number can't be greater then 11"
            return false
        }


        else if(binding.CitySignUp.getText().toString()==""){
           binding.txtLayoutCitySignUp.error="Plz Enter Your City Name"
           return false
       }
        else if(binding.AddressSignUp.getText().toString()==""){
           binding.txtLayoutAddressSignUp.error="Plz Enter Your Business Address"
           return false
       }
        else if(binding.ShopTypeSignUp.getText().toString()==""){
           binding.txtLayoutShopTypeSignUp.error="Plz Enter Your Business Type"
           return false
       }



       return true
   }
}