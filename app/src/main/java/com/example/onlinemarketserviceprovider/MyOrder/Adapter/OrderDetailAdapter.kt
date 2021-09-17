package com.example.onlinemarketserviceprovider.MyOrder.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.onlinemarketserviceprovider.MyOrder.Modal.OrderItem
import com.example.onlinemarketserviceprovider.R

class OrderDetailAdapter(context: Context, items:ArrayList<OrderItem>): BaseAdapter() {
    var mContext = context
    var Item:ArrayList<OrderItem> = items
    override fun getCount(): Int {
        return Item.size
    }

    override fun getItem(position: Int): Any {
        return Item[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var BuyItem =Item[position]

        var view = LayoutInflater.from(mContext).inflate(R.layout.order_detail_items,parent,false)
        var ItemName = view.findViewById<TextView>(R.id.itemName)
        var ItemPrice = view.findViewById<TextView>(R.id.ItemPrice)
//        var ItemDiscount = view.findViewById<TextView>(R.id.itemDiscount)
        var ItemColor = view.findViewById<TextView>(R.id.itemColor)
        var ItemSize = view.findViewById<TextView>(R.id.itemSize)

        ItemName.text ="Name:"+ BuyItem.ProductName+" |"
        ItemPrice.text ="Price:"+BuyItem.Price.toString()+" |"
//        ItemDiscount.text = BuyItem.Discount.toString()
        ItemColor.text ="Color:"+BuyItem.Color.toString()+" |"
        ItemSize.text ="Size:"+BuyItem.Size.toString()+" |"

        return view
    }
}