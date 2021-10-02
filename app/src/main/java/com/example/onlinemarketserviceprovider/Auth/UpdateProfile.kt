package com.example.onlinemarketserviceprovider.Auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Helper.LoadingDialog
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.databinding.ActivityUpdateProfileBinding
import org.json.JSONException
import org.json.JSONObject

class UpdateProfile : AppCompatActivity() {
    private  var sharedPreferance:SharedPreferences?=null
    private var gender:Int?=null
    private var LoadingDialog: LoadingDialog = LoadingDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackUpdateProfile.setOnClickListener(View.OnClickListener {
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()

        })

        binding.btnUpdate.setOnClickListener(View.OnClickListener {
         if(validation(binding)){
             updateProfile(binding)
         }
        })

        binding.radioMale.setOnClickListener(View.OnClickListener {
            Toast.makeText(this,"Male",Toast.LENGTH_LONG).show()
            gender=1
        })
        binding.radioFemale.setOnClickListener(View.OnClickListener {
            gender=0
            Toast.makeText(this,"Female",Toast.LENGTH_LONG).show()
        })


        //todo --------------it is used to get Detail of Shop
        getShopDetail(binding)

    }
    //todo ---------------Update Profile
    private fun updateProfile(binding:  ActivityUpdateProfileBinding) {
        LoadingDialog.startLoad(this)
      val shop_id =sharedPreferance!!.getString("ShopID",null)
              val queue = Volley.newRequestQueue(this)
              val joGetProductRequest: StringRequest = object: StringRequest(Method.POST, UrlConstant.UpdateProfile,
                  Response.Listener {
                      try {
                          var jsonObject = JSONObject(it)
                          if(jsonObject.getBoolean("success")){
                               Toast.makeText(this,"Update Successfully",Toast.LENGTH_LONG).show()
                              LoadingDialog.isDismiss()
                              }else{
                                  Toast.makeText(this,jsonObject.getJSONObject("message").getJSONArray("ShopPhoneNumber")[0].toString(),Toast.LENGTH_LONG).show()
                              LoadingDialog.isDismiss()
                              }


                      }catch(e:JSONException){
                          LoadingDialog.isDismiss()
                      Toast.makeText(this,"JsonException"+e,Toast.LENGTH_LONG).show()
                      }

                  }, Response.ErrorListener {
                      Toast.makeText(this,"Vollery Error:"+it.printStackTrace(), Toast.LENGTH_LONG).show()
                      LoadingDialog.isDismiss()

                  }){
                  override fun getParams(): MutableMap<String, String> {
                      var params:HashMap<String,String> = HashMap()
                      params["shop_id"]=shop_id.toString()
                      params["ShopName"]=binding.etShopName.getText().toString().capitalize()
                      params["ShopPhoneNumber"]=binding.etShopPhone.getText().toString()
                      params["ShopAddress"]=binding.etAddress.getText().toString().capitalize()
                      params["ShopType"]=binding.etShopType.getText().toString().capitalize()
                      params["ShopCity"]=binding.etCity.getText().toString().capitalize()
                      params["Gender"]=gender.toString()
                      params["password"]=binding.etPassword.getText().toString()
                      return params
                  }

                  override fun getHeaders(): MutableMap<String, String> {
                      val token =sharedPreferance!!.getString("Token",null)
                      val hmtoken:HashMap<String,String> = HashMap()
                      hmtoken["Authorization"]="Bearer "+token
                      return hmtoken
                  }

              }


              queue.add(joGetProductRequest)

    }

    //todo ---------------Get ShopDetail
    private fun getShopDetail(binding: ActivityUpdateProfileBinding) {
        LoadingDialog.startLoad(this)

        sharedPreferance = getSharedPreferences("ShopDetail", AppCompatActivity.MODE_PRIVATE)
        val shop_id = sharedPreferance!!.getString("ShopID", null)
        val queue = Volley.newRequestQueue(this)
        val joGetProductRequest: StringRequest = object : StringRequest(Method.GET, UrlConstant.GetShopDetail+shop_id,
            Response.Listener {
                try {
                    var jsonObject = JSONObject(it)
                    if (jsonObject.getBoolean("success")) {
                            var shopDetail = jsonObject.getJSONObject("ShopDetail")
                        binding.etShopName.setText(shopDetail.getString("ShopName"))
                        binding.etShopPhone.setText(shopDetail.getString("ShopPhoneNumber"))
                        binding.etCity.setText(shopDetail.getString("City"))
                        binding.etAddress.setText(shopDetail.getString("ShopAddress"))
                        binding.etShopType.setText(shopDetail.getString("ShopType"))
                        if (shopDetail.getString("Gender").toInt()==1){
                            gender=1
                            binding.radioMale.setChecked(true)

                        }else{
                            gender=0
                            binding.radioFemale.setChecked(true)

                            LoadingDialog.isDismiss()
                        }

                        LoadingDialog.isDismiss()
                    }else{
                        Toast.makeText(this,"Error"+it,Toast.LENGTH_LONG).show()
                        LoadingDialog.isDismiss()
                    }


                }catch(e:Exception){
                Toast.makeText(this,"JsonExcpection:"+e.printStackTrace(),Toast.LENGTH_LONG).show()
                    LoadingDialog.isDismiss()
                }

            }, Response.ErrorListener {
                Toast.makeText(this, "Vollery Error:" + it.printStackTrace(), Toast.LENGTH_LONG)
                    .show()
                LoadingDialog.isDismiss()
            }) {


            override fun getHeaders(): MutableMap<String, String> {
                val token = sharedPreferance!!.getString("Token", null)
                val hmtoken: HashMap<String, String> = HashMap()
                hmtoken["Authorization"] = "Bearer " + token
                return hmtoken
            }

        }


        queue.add(joGetProductRequest)

    }
}
//Use to Validate during registration of shop
private fun validation(binding: ActivityUpdateProfileBinding):Boolean{

    if(binding.etShopName.getText().toString()==""){
        binding.txtLayoutShopNameSignUp.error="Plz Enter Shop Name"
        return false
    }

    else if(binding.etShopPhone.getText().toString()==""){
        binding.txtLayoutShopPhoneSignUp.error="Plz Enter Your Phone Number"
        return false
    }


    else if(binding.etShopPhone.getText().toString().count()>11){
        binding.txtLayoutShopPhoneSignUp.error="Phone Number can't be greater then 11"
        return false
    }


    else if(binding.etCity.getText().toString()==""){
        binding.txtLayoutCitySignUp.error="Plz Enter Your City Name"
        return false
    }
    else if(binding.etAddress.getText().toString()==""){
        binding.txtLayoutAddressSignUp.error="Plz Enter Your Business Address"
        return false
    }
    else if(binding.etShopType.getText().toString()==""){
        binding.txtLayoutShopTypeSignUp.error="Plz Enter Your Business Type"
        return false
    }



    return true
}
