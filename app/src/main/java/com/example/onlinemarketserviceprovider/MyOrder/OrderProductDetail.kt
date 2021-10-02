package com.example.onlinemarketserviceprovider.MyOrder

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.MyOrder.Adapter.OrderDetailAdapter
import com.example.onlinemarketserviceprovider.MyOrder.Modal.OrderItem
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.databinding.ActivityOrderProductDetailBinding
import org.json.JSONException
import org.json.JSONObject

class OrderProductDetail : AppCompatActivity() {
    var Item = arrayListOf<OrderItem>()
    var ItemListView: ListView? =null
    var adapter: BaseAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =ActivityOrderProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var sharedPreference = getSharedPreferences("ShopDetail", MODE_PRIVATE)
        ItemListView = findViewById(R.id.ListViewOrderDetail)

        binding.btnBackOrderDetail.setOnClickListener(View.OnClickListener {
            finish()
        })

        val Shop_Id: String? =intent.getStringExtra("shop_id").toString()
        val Order_Number: String? =intent.getStringExtra("order_number").toString()
        val status: String? =intent.getStringExtra("status").toString()
//        Toast.makeText(this,"Order_Number:"+ Order_Number,Toast.LENGTH_LONG).show()
        getOrderProductList(Shop_Id,Order_Number,sharedPreference,binding,status)

    }

    private fun getOrderProductList(
        Shop_Id: String?,
        Order_Number: String?,
        sharedPreference: SharedPreferences,
        binding: ActivityOrderProductDetailBinding,
        status: String?,) {
        var queue = Volley.newRequestQueue(this)
        var jrOrderProductDetail:StringRequest = object :StringRequest(Method.POST,UrlConstant.OderProductDetail,
            Response.Listener {

                try{
                    var jsonObject =JSONObject(it)
                    if (jsonObject.getBoolean("success")) {

                       var OrderDetail =jsonObject.getJSONArray("OrderItemDetail")
//                        Toast.makeText(this,"Length:"+ jsonObject.getJSONArray("OrderItemDetail"),Toast.LENGTH_LONG).show()
//                        Item.clear()

                        for (i in 0 until OrderDetail.length()){
                            var orderDetail =OrderDetail.getJSONObject(i)

                            Item.add(OrderItem(
                                orderDetail.optString("ProductName"),
                                orderDetail.optInt("Quantity"),
                                orderDetail.optInt("SellPrice"),
                                orderDetail.optInt("Discount"),
                                orderDetail.optString("Color"),
                                orderDetail.optString("Size"),
                                orderDetail.optInt("OrderNumber")
                            ))


                        }

                        adapter = OrderDetailAdapter(this,Item)

                        ItemListView!!.adapter = adapter
                        binding.tvTotalPrice.text ="Total Price:"+ jsonObject.get("TotalPrice").toString()
                    }else{

                        Toast.makeText(this,"error:"+jsonObject.getString("message"),Toast.LENGTH_LONG).show()
                    }

                }catch (e:JSONException){
                    Toast.makeText(this,"Json Exption error:"+e,Toast.LENGTH_LONG).show()
                }

            },Response.ErrorListener {
                Toast.makeText(this,"Volley error:"+it.printStackTrace(),Toast.LENGTH_LONG).show()
            }){

            override fun getHeaders(): MutableMap<String, String> {
                val Token=sharedPreference.getString("Token",null)
                val ghToken:HashMap<String,String> = HashMap()
                ghToken["Authorization"]="Bearer "+Token
                return ghToken
            }

            override fun getParams(): MutableMap<String, String> {
                val params:HashMap<String,String> = HashMap()
                params["order_number"]=Order_Number.toString()
                params["shop_id"]=Shop_Id.toString()
                params["status"]=status.toString()
                return params
            }
        }
        queue.add(jrOrderProductDetail)
    }
}