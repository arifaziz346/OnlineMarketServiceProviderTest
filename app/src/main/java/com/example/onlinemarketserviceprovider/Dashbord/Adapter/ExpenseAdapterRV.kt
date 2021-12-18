package com.example.onlinemarketserviceprovider.Dashbord.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Dashbord.Expense
import com.example.onlinemarketserviceprovider.Dashbord.Model.ExpenseModal
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import org.json.JSONObject

class ExpenseAdapterRV(mContext:Context,expenseList:ArrayList<ExpenseModal>):RecyclerView.Adapter<ExpenseAdapterRV.viewHolder>() {

    private var context = mContext
    private var ExpenseList: ArrayList<ExpenseModal> = expenseList
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view = LayoutInflater.from(context)
            .inflate(R.layout.items_expense_month_wise_lv, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        sharedPreferences =
            context.getSharedPreferences("ShopDetail", AppCompatActivity.MODE_PRIVATE);
        var Expense = ExpenseList[position]
        holder.amount.text = "Rs." + Expense.amount.toString()
        holder.month.text = Expense.month
        holder.description.text = "Description: " + Expense.description
//        holder.edit.setOnClickListener {
//           View.OnClickListener{
//               Toast.makeText(context,"Edit",Toast.LENGTH_LONG).show()
////               editExpense(Expense.shop_id,Expense.expense_id,amount,description,sharedPreferences!!.getString("Token",null).toString())
//           } }

        holder.edit.setOnClickListener(
            View.OnClickListener {
//                Toast.makeText(context,"Edit"+Expense.shop_id+" "+Expense.expense_id,Toast.LENGTH_LONG).show()
                var DialogView =
                    LayoutInflater.from(context).inflate(R.layout.custom_dialog_add_expense, null)
                val title = SpannableString("Edit Expense")
                title.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    title.length,
                    0
                )
                var mBuilder = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setView(DialogView)
                val mAlertDialog = mBuilder.show()
//                .setView(mDialogView)
                mAlertDialog.show()

                var amount = DialogView.findViewById<EditText>(R.id.et_amountExpense)
                amount.setText(Expense.amount.toString())
                var description = DialogView.findViewById<EditText>(R.id.et_descrioptionExpense)
                description.setText(Expense.description.toString())
                var btnCancel = DialogView.findViewById<Button>(R.id.btnCancelExpense)
                var btnSubmit = DialogView.findViewById<Button>(R.id.btnSubmitExpense)

                //---------Btn Submit

                btnSubmit!!.setOnClickListener(View.OnClickListener {
                    if (amount.text.toString() != "" && description.text.toString() != "") {
                        editExpense(
                            Expense.shop_id,
                            Expense.expense_id,
                            amount.text.toString(),
                            description.text.toString(),
                            sharedPreferences!!.getString("Token", null).toString(),
                            position
                        )
                        mAlertDialog.dismiss()
                    } else {
                        Toast.makeText(
                            context,
                            "Plz Enter amount and description!",
                            Toast.LENGTH_SHORT
                        ).show()
                        mAlertDialog.dismiss()
                    }

                })

                btnCancel!!.setOnClickListener(View.OnClickListener {
                    mAlertDialog.dismiss()
                })
            })

        holder.delete.setOnClickListener(
            View.OnClickListener {
                delete(Expense.shop_id.toString(),Expense.expense_id.toString())
            }
        )
    }


    override fun getItemCount(): Int {

        return ExpenseList.size
    }


    class viewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var month = view.findViewById<TextView>(R.id.month_ExpenseTV)
        var amount = view.findViewById<TextView>(R.id.AmountExpenseTV)
        var edit = view.findViewById<TextView>(R.id.EditExpense)
        var delete = view.findViewById<TextView>(R.id.DeleteExpense)
        var description = view.findViewById<TextView>(R.id.expenseDescriptionTV)
    }

    private fun editExpense(
        shopId: Int,
        expenseId: Int,
        amount: String,
        description: String,
        token: String,
        position: Int
    ) {


        var queue = Volley.newRequestQueue(context)
        var JsonRequestUpdateProfile: StringRequest = object : StringRequest(Method.POST,
            UrlConstant.EditExpense,
            Response.Listener {
                var jsonObject = JSONObject(it)


                try {
                    if (jsonObject.getBoolean("success")) {
                        Toast.makeText(context, "Edit successfull", Toast.LENGTH_LONG).show()
//                                ExpenseList.removeAt(position)
//                                notifyDataSetChanged()
                        (context as Expense).notifyDataUpdate()


                    } else {
                        Toast.makeText(
                            context,
                            "Fail to Edit " + jsonObject.getString("message").toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } catch (ex: Exception) {
                    Toast.makeText(context, "Exception:" + ex, Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(context, "Volley Error", Toast.LENGTH_LONG).show()
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val gHToken: HashMap<String, String> = HashMap()
                gHToken["Authorization"] = "Bearer " + token
                return gHToken
            }

            override fun getParams(): MutableMap<String, String> {
                var params: HashMap<String, String> = HashMap()
                params["Shop_id"] = shopId.toString()
                params["expense_id"] = expenseId.toString()
                params["amount"] = amount
                params["description"] = description
                return params
            }


        }
        queue.add(JsonRequestUpdateProfile)
    }


    //used to Delete
    private fun delete(shop_id:String,expense_id:String) {
        var queue = Volley.newRequestQueue(context)
        var JsonRequestUpdateProfile: StringRequest =
            object : StringRequest(Method.POST, UrlConstant.DeleteExpense,
                Response.Listener {
                    var jsonObject = JSONObject(it)

                    try {
                        if(jsonObject.getBoolean("success")){
                         Toast.makeText(context,"Delete Successfully",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(context,"Message:"+jsonObject.getString("message").toString(),Toast.LENGTH_LONG).show()
                        }

                    } catch (ex: Exception) {
                        Toast.makeText(context,"Exception"+ex.message.toString(),Toast.LENGTH_LONG).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(context,"Volley Error"+it.message.toString(),Toast.LENGTH_LONG).show()
                }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val gHToken: HashMap<String, String> = HashMap()
                    gHToken["Authorization"] = "Bearer " + sharedPreferences!!.getString("Token",null).toString()
                    return gHToken
                }

                override fun getParams(): MutableMap<String, String> {
                    var params:HashMap<String,String> = HashMap()
                    params["shop_id"]=shop_id
                    params["expense_id"]=expense_id
                    return params
                }


            }
        queue.add(JsonRequestUpdateProfile)

}
}