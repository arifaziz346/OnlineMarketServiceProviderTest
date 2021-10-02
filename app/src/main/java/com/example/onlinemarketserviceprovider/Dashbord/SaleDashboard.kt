package com.example.onlinemarketserviceprovider.Dashbord

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import android.widget.DatePicker
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Dashbord.Adapter.MonthSaleProfitLV
import com.example.onlinemarketserviceprovider.Dashbord.Model.SaleProfitDetail
import com.example.onlinemarketserviceprovider.Helper.LoadingDialog
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.databinding.ActivitySaleDashboardBinding
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SaleDashboard : AppCompatActivity() {
    private var SaleProfitDetail_LV: ListView? =null
    private var adapterSaleProfitLV: MonthSaleProfitLV?=null
    private var MonthSaleProfit = ArrayList<SaleProfitDetail>()
    private val loadingDialog =LoadingDialog()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivitySaleDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.MonthlyWiseSaleReportTV.text ="Month Wise Sale Report of "+Calendar.getInstance().get(Calendar.YEAR)
        getDashboardDetail(binding)
        SaleProfitDetail_LV=binding.MonthWiseSaleDetailLV

        monthWiseSaleProfitDetail()

        binding.btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })




    }

    private fun monthWiseSaleProfitDetail() {
//        loadingDialog.startLoad(this)
         var sharedPreferance=getSharedPreferences("ShopDetail", MODE_PRIVATE);
         val shop_id =sharedPreferance.getString("ShopID",null)
                 val queue = Volley.newRequestQueue(this)
                 val joGetProductRequest: StringRequest = object: StringRequest(Method.GET, UrlConstant.GetMonthWiseSaleDetail+shop_id,
                     Response.Listener {


                         try {
                             var jsonObject = JSONObject(it)
                             var items = jsonObject.getJSONArray("MonthlySaleProfitDetail")
                             if(jsonObject.getBoolean("success")){

                                 for(i in 0 until items.length()){
                                     var SaleProfitl=items.getJSONObject(i)
                                     MonthSaleProfit!!.add(SaleProfitDetail(
                                         SaleProfitl.optString("Month"),
                                         SaleProfitl.optString("Sale").toInt(),
                                         SaleProfitl.optString("Profit").toInt()))
                                 }
                                 adapterSaleProfitLV = MonthSaleProfitLV(this,MonthSaleProfit)
                                 SaleProfitDetail_LV!!.adapter=adapterSaleProfitLV
                                 loadingDialog.isDismiss()
                                 }
                             loadingDialog.isDismiss()

                         }catch(e:JSONException){
                         Toast.makeText(this,"JsonException:"+e,Toast.LENGTH_LONG).show()
                             loadingDialog.isDismiss()
                         }

                     }, Response.ErrorListener {
                         loadingDialog.isDismiss()
                         Toast.makeText(this,"Vollery Error:"+it.printStackTrace(), Toast.LENGTH_LONG).show()

                     }){


                     override fun getHeaders(): MutableMap<String, String> {
                         val token =sharedPreferance.getString("Token",null)
                         val hmtoken:HashMap<String,String> = HashMap()
                         hmtoken["Authorization"]="Bearer "+token
                         return hmtoken
                     }

                 }


                 queue.add(joGetProductRequest)


    }


    //GetDashboardDetail from the server----------------------------->
    private fun getDashboardDetail(binding:ActivitySaleDashboardBinding) {
        loadingDialog.startLoad(this)
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
                        binding.tvTotalSale.text ="Total Sale: Rs."+DashBoardDetail.getString("TotalSale")
                        binding.tvTotalProfit.text ="Total Profit: Rs."+DashBoardDetail.getString("TotalProfit")
                        binding.tvTotalOrder.text ="Total Orders: "+DashBoardDetail.getString("TotalOrder")
                        binding.tvTotalOrderAbandoned.text ="Total  Abandoned: "+DashBoardDetail.getString("TotalAbandoned")
                        binding.tvTotalOrderPending.text ="Total Pending: "+DashBoardDetail.getString("TotalOrderPending")
                        loadingDialog.isDismiss()
                    }
                }catch (e: JSONException){
                    Toast.makeText(this,"error:"+e, Toast.LENGTH_SHORT).show()
                    loadingDialog.isDismiss()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this,"VolleyFailure:"+it.printStackTrace(), Toast.LENGTH_SHORT).show()
                loadingDialog.isDismiss()
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

    override fun onBackPressed() {
        finish()
        val intent =Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}
