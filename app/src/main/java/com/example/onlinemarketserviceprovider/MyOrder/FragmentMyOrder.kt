package com.example.onlinemarketserviceprovider.MyOrder

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.MyOrder.Adapter.OrderRV
import com.example.onlinemarketserviceprovider.MyOrder.Modal.Order
import com.example.onlinemarketserviceprovider.MyOrder.Modal.OrderItem
import com.example.onlinemarketserviceprovider.R
import org.json.JSONObject


class FragmentMyOrder : Fragment() {
private var OrderList = ArrayList<Order>()
private var OrderItems =ArrayList<OrderItem>()
private var OrderNumber:Int?=null
private var OrderBy:String?=null
private var OrderDate:String?= null
private var Phone:String?=null
private var City:String?= null
private var Address:String?= null
private var PaymentType:Int?=null
private var sharedPreferences:SharedPreferences?=null
private var orderReyclerView:RecyclerView?=null
private var adapter:OrderRV?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        //todo ------> Function get MyOrder from serve
        getMyOrderList()

        return inflater.inflate(R.layout.fragment_my_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        orderReyclerView = view.findViewById<RecyclerView>(R.id.RV_MyOrder)
        var layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context)
        orderReyclerView!!.layoutManager =layoutManager
        super.onViewCreated(view, savedInstanceState)
    }
    //Get MyOrder from Server----------------------------------------------------------------------------------------->
    fun getMyOrderList(){

        val queue =Volley.newRequestQueue(requireContext())
         sharedPreferences =requireContext().getSharedPreferences("ShopDetail",AppCompatActivity.MODE_PRIVATE)
         val token =sharedPreferences!!.getString("Token",null)
        val shop_id =sharedPreferences!!.getString("ShopID",null)

        //Volley Request---------------------------------------------------->
        val myOrderRequest:StringRequest = object :StringRequest(
            Method.GET,UrlConstant.MyOrderProduct+shop_id,Response.Listener {
                try {

                    var jsonObject =JSONObject(it)
                    if(jsonObject.getBoolean("success")){

                     val OrderDetail =jsonObject.getJSONArray("OrderDetail")
                        OrderItems.clear()
                        OrderList.clear()

                        for (i in 0 until OrderDetail.length()) {
                            var item = OrderDetail.getJSONObject(i)
//                            var productDetail =item.getJSONArray("ProductDetail")

                              OrderNumber=item.optString("order_number").toInt()
                              OrderBy=item.optString("Orderby")
                              OrderDate=item.optString("created_at")
                              Phone=item.optString("phone")
                              City=item.optString("city")
                              Address=item.optString("address")
                              PaymentType=item.optString("payment_type").toInt()


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
                            OrderList.add(Order(OrderNumber!!,OrderBy!!,OrderDate!!,Phone!!,City!!,Address!!,PaymentType!!,OrderItems))

                        }
                        adapter  = OrderRV(requireContext(),OrderList)
                        orderReyclerView!!.adapter =adapter
                       }else{
                          //todo ---------------->handle token expire
                        Toast.makeText(requireContext(), "Token has expire:Dont worry i will handle it.", Toast.LENGTH_LONG).show()
                         }

                }catch(e:Exception){
                    Toast.makeText(requireContext(), "error:"+e, Toast.LENGTH_LONG).show()

                }


        },Response.ErrorListener {
            //Failure
                Log.d("VolleyErorr", it.toString())
                Toast.makeText(requireContext(), "error:"+it.printStackTrace(), Toast.LENGTH_SHORT).show()
        })
        {
            //Provide Token for authorization--------------------->
            override fun getHeaders(): MutableMap<String, String> {
                val tokenGH: MutableMap<String,String> = HashMap()
                tokenGH["Authorization"] = "Bearer "+token
                return tokenGH
            }
          }

           queue.add(myOrderRequest)
    }
}