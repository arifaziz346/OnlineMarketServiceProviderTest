package com.example.onlinemarketserviceprovider.MyOrder

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinemarketserviceprovider.MyOrder.Adapter.OrderRV
import com.example.onlinemarketserviceprovider.MyOrder.Modal.Order
import com.example.onlinemarketserviceprovider.R


class FragmentMyOrder : Fragment() {
var OrderList = ArrayList<Order>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        OrderList.add(
            Order(15645,"Arif Aziz","7/22/2021","03429040048","Chitral",
        "Alkhair Electric Stroe Shahi Bazar Chitral",)
        )
        OrderList.add(Order(14545,"Salman Aziz","7/3/2021","03429040048","Islamabad",
            "Alkhair Electric Stroe Shahi Bazar Chitral Alkhair Electric Stroe Shahi Bazar Chitral",))

        OrderList.add(Order(15645,"Faizan Aziz","7/6/2021","03429040048","Peshawar",
            "Alkhair Electric Stroe Shahi Bazar Chitral",))

        OrderList.add(Order(15645,"Abdul Aziz","7/4/2021","03429040048","Chitral",
            "Alkhair Electric Stroe Shahi Bazar Chitral",))

        return inflater.inflate(R.layout.fragment_my_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var orderReyclerView = view.findViewById<RecyclerView>(R.id.RV_MyOrder)
        var layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(context)
        orderReyclerView.layoutManager =layoutManager

        var adapter  = OrderRV(requireContext(),OrderList)
        orderReyclerView.adapter =adapter

        super.onViewCreated(view, savedInstanceState)
    }

}