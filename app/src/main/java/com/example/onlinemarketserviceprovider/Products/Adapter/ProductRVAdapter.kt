package com.example.onlinemarketserviceprovider.Products.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinemarketserviceprovider.Products.Modal.Product
import com.example.onlinemarketserviceprovider.R

class ProductRVAdapter(contex:Context,product:ArrayList<Product>):RecyclerView.Adapter<ProductRVAdapter.viewHolder>() {
    var mContext=contex
    var ProductList:ArrayList<Product> =product


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
       val view = LayoutInflater.from(mContext).inflate(R.layout.items_product_recylerview,parent,false)
    return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       var product = ProductList[position]
        holder.Name.text ="Name:"+product.ProductName.toString()
        holder.price.text ="Price:"+product.Price.toString()
        holder.size.text ="Size"+product.Size.toString()
        holder.quantity.text ="Quantity:"+product.Quantity.toString()
        holder.color.text ="Color:"+product.Color.toString()
        holder.description.text ="Detail:"+product.Description.toString()
    }

    override fun getItemCount(): Int {
        return ProductList.size
    }

    class viewHolder(view: View):RecyclerView.ViewHolder(view){
        var Name= view.findViewById<TextView>(R.id.product_name)
        var color= view.findViewById<TextView>(R.id.product_color)
        var quantity= view.findViewById<TextView>(R.id.product_quantity)
        var size= view.findViewById<TextView>(R.id.product_size)
        var price= view.findViewById<TextView>(R.id.product_price)
        var description= view.findViewById<TextView>(R.id.product_description)

        var productImage = view.findViewById<ImageView>(R.id.product_imageView)
        var btnDelete = view.findViewById<ImageButton>(R.id.product_delete)
        var btnEdit = view.findViewById<ImageButton>(R.id.product_edit)

    }
}