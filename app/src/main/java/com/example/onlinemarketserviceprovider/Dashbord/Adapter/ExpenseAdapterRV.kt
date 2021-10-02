package com.example.onlinemarketserviceprovider.Dashbord.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onlinemarketserviceprovider.Dashbord.Model.ExpenseModal
import com.example.onlinemarketserviceprovider.R

class ExpenseAdapterRV(mContext:Context,expenseList:ArrayList<ExpenseModal>):RecyclerView.Adapter<ExpenseAdapterRV.viewHolder>() {

var context=mContext
var ExpenseList:ArrayList<ExpenseModal> =  expenseList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.items_expense_month_wise_lv,parent,false)
    return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
       var Expense =ExpenseList[position]
        holder.amount.text ="Rs."+Expense.amount.toString()
        holder.month.text =Expense.month
        holder.description.text ="Description: "+Expense.description
//        holder.edit.setOnClickListener {
//            View.OnClickListener{
//
//        } }
    }

    override fun getItemCount(): Int {

        return ExpenseList.size
    }


    class viewHolder(view:View):RecyclerView.ViewHolder(view){
     var month = view.findViewById<TextView>(R.id.month_ExpenseTV)
     var amount = view.findViewById<TextView>(R.id.AmountExpenseTV)
     var edit = view.findViewById<TextView>(R.id.EditExpense)
     var delete = view.findViewById<TextView>(R.id.DeleteExpense)
     var description =view.findViewById<TextView>(R.id.expenseDescriptionTV)
    }
}