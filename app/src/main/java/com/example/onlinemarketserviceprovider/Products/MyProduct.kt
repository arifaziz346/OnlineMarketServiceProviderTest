package com.example.onlinemarketserviceprovider.Products

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Helper.LoadingDialog
import com.example.onlinemarketserviceprovider.Products.Adapter.ProductRVAdapter
import com.example.onlinemarketserviceprovider.Products.Modal.Product
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.databinding.ActivityMyProductBinding
import org.json.JSONObject

import android.text.Editable
import androidx.annotation.NonNull
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.R
import com.squareup.picasso.Picasso
import java.lang.System.load


class MyProduct : AppCompatActivity() {
    private lateinit var sharedPreferance: SharedPreferences
    private var adapter: ProductRVAdapter?=null
    private var ProductRVAdapter: RecyclerView?=null
    private var ProductList= ArrayList<Product>()
    private var loadingDialog: LoadingDialog = LoadingDialog()
    private var ProductPhotos:ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMyProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferance=getSharedPreferences("ShopDetail",MODE_PRIVATE)


        getProducts(binding)
//        loadingDialog.startLoad(this)

        //Search Product
        binding.etProductSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                performSearch(s.toString(),binding)
            }
        })

        //This button is used to create products
        var btnCreateProduct =binding.btnCreateProduct
        btnCreateProduct.setOnClickListener(View.OnClickListener {
           Picasso.get().load(R.drawable.add_product_icon_blue)
            val intent = Intent(this,CreateProduct::class.java)
            startActivity(intent)
        })

        binding.btnBackMyOrder.setOnClickListener(View.OnClickListener {
            finish()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        })


    }

    //todo -----------------------Used to Search Product
    private fun performSearch(searchProduct: String, binding: ActivityMyProductBinding) {

             val shop_id =sharedPreferance.getString("ShopID",null)
              val queue = Volley.newRequestQueue(this)
              val joGetProductRequest: StringRequest = object: StringRequest(Method.POST,UrlConstant.SearchMyProduct ,
                  Response.Listener {
                      try {
                          var jsonObject = JSONObject(it)
                          var products=jsonObject.getJSONArray("Product")

                          ProductList.clear()

                          if(jsonObject.getBoolean("success")){

                              loadingDialog.isDismiss()

                              for(i in 0 until products.length()){
                                  var items =products.getJSONObject(i)
                                  var productPhotos=items.getJSONArray("ProductPhotos")

                                  for(i in 0 until productPhotos.length()){

                                      ProductPhotos.add(productPhotos.getString(i))


                                  }

//                                  Log.d("Discount",items.optInt("Discount").toString())
                                  ProductList.add(Product(
                                      items.optInt("product_id"),
                                      items.optInt("shop_id"),
                                      items.optString("Name"),
                                      items.optString("Color"),
                                      items.optString("Description"),
                                      items.optInt("Quantity"),
                                      items.optString("Size"),
                                      items.optInt("SellPrice"),
                                      items.optInt("CostPrice"),
                                      items.optInt("Discount"),
                                      ProductPhotos
                                  ))
                                  ProductPhotos=ArrayList()
                              }

                              ProductRVAdapter = binding.productsRecyclerView
                              var layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(this)
                              ProductRVAdapter!!.layoutManager = layoutManager

                              adapter = ProductRVAdapter(this,ProductList)
                              ProductRVAdapter!!.adapter = adapter

                              loadingDialog.isDismiss()
                          }else{
                              loadingDialog.isDismiss()
                              Toast.makeText(this,"Fail"+jsonObject, Toast.LENGTH_LONG).show()
                              loadingDialog.isDismiss()
                          }

                      }catch(e:Exception){
                          Toast.makeText(this,"error:"+e, Toast.LENGTH_LONG).show()
                          loadingDialog.isDismiss()
                      }

                  }, Response.ErrorListener {
                      loadingDialog.isDismiss()
                      Toast.makeText(this,"Vollery Error:"+it.printStackTrace(), Toast.LENGTH_LONG).show()

                  }){

                  override fun getParams(): MutableMap<String, String> {
                      val shop_id =sharedPreferance.getString("ShopID",null)
                      var params:HashMap<String,String> = HashMap()
                      params["shop_id"]=shop_id.toString()
                      params["search"]=searchProduct
                      return params
                  }

                  override fun getHeaders(): MutableMap<String, String> {
                      val token =sharedPreferance.getString("Token",null)
                      val hmtoken:HashMap<String,String> = HashMap()
                      hmtoken["Authorization"]="Bearer "+token
                      return hmtoken
                  }

              }


              queue.add(joGetProductRequest)
          }


    //------------>todo it will get all the products from server

    private fun getProducts(binding: ActivityMyProductBinding) {
        loadingDialog.startLoad(this)

        val shop_id =sharedPreferance.getString("ShopID",null)
        val queue = Volley.newRequestQueue(this)
        val joGetProductRequest: StringRequest = object: StringRequest(Method.GET, UrlConstant.GetProduct+shop_id,
            Response.Listener {
                try {
                    var jsonObject = JSONObject(it)
                    var products=jsonObject.getJSONArray("Product")

                    ProductList.clear()

                    if(jsonObject.getBoolean("success")){


                        for(i in 0 until products.length()){
                            var items =products.getJSONObject(i)
                            var productPhotos=items.getJSONArray("ProductPhotos")

                            for(i in 0 until productPhotos.length()){

                                ProductPhotos.add(productPhotos.getString(i))


                            }

//                            Log.d("Discount",items.optInt("Discount").toString())
                            ProductList.add(Product(
                                items.optInt("product_id"),
                                items.optInt("shop_id"),
                                items.optString("Name"),
                                items.optString("Color"),
                                items.optString("Description"),
                                items.optInt("Quantity"),
                                items.optString("Size"),
                                items.optInt("SellPrice"),
                                items.optInt("CostPrice"),
                                items.optInt("Discount"),
                                ProductPhotos
                            ))
                            ProductPhotos=ArrayList()
                        }

                        ProductRVAdapter = binding.productsRecyclerView
                        var layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(this)
                        ProductRVAdapter!!.layoutManager = layoutManager

                        adapter = ProductRVAdapter(this,ProductList)
                        ProductRVAdapter!!.adapter = adapter
                        loadingDialog.isDismiss()

                    }else{
                        loadingDialog.isDismiss()
                        Toast.makeText(this,"Fail"+jsonObject, Toast.LENGTH_LONG).show()
                    }

                }catch(e:Exception){
                    Toast.makeText(this,"error:"+e, Toast.LENGTH_LONG).show()
                    loadingDialog.isDismiss()
                }

            }, Response.ErrorListener {
                Toast.makeText(this,"Vollery Error:"+it.printStackTrace(), Toast.LENGTH_LONG).show()
                loadingDialog.isDismiss()
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val token =sharedPreferance.getString("Token",null)
                val hmtoken:HashMap<String,String> = HashMap()
                hmtoken["Authorization"]="Bearer "+token
                return hmtoken
            }

        }


        queue.add(joGetProductRequest)
    }

    override fun onBackPressed() {
        finish()
       val  intent=Intent(this,MainActivity::class.java)
       startActivity(intent)

    }
}