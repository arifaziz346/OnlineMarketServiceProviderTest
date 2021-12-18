package com.example.onlinemarketserviceprovider.MyOrder

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Helper.LoadingDialog
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.MyOrder.Adapter.TransferredOrderRV
import com.example.onlinemarketserviceprovider.MyOrder.Modal.Order
import com.example.onlinemarketserviceprovider.MyOrder.Modal.OrderItem
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import org.json.JSONObject

class TransferredOrderDetail : AppCompatActivity() {
    private var OrderList = ArrayList<Order>()
    private var OrderItems = ArrayList<OrderItem>()
    private var OrderNumber: Int? = null
    private var OrderBy: String? = null
    private var OrderDate: String? = null
    private var Phone: String? = null
    private var City: String? = null
    private var TransferredDate:String?=null
    private var Address: String? = null
    private var PaymentType: Int? = null
    private var sharedPreferences: SharedPreferences? = null
    private var orderReyclerView: RecyclerView? = null
    private var adapter: TransferredOrderRV? = null
    private var btnBack: AppCompatButton?=null
    private var LoadingDialog: LoadingDialog = LoadingDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transferred_order)

        //init
        btnBack=findViewById(R.id.btnBackMyOrder)

        btnBack!!.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })
        //todo ------> Function get MyOrder from serve

        getMyOrderList()

        orderReyclerView = findViewById(R.id.RV_MyOrder) as RecyclerView
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        orderReyclerView!!.layoutManager = layoutManager
    }

    //todo Get MyOrder from Server------------------------------------------------------------------------------->
    fun getMyOrderList() {
        LoadingDialog.startLoad(this)
        val queue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("ShopDetail", AppCompatActivity.MODE_PRIVATE)
        val token = sharedPreferences!!.getString("Token", null)
        val shop_id = sharedPreferences!!.getString("ShopID", null)

        //Volley Request---------------------------------------------------->
        val myOrderRequest: StringRequest = object : StringRequest(
            Method.GET, UrlConstant.GetTransferredOrder + shop_id, Response.Listener {
                try {

                    var jsonObject = JSONObject(it)
                    if (jsonObject.getBoolean("success")) {

                        val OrderDetail = jsonObject.getJSONArray("OrderDetail")

                        if(OrderDetail.length()<=0){
                            Toast.makeText(this,"Sorry, No data available...!",Toast.LENGTH_SHORT).show()
                        }
                        OrderItems.clear()

                        for (i in 0 until OrderDetail.length()) {
                            var item = OrderDetail.getJSONObject(i)
//                            var productDetail =item.getJSONArray("ProductDetail")

                            OrderNumber = item.optString("order_number").toInt()
                            OrderBy = item.optString("Orderby")
                            OrderDate = item.optString("created_at")
                            Phone = item.optString("phone")
                            City = item.optString("city")
                            Address = item.optString("address")
                            PaymentType = item.optString("payment_type").toInt()
                            TransferredDate=item.optString("OrderTransferredDate")

//                            for (i in 0 until productDetail.length()){
//                                val ProductDetail = productDetail.getJSONObject(i)
////
//                                OrderItems.add(
//                                    OrderItem(ProductDetail.optString("ProductName"),
//                                        ProductDetail.optString("quantity").toInt(),
//                                        ProductDetail.optString("ProductPrice").toInt(),
//                                        ProductDetail.optString("discount").toInt(),
//                                        ProductDetail.optString("Color"),
//                                        ProductDetail.optString("Size"),
//                                        ProductDetail.optString("order_number").toInt()
//
//                                    ))}
                            OrderList.add(
                                Order(
                                    OrderNumber!!,
                                    OrderBy!!,
                                    OrderDate!!,
                                    Phone!!,
                                    City!!,
                                    Address!!,
                                    PaymentType!!,
                                    TransferredDate!!,
                                    OrderItems
                                )
                            )

                        }

                        adapter = TransferredOrderRV(this, OrderList)
                        orderReyclerView!!.adapter = adapter
                        LoadingDialog.isDismiss()
                    } else {
                        //todo ---------------->handle token expire
                        Toast.makeText(
                            this,
                            "Token has expire:Dont worry i will handle it.",
                            Toast.LENGTH_LONG
                        ).show()
                        LoadingDialog.isDismiss()
                    }

                } catch (e: Exception) {
                    Toast.makeText(this, "error:" + e, Toast.LENGTH_LONG).show()
                    LoadingDialog.isDismiss()
                }


            }, Response.ErrorListener {
                //Failure
                Log.d("VolleyErorr", it.toString())
                Toast.makeText(this, "error:" + it.printStackTrace(), Toast.LENGTH_SHORT).show()
                LoadingDialog.isDismiss()
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
}
