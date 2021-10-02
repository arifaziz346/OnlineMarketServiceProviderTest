package com.example.onlinemarketserviceprovider.MyOrder.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinemarketserviceprovider.MyOrder.Modal.Order
import com.example.onlinemarketserviceprovider.MyOrder.OrderProductDetail
import com.example.onlinemarketserviceprovider.R

class TransferredOrderRV(context: Activity, OrderList:ArrayList<Order>): RecyclerView.Adapter<TransferredOrderRV.viewHolder>() {

    private var mContext: Context =context
    private var OrderList:ArrayList<Order> =OrderList
    private var sharedPreferences: SharedPreferences = mContext.getSharedPreferences("ShopDetail",
        AppCompatActivity.MODE_PRIVATE)
    private  val builder = AlertDialog.Builder(mContext)
    private var code:Int=0
//    private var OrderItems=[]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.items_transferred_order_rv,parent,false)

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
        holder.OrderTransferredTv.text="Transferred Date: "+order.TransferredDate



        //todo -------------> Show Order Product Detail list
        holder.btnItemDetail.setOnClickListener(View.OnClickListener {
            val shop_id = sharedPreferences.getString("ShopID",null)
            val intent = Intent(mContext, OrderProductDetail::class.java)
            intent.putExtra("order_number",order.OrderNumber.toString())
            intent.putExtra("shop_id",shop_id.toString())
            intent.putExtra("status","0")
            mContext.startActivity(intent)


        })
    }



    override fun getItemCount(): Int {

        return OrderList.size
    }

    class viewHolder(view: View): RecyclerView.ViewHolder(view){

        var OrderNumber= view.findViewById<TextView>(R.id.OrderNumber)
        var OrderBy= view.findViewById<TextView>(R.id.OrderedBy)
        var OrderDate= view.findViewById<TextView>(R.id.OrderedDate)
        var Phone= view.findViewById<TextView>(R.id.MobileNumber)
        var City= view.findViewById<TextView>(R.id.City)
        var Address= view.findViewById<TextView>(R.id.PhysicalAddress)
        var btnItemDetail = view.findViewById<Button>(R.id.BtnItemsDetail)
        var OrderTransferredTv = view.findViewById<TextView>(R.id.OrderTransferredTv)

    }



}