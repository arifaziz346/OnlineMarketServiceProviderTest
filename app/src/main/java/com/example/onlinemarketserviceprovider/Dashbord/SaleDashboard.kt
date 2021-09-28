package com.example.onlinemarketserviceprovider.Dashbord

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.databinding.ActivitySaleDashboardBinding
import org.json.JSONException
import org.json.JSONObject

class SaleDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySaleDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDashboardDetail(binding)

        binding.btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })


    }

    //GetDashboardDetail from the server----------------------------->
    private fun getDashboardDetail(binding:ActivitySaleDashboardBinding) {
        val  sharedPreferences: SharedPreferences = getSharedPreferences("ShopDetail",AppCompatActivity.MODE_PRIVATE)
        val id =sharedPreferences.getString("ShopID",null)
//        Toast.makeText(requireContext(),"ID:" +id,Toast.LENGTH_SHORT).show()
        val myReq: StringRequest = object : StringRequest(
            Method.GET,
            UrlConstant.DashboardDetail+id,
            Response.Listener {
                try {
                    var jODashboardDetail = JSONObject(it)
                    if(jODashboardDetail.getBoolean("success")){
                        var DashBoardDetail =jODashboardDetail.getJSONObject("DashboardDetail")
//                        binding.tvTotalSale.text ="Total Sale: "+ DashBoardDetail.getString("TotalSale")
//                        binding.tvOrder.text = DashBoardDetail.getString("TotalOrder")
//                        binding.tvAbandonedOrder.text =DashBoardDetail.getString("Abounded")
//                        binding.tvProductSold.text =DashBoardDetail.getString("TotalProductSold")
//                        binding.tvProfit.text =DashBoardDetail.getString("TotalProfit")
                    }
                }catch (e: JSONException){
                    Toast.makeText(this,"error:"+e.printStackTrace(), Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this,"VolleyFailure:"+it.printStackTrace(), Toast.LENGTH_SHORT).show()
            }
        ) {
            //            @Throws(AuthFailureError::class)
//            override fun getParams(): Map<String, String>? {
//                val  sharedPreferences:SharedPreferences = requireContext().getSharedPreferences("ShopDetail",AppCompatActivity.MODE_PRIVATE)
//        val id =sharedPreferences.getString("ShopID",null)
//        val token = sharedPreferences.getString("Token",null)
//                val params: MutableMap<String, String> = HashMap()
//                params["id"] = 201.toString()
//
//                return params
//            }
            val token = sharedPreferences.getString("Token",null)
            //Get Header is used to provide token to the server
            override fun getHeaders(): MutableMap<String, String> {
                val tokenGH: MutableMap<String, String> = HashMap()
                tokenGH["Authorization"] = "Bearer "+token
                return tokenGH
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(myReq)
    }

}