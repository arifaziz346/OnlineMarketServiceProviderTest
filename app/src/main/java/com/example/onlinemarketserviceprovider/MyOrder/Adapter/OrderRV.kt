package com.example.onlinemarketserviceprovider.MyOrder.Adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.MyOrder.Modal.Order
import com.example.onlinemarketserviceprovider.MyOrder.OrderProductDetail
import com.example.onlinemarketserviceprovider.R
import kotlinx.coroutines.NonCancellable.cancel
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OrderRV(context: Context,OrderList:ArrayList<Order>):RecyclerView.Adapter<OrderRV.viewHolder>() {

    private var mContext:Context =context
    private var OrderList:ArrayList<Order> =OrderList
    private var sharedPreferences:SharedPreferences = mContext.getSharedPreferences("ShopDetail",AppCompatActivity.MODE_PRIVATE)
    private  val builder = AlertDialog.Builder(mContext)
    private var code:Int=0
//    private var OrderItems=[]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.items_my_product_order,parent,false)

    return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        var order = OrderList[position]


        holder.OrderNumber.text ="Order Number:"+ order.OrderNumber.toString()
        holder.OrderDate.text ="Order Date: "+ order.OrderDate.toString()
        holder.OrderBy.text ="OrderBy: "+ order.OrderBy.toString()
        holder.Phone.text ="Phone: "+order.Phone.toString()
        holder.City.text= "City: "+order.City.toString()
        holder.Address.text ="Address:"+order.Address.toString()


        //Btn Cancel Order
        holder.btnCancel.setOnClickListener(View.OnClickListener {

            //todo ------------->Show confirmation message
           val message:String ="Are you sure you want to cancel this order?"
            code=1
            confirmtionMessage(order.OrderNumber,message,code)


        })

        //todo -------------> Show confirmation message (Transfer Order)
        holder.btnTransfer.setOnClickListener(View.OnClickListener {
            val message:String ="Are you sure you want to transfer this order?"
            code=2
            confirmtionMessage(order.OrderNumber,message,code)
        })

        //todo -------------> Show Order Product Detail list
        holder.btnItemDetail.setOnClickListener(View.OnClickListener {
            val shop_id = sharedPreferences.getString("ShopID",null)
            val intent =Intent(mContext,OrderProductDetail::class.java)
            intent.putExtra("order_number",order.OrderNumber.toString())
            intent.putExtra("shop_id",shop_id.toString())
            mContext.startActivity(intent)


        })
    }



    override fun getItemCount(): Int {

    return OrderList.size
    }

    class viewHolder(view: View):RecyclerView.ViewHolder(view){

        var OrderNumber= view.findViewById<TextView>(R.id.OrderNumber)
        var OrderBy= view.findViewById<TextView>(R.id.OrderedBy)
        var OrderDate= view.findViewById<TextView>(R.id.OrderedDate)
        var Phone= view.findViewById<TextView>(R.id.MobileNumber)
        var City= view.findViewById<TextView>(R.id.City)
        var Address= view.findViewById<TextView>(R.id.PhysicalAddress)

        var btnCancel = view.findViewById<Button>(R.id.btnCancelOrder)
        var btnTransfer = view.findViewById<Button>(R.id.btnTransferOrder)
        var btnItemDetail = view.findViewById<Button>(R.id.BtnItemsDetail)

    }


    //todo --------------> DilogBox for confimation <----------------------
    private fun confirmtionMessage(OrderNumber:Int,message:String,code:Int) {
        val builder = AlertDialog.Builder(mContext)
        //set title for alert dialog
        builder.setTitle(message)
        //performing cancel action
        //Yes will cancel order
        builder.setNeutralButton("Yes"){dialogInterface , which ->


            //todo --------------> this function is used to cancel order
            when(code){
                1->(cancelOrder(OrderNumber))
                2->(transferOrder(OrderNumber))

            }


        }
        //No will clos dialog box
        builder.setNegativeButton("No"){dialogInterface, which ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

    //todo Cancel order------------------------------------->
    private fun cancelOrder(Order_Number:Int) {
        var queue =Volley.newRequestQueue(mContext)

        var jrCancelOrder:StringRequest =object :StringRequest(Request.Method.POST,UrlConstant.CancelOrder,
            Response.Listener {
                val jsonObject =JSONObject(it)
                try {
                    if(jsonObject.getBoolean("success")){
                        Toast.makeText(mContext,""+jsonObject.get("message"),Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(mContext,"Fail to cancel Order!",Toast.LENGTH_LONG).show()
                    }
                }catch(e:JSONException){
                    Toast.makeText(mContext,"Json Error:"+e.printStackTrace(),Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(mContext,"Volley Error:"+it.printStackTrace(),Toast.LENGTH_SHORT).show()

            }){
            override fun getHeaders(): MutableMap<String, String> {
                val ghToken:MutableMap<String,String> = HashMap()
                val token = sharedPreferences.getString("Token",null)
                ghToken["Authorization"]="Bearer "+token
                return ghToken
            }
            override fun getParams(): MutableMap<String, String> {

                val param:HashMap<String,String> = HashMap()
                param["order_number"]=Order_Number.toString()
                return param
            }
        }

        queue.add(jrCancelOrder)
    }

    //todo Transfer order----------------------------------->
    private  fun transferOrder(OrderNumber: Int){
        var queue =Volley.newRequestQueue(mContext)
        var jrTransferOrder:StringRequest = object :StringRequest(Method.POST,UrlConstant.TransferOrder,Response.Listener {
               var jsonObject:JSONObject = JSONObject(it)
          try{
              if(jsonObject.getBoolean("success")){
                  Toast.makeText(mContext,"Success"+jsonObject.get("message"),Toast.LENGTH_LONG).show()
              }else{
                  Toast.makeText(mContext,"error:Fail"+jsonObject.get("message"),Toast.LENGTH_LONG).show()
              }
          }catch(e:Exception){
              Toast.makeText(mContext,"error:"+e.printStackTrace(),Toast.LENGTH_LONG).show()
          }
        },Response.ErrorListener {
            Toast.makeText(mContext,"error:"+it.printStackTrace(),Toast.LENGTH_LONG).show()
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val token = sharedPreferences.getString("Token",null)
                val ghToken:HashMap<String,String> = HashMap()
                ghToken["Authorization"]="Bearer "+token
                return ghToken
            }

            override fun getParams(): MutableMap<String, String> {
                val shop_id = sharedPreferences.getString("ShopID",null)
                var params:HashMap<String,String> = HashMap()
                params["order_number"]=OrderNumber.toString()
                params["shop_id"]=shop_id.toString()
                return params
            }
        }
         queue.add(jrTransferOrder)


    }
}