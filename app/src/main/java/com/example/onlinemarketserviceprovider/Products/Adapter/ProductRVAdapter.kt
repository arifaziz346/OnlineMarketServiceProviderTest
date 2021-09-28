package com.example.onlinemarketserviceprovider.Products.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Helper.LoadingDialog
import com.example.onlinemarketserviceprovider.Products.EditProduct
import com.example.onlinemarketserviceprovider.Products.Modal.Product
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import com.squareup.picasso.Picasso
import org.json.JSONObject

class ProductRVAdapter(contex:Context,product:ArrayList<Product>):RecyclerView.Adapter<ProductRVAdapter.viewHolder>() {
    private var mContext=contex
    private var ProductList:ArrayList<Product> =product
    private var sharedPreference:SharedPreferences =mContext.getSharedPreferences("ShopDetail",AppCompatActivity.MODE_PRIVATE)
    private var LoadingDialog:LoadingDialog = LoadingDialog()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
       val view = LayoutInflater.from(mContext).inflate(R.layout.items_product_recylerview,parent,false)
    return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        var  photoOne:String?=null
        var  photoTwo:String?=null
        var  photoThree:String?=null
        var  photoFour:String?=null
       var product = ProductList[position]
        holder.Name.text ="Name:"+product.ProductName.toString()
        holder.price.text ="Price:"+product.SellPrice.toString()
        holder.size.text ="Size"+product.Size.toString()
        holder.quantity.text ="Quantity:"+product.Quantity.toString()
        holder.color.text ="Color:"+product.Color.toString()
        holder.description.text ="Detail:"+product.Description.toString()
        for (i in 0 until product.Product_Photos.size){
            when(i){
              0->{
                  photoOne=product.Product_Photos[i]
              }
              1->{
                  photoTwo=product.Product_Photos[i]
              }
              2->{
                  photoThree=product.Product_Photos[i]
              }
              3->{
                  photoFour=product.Product_Photos[i]
              }
            }

        }
//        Toast.makeText(mContext,"Discount:"+product.Discount,Toast.LENGTH_SHORT).show()
        Picasso.get().load(UrlConstant.URL+"storage/products/"+photoOne).into(holder.productImage)

        //Use to Delete item
        holder.btnDelete.setOnClickListener(
            View.OnClickListener {
                ProductList.removeAt(position)
                notifyDataSetChanged()
                LoadingDialog.startLoad(mContext as Activity)
            deleteItem(product.ShopId,product.ProductID)
        })

        //Use to edit product
        holder.btnEdit.setOnClickListener(View.OnClickListener
        {
            var intent =Intent(mContext,EditProduct::class.java)
            intent.putExtra("product_id",product.ProductID).toString()
            intent.putExtra("shop_id",product.ShopId).toString()
            intent.putExtra("Name",product.ProductName).toString()
            intent.putExtra("CostPrice",product.CostPrice).toString()
            intent.putExtra("SellPrice",product.SellPrice).toString()
            intent.putExtra("Size",product.Size).toString()
            intent.putExtra("Quantity",product.Quantity).toString()
            intent.putExtra("Color",product.Color).toString()
            intent.putExtra("Discount",product.Discount).toString()
            intent.putExtra("Description",product.Description).toString()
            intent.putExtra("PhotoOne",photoOne).toString()
            intent.putExtra("PhotoTwo",photoTwo).toString()
            intent.putExtra("PhotoThree",photoThree).toString()
            intent.putExtra("PhotoFour",photoFour).toString()
            mContext.startActivity(intent)
        })
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

    // TODO: 9/25/2021  ----------------->this function is use to delete data item from database
    private fun deleteItem(shop_id:Int,product_id:Int){
        //Volley
        val queue = Volley.newRequestQueue(mContext)
        val joDeleteRequest:StringRequest =object :StringRequest(Method.POST,UrlConstant.DeleteProduct,
            Response.Listener {
            val jsonObject=JSONObject(it)
                if (jsonObject.getBoolean("success")){
                    refresh()
                    Toast.makeText(mContext,"Delete successfully",Toast.LENGTH_LONG).show()
                  LoadingDialog.isDismiss()
                }else{
                    Toast.makeText(mContext,"Fail:"+it,Toast.LENGTH_LONG).show()
                    LoadingDialog.isDismiss()
                }
            },
            Response.ErrorListener {
                LoadingDialog.isDismiss()
                Toast.makeText(mContext,"Volley Error:"+it.printStackTrace(),Toast.LENGTH_LONG).show()
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val token =sharedPreference.getString("Token",null)
                val hmtoken:HashMap<String,String> = HashMap()
                hmtoken["Authorization"]="Bearer "+token
                return hmtoken
            }

            override fun getParams(): MutableMap<String, String> {
                var params:HashMap<String,String> =HashMap()
                params["product_id"]=product_id.toString()
                params["shop_id"]=shop_id.toString()
                return params
            }

        }
        queue.add(joDeleteRequest)
    }
    private fun refresh(){

    }
}