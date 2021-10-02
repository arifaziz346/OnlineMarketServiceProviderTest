package com.example.onlinemarketserviceprovider.Products.Adapter

import android.app.Activity
import android.app.AlertDialog
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
import com.example.onlinemarketserviceprovider.Products.MyProduct
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class ProductRVAdapter(contex:Context,product:ArrayList<Product>):RecyclerView.Adapter<ProductRVAdapter.viewHolder>() {
    private var mContext=contex
    private var ProductList:ArrayList<Product> =product
    private var sharedPreference:SharedPreferences =mContext.getSharedPreferences("ShopDetail",AppCompatActivity.MODE_PRIVATE)
    private var LoadingDialog:LoadingDialog = LoadingDialog()
    val inflater= LayoutInflater.from(mContext)
    val mDialogView = inflater.inflate(R.layout.dilaog_update_stock,null)
//    private var mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dilaog_update_stock,null)
    private var product_id:Int?=null
    private var name=mDialogView.findViewById(R.id.et_productNameUpdateStock) as TextInputEditText
    private var costPrice=mDialogView.findViewById(R.id.et_ProductCostPriceUpdateStock) as TextInputEditText
    private var sellPrice=mDialogView.findViewById(R.id.et_ProductSellPriceUpdateStock) as TextInputEditText
    private var size=mDialogView.findViewById(R.id.et_ProductSizeUpdateStock) as TextInputEditText
    private var quantity=mDialogView.findViewById(R.id.et_ProductQuantityUpdateStock) as TextInputEditText
    private var color=mDialogView.findViewById(R.id.et_ProductColorUpdateStock) as TextInputEditText
    private var discount=mDialogView.findViewById(R.id.et_ProductDiscountUpdateStock) as TextInputEditText
    private var btnUpdateStock=mDialogView.findViewById(R.id.btn_UpdateStock) as Button
    private var btnCancelDialogBox=mDialogView.findViewById(R.id.btn_Cancel_diloagBox) as Button
    private lateinit var isDialog:AlertDialog
    private val mBuilder = AlertDialog.Builder(mContext)



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
        holder.Name.text ="Name: "+product.ProductName.toString()
        holder.price.text ="Price: "+product.SellPrice.toString()
        holder.size.text ="Size: "+product.Size.toString()
        holder.quantity.text ="Quantity: "+product.Quantity.toString()
        holder.color.text ="Color: "+product.Color.toString()
//        holder.description.text ="Detail:"+product.Description.toString()
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
        //todo ------------------this TextView is used to update stock
        holder.tvUpdateStock.setOnClickListener(
            View.OnClickListener {
                getProductDetailForUpdateStock(product,mDialogView)

            })

        //todo -----------------Use to Delete item
        holder.btnDelete.setOnClickListener(
            View.OnClickListener {
                ProductList.removeAt(position)
                notifyDataSetChanged()
                LoadingDialog.startLoad(mContext as Activity)
            deleteItem(product.ShopId,product.ProductID)
                isDialog.dismiss()
        })

        //todo Use to edit product
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


        //todo Button UpdateStock-------------------->
        btnUpdateStock.setOnClickListener(View.OnClickListener {
            if(validate()){
                updateStock()



            }
        })
        //todo Button UpdateStock-------------------->
        btnCancelDialogBox.setOnClickListener(View.OnClickListener {
            val intent =Intent(mContext,MyProduct::class.java)
            mContext.startActivity(intent)
        })

    }

    //Update stock
    private fun updateStock() {
         val shop_id =sharedPreference.getString("ShopID",null)
                 val queue = Volley.newRequestQueue(mContext)
                 val joGetProductRequest: StringRequest = object: StringRequest(Method.POST, UrlConstant.UpdateStock,
                     Response.Listener {
                         try {
                             var jsonObject = JSONObject(it)


                             ProductList.clear()

                             if(jsonObject.getBoolean("success")){
                              Toast.makeText(mContext,"Update Stock Successfully",Toast.LENGTH_LONG).show()
                                 }else{
                                     Toast.makeText(mContext,""+jsonObject.getString("message"),Toast.LENGTH_LONG).show()
                                 }


                         }catch(e:JSONException){
                             Toast.makeText(mContext,""+e,Toast.LENGTH_LONG).show()
                         }

                     }, Response.ErrorListener {
                         Toast.makeText(mContext,"Vollery Error:"+it.printStackTrace(), Toast.LENGTH_LONG).show()

                     }){
                     override fun getParams(): MutableMap<String, String> {
                         val params:HashMap<String,String> = HashMap()
                             params["shop_id"]=shop_id.toString()
                             params["product_id"]=product_id.toString()
                             params["SellPrice"]=sellPrice.getText().toString()
                             params["CostPrice"]=costPrice.getText().toString()
                             params["Color"]=color.getText().toString()
                             params["Size"]=size.getText().toString()
                             params["Quantity"]=quantity.getText().toString()
                             params["Discount"]=discount.getText().toString()
                         return params
                     }


                     override fun getHeaders(): MutableMap<String, String> {
                         val token =sharedPreference.getString("Token",null)
                         val hmtoken:HashMap<String,String> = HashMap()
                         hmtoken["Authorization"]="Bearer "+token
                         return hmtoken
                     }

                 }


                 queue.add(joGetProductRequest)

    }

    //Todo get Rroduct Detailf for update stock
    private fun getProductDetailForUpdateStock(product: Product, mDialogView: View) {


        name.setText(product.ProductName)
        costPrice.setText(product.CostPrice.toString())
        sellPrice.setText(product.SellPrice.toString())
        size.setText(product.Size)
        quantity.setText(product.Quantity.toString())
        color.setText(product.Color)
        discount.setText(product.Discount.toString())
        product_id=product.ProductID

        var builder = AlertDialog.Builder(mContext)
        builder.setView(mDialogView)
        builder.setCancelable(false)
//        isDialog=builder.create()
        try{
            isDialog=builder.show()
        }
        catch(e:Exception){
            Toast.makeText(mContext,"Exception:"+e,Toast.LENGTH_LONG).show()
        }




//        mBuilder.setView(mDialogView)
////        mBuilder.setCancelable(false)
//        mBuilder.create()
//        mBuilder.show()



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
//        var description= view.findViewById<TextView>(R.id.product_description)

        var productImage = view.findViewById<ImageView>(R.id.product_imageView)
        var btnDelete = view.findViewById<ImageButton>(R.id.product_delete)
        var btnEdit = view.findViewById<ImageButton>(R.id.product_edit)
        var tvUpdateStock = view.findViewById<TextView>(R.id.tvUpdateStock)

    }

    // TODO: 9/25/2021  ----------------->this function is use to delete data item from database
    private fun deleteItem(shop_id:Int,product_id:Int){
        //Volley
        val queue = Volley.newRequestQueue(mContext)
        val joDeleteRequest:StringRequest =object :StringRequest(Method.POST,UrlConstant.DeleteProduct,
            Response.Listener {
            val jsonObject=JSONObject(it)
                if (jsonObject.getBoolean("success")){

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
    //TODO: For Validation
    private fun validate():Boolean{




        if(costPrice.getText().toString()==""){
            costPrice.error = "Plz Enter Price Of Product"
            return false
        }
        if(sellPrice.getText().toString()==""){
            sellPrice.error = "Plz Enter Sell Price"
            return false
        }



        try {
            if(costPrice.getText().toString().toInt()>sellPrice.getText().toString().toInt()){
                Toast.makeText(mContext,"Cost Price Can't be greater than Sell Price",Toast.LENGTH_LONG).show()
                return false
            }
        }catch(e:Exception){
            if(costPrice.getText().toString().toInt()==null){
                costPrice.setText(null)
            }
         }


        if(quantity.getText().toString()==""){
            quantity.error = "Plz Enter Product Quantity"
            return false
        }

        try{
        if(discount.getText().toString().toInt()>sellPrice.getText().toString().toInt()){
            Toast.makeText(mContext,"Discount Can't be greater than Sell Price",Toast.LENGTH_LONG).show()
            return false
        }
        }catch (e:Exception){
            discount.setText(null)

        }

        return true
    }


}