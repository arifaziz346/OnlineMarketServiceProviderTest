package com.example.onlinemarketserviceprovider.MyOrder.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinemarketserviceprovider.MyOrder.Modal.Order
import com.example.onlinemarketserviceprovider.R

class OrderRV(context: Context,OrderList:ArrayList<Order>):RecyclerView.Adapter<OrderRV.viewHolder>() {
    var mContext:Context =context
    var OrderList:ArrayList<Order> =OrderList



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.items_my_product_order,parent,false)
    return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        var order = OrderList[position]
        holder.OrderNumber.text ="Order Number:"+ order.OrderNumber.toString()
        holder.OrderDate.text ="Order Date:"+ order.OrderDate.toString()
        holder.OrderBy.text ="Order By"+ order.OrderBy.toString()
        holder.Phone.text ="Phone:"+order.Phone.toString()
        holder.City.text= "City:"+order.City.toString()
        holder.Address.text ="Address"+order.address.toString()
    }

    override fun getItemCount(): Int {

    return OrderList.size}

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
}