package com.example.onlinemarketserviceprovider.Products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinemarketserviceprovider.Products.Adapter.ProductRVAdapter
import com.example.onlinemarketserviceprovider.Products.Modal.Product
import com.example.onlinemarketserviceprovider.R

class FragmentProduct : Fragment() {
 var Product= ArrayList<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Product.add(
            Product(1,2,"Oppo A12","Blue","We are just testing our app",
            10,"12-12",4000)
        )

        Product.add(Product(1,2,"Apple","White","We are just testing our app We are just testing our appWe are just testing our app",
            10,"12-12",4000))

        Product.add(Product(1,2,"Hp lapTop","Silver","We are just testing our app",
            10,"12-12",4000))

        Product.add(Product(1,2,"Infinix","Blue","We are just testing our app",
            10,"12-12",4000))

        Product.add(Product(1,2,"LapTop","Blue","We are just testing our app",
            10,"12-12",4000))

        return inflater.inflate(R.layout.fragment_product, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mContext:Context
        var btnCreateProduct =view.findViewById<Button>(R.id.btn_CreateProduct)
        btnCreateProduct.setOnClickListener(View.OnClickListener {
        val intent = Intent(context,CreateProduct::class.java)
            startActivity(intent)
        })

        //
        var ProductRVAdapter = view.findViewById<RecyclerView>(R.id.products_recyclerView)
        var layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context)
        ProductRVAdapter.layoutManager = layoutManager

        var adapter = ProductRVAdapter(requireContext(),Product)
        ProductRVAdapter.adapter = adapter

    }
}