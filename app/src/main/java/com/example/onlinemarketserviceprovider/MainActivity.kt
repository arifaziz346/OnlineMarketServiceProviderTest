package com.example.onlinemarketserviceprovider

import android.content.Context
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
import com.bumptech.glide.Glide
import com.example.onlinemarketserviceprovider.Auth.UpdateProfile
import com.example.onlinemarketserviceprovider.Auth.login
import com.example.onlinemarketserviceprovider.Dashbord.Expense
import com.example.onlinemarketserviceprovider.Dashbord.SaleDashboard
import com.example.onlinemarketserviceprovider.MyOrder.TransferredOrderDetail
import com.example.onlinemarketserviceprovider.MyOrder.MyOrder
import com.example.onlinemarketserviceprovider.Products.MyProduct
import com.example.onlinemarketserviceprovider.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences=getSharedPreferences("ShopDetail", MODE_PRIVATE)

        //ShopName
        binding.TvShopName.setText(sharedPreferences.getString("ShopName",null))



        FirebaseMessaging.getInstance().token.addOnSuccessListener {

        }


        //Sale Dashboard
        binding.cvSaleDashboard.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,SaleDashboard::class.java)
                startActivity(intent)
            })

        binding.cvMyProduct.setOnClickListener(
            View.OnClickListener {
            val intent = Intent(this,MyProduct::class.java)
                startActivity(intent)
            })
        binding.cvOrder.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,MyOrder::class.java)
                startActivity(intent)

            })

        binding.AbandonedOrder.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,TransferredOrderDetail::class.java)
            startActivity(intent)
        })
        binding.cvUpdateProfile.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this,UpdateProfile::class.java)
                startActivity(intent)
                finish()
            })

        binding.cvAssets.setOnClickListener(
            View.OnClickListener {
//                val intent = Intent(this,UpdateProfile::class.java)
//                startActivity(intent)
//                finish()
            }
        )

        binding.cvExpense.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,Expense::class.java)
            startActivity(intent)
            finish()
        })

        binding.cvLogOut.setOnClickListener(
            View.OnClickListener {
//                FirebaseMessaging.getInstance().deleteToken()
                sharedPreferences.edit().clear()
                trimCache(this)
                this.cacheDir.deleteRecursively()
                getCacheDir().delete()
                val intent = Intent(this,login::class.java)
                startActivity(intent)
                finish()


            })

        //Check has order or not
        checkMyOrder(binding)

    }

    private fun checkMyOrder(binding: ActivityMainBinding) {
        val queue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("ShopDetail", AppCompatActivity.MODE_PRIVATE)
        val token = sharedPreferences!!.getString("Token", null)
        val shop_id = sharedPreferences!!.getString("ShopID", null)

        //Volley Request---------------------------------------------------->
        val myOrderRequest: StringRequest = object : StringRequest(
            Method.GET, UrlConstant.MyOrderProduct + shop_id, Response.Listener {
                try {

                    var jsonObject = JSONObject(it)
                    if (jsonObject.getBoolean("success")) {

                        val OrderDetail = jsonObject.getJSONArray("OrderDetail")


                        if(OrderDetail.length()>0){
                        Glide.with(this).load(R.drawable.new_order_bell).into(binding.OrderTV)
                        }


                    } else {
                        //todo ---------------->handle token expire

                    }

                } catch (e: Exception) {
                }


            }, Response.ErrorListener {

            }) {
            //Provide Token for authorization--------------------->
            override fun getHeaders(): MutableMap<String, String> {
                val tokenGH: MutableMap<String, String> = HashMap()
                tokenGH["Authorization"] = "Bearer " + token
                return tokenGH
            }
        }

        queue.add(myOrderRequest)
    }
    fun trimCache(context: Context) {
        val dir: File? = context.cacheDir
        if (dir != null && dir.isDirectory()) {
            val children: Array<File> = dir.listFiles()
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                var temp: File
                for (i in children.indices) {
                    temp = children[i]
                    temp.delete()
                }
            }
        }
    }
}