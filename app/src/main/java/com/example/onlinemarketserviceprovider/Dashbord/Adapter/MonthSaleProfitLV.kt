package com.example.onlinemarketserviceprovider.Dashbord.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.onlinemarketserviceprovider.Dashbord.Model.SaleProfitDetail
import com.example.onlinemarketserviceprovider.R

class MonthSaleProfitLV(context: Context,SaleProfitDetail:ArrayList<SaleProfitDetail>) :BaseAdapter(){
    private var SaleProfitDetail:ArrayList<SaleProfitDetail> =SaleProfitDetail
    private var mContext=context
    override fun getCount(): Int {
        return SaleProfitDetail.size
    }

    override fun getItem(position: Int): Any {
       return SaleProfitDetail[position]
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var SaleDetail =SaleProfitDetail[position]
        var view = LayoutInflater.from(mContext).inflate(R.layout.items_sale_profit_month_wise_lv,parent,false)
        var Month =view.findViewById<TextView>(R.id.monthTV)
        var Profit =view.findViewById<TextView>(R.id.ProfitTV)
        var Sale =view.findViewById<TextView>(R.id.SaleTV)

        Month.text =SaleDetail.Month
        Profit.text ="Rs."+SaleDetail.Profit.toString()
        Sale.text ="Rs."+SaleDetail.Sale.toString()


    return view
    }
}