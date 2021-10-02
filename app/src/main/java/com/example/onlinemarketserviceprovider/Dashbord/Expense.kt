package com.example.onlinemarketserviceprovider.Dashbord

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Dashbord.Adapter.ExpenseAdapterRV
import com.example.onlinemarketserviceprovider.Dashbord.Model.ExpenseModal
import com.example.onlinemarketserviceprovider.Helper.LoadingDialog
import com.example.onlinemarketserviceprovider.MainActivity
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.databinding.ActivityExpenseBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Expense : AppCompatActivity() {
    private var ExpenseList:ArrayList<ExpenseModal> =ArrayList()
    private var AdaperExpenseRV:ExpenseAdapterRV?=null
//    private var ExpenseModal:ExpenseModal?=null
    private var LoadingDialog:LoadingDialog = LoadingDialog()
    private var swipe_refresh_layout: SwipeRefreshLayout? =null
    private var TotalExpense=0
    private var RVexpenseRecyclerView:RecyclerView?=null
    private var sharedPreferences:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences=getSharedPreferences("ShopDetail", MODE_PRIVATE);
        LoadingDialog.startLoad(this)


        binding.addExpenseBtn.setOnClickListener(View.OnClickListener {


            var DialogViewExpense =  LayoutInflater.from(this).inflate(R.layout.custom_dialog_add_expense, null)
            val title = SpannableString("Add Expense")
            title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length,0)

            var mBuilder = AlertDialog.Builder(this)
                .setTitle(title)
                .setView(DialogViewExpense)
            val mAlertDialog = mBuilder.show()
//                .setView(mDialogView)
            mAlertDialog.show()

            var amount=DialogViewExpense.findViewById<EditText>(R.id.et_amountExpense)
            var description=DialogViewExpense.findViewById<EditText>(R.id.et_descrioptionExpense)
            var btnCancel=DialogViewExpense.findViewById<Button>(R.id.btnCancelExpense)
            var btnSubmit=DialogViewExpense.findViewById<Button>(R.id.btnSubmitExpense)

            //---------Btn Submit

            btnSubmit!!.setOnClickListener( View.OnClickListener {
                if(amount.text.toString()!="" && description.text.toString()!=""){
                    addExpense(amount.text.toString(),description.text.toString(),binding)
                    mAlertDialog.dismiss()
                }else{
                    Toast.makeText(this, "Plz Enter amount and description!", Toast.LENGTH_SHORT).show()
                    mAlertDialog.dismiss()
                }

            })

            btnCancel!!.setOnClickListener(View.OnClickListener {

            })

        })



        binding.btnBack.setOnClickListener(View.OnClickListener {
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        })
//        RVexpenseRecyclerView =findViewById(R.id.ExpenseDetail_RV)
        binding.SelectDateTime.setOnClickListener(View.OnClickListener {
            showDatePicker(binding)
        })

        swipe_refresh_layout=binding.itemsswipetorefresh
        swipe_refresh_layout!!.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
        swipe_refresh_layout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            AdaperExpenseRV!!.notifyDataSetChanged()
            ExpenseList.clear()
            getWorker(0,0,0,binding)
        })

        getWorker(0,0,0,binding)
    }
//Add Expense
    private fun addExpense(amount:String,description:String,binding: ActivityExpenseBinding) {


            var queue = Volley.newRequestQueue(this)
                var JsonRequestUpdateProfile:StringRequest =object :StringRequest(Method.POST,UrlConstant.CreateExpense,
                    Response.Listener {
                         var jsonObject=JSONObject(it)
                        if(jsonObject.getBoolean("success")){
                            Toast.makeText(this,"Add successfully",Toast.LENGTH_LONG).show()
                            getWorker(0,0,0,binding)
                        }else{
                            Toast.makeText(this,"Fail to Add!"+jsonObject.toString(),Toast.LENGTH_LONG).show()
                        }

                        try{

                        }catch (ex:Exception){
                         Toast.makeText(this,"Exception:"+ex.message,Toast.LENGTH_LONG).show()
                        }

                    },Response.ErrorListener {
                        Toast.makeText(this,"Vollery:"+it.message,Toast.LENGTH_LONG).show()
                    }){

                    override fun getHeaders(): MutableMap<String, String> {
                        val gHToken:HashMap<String,String> =HashMap()
                        gHToken["Authorization"]="Bearer "+sharedPreferences!!.getString("Token",null).toString()
                        return gHToken
                    }

                    override fun getParams(): MutableMap<String, String> {
                        var param:HashMap<String,String> = HashMap()
                        param["shop_id"]=sharedPreferences!!.getString("ShopID",null).toString()
                        param["amount"]=amount
                        param["description"]=description
                        return  param
                    }



                }
                queue.add(JsonRequestUpdateProfile)

    }

    private fun getWorker(dayOfMonth: Int, monthOfYear: Int, year: Int,binding: ActivityExpenseBinding) {
        TotalExpense=0;
//        AdaperExpenseRV!!.notifyDataSetChanged()
        ExpenseList.clear()

        var sharedPreferance=getSharedPreferences("ShopDetail", MODE_PRIVATE);
        val shop_id=sharedPreferance.getString("ShopID",null).toString()
            var queue = Volley.newRequestQueue(this)
            var JsonRequestUpdateProfile: StringRequest = object : StringRequest(Method.GET,
                UrlConstant.GetExpense+"?shop_id="+shop_id+"&dayOfMonth="+dayOfMonth+"&monthOfYear="+monthOfYear+"&year="+year,
                Response.Listener {
                    var jsonObject = JSONObject(it)

                    try {
                        if(jsonObject.getBoolean("success")){
                            swipe_refresh_layout!!.setRefreshing(false)
                            LoadingDialog.isDismiss()
//                            Toast.makeText(this,"Successfull"+jsonObject.getJSONObject("Expense").toString(), Toast.LENGTH_LONG).show()

                            var expense = jsonObject.getJSONArray("Expense")
                            if(expense.length()<=0){
                                Toast.makeText(this,"Not Found!", Toast.LENGTH_LONG).show()
                                LoadingDialog.isDismiss()
                                swipe_refresh_layout!!.setRefreshing(false)
                            }
                            for (i in 0 until expense.length()){
                                var Expense = expense.getJSONObject(i)
                                TotalExpense+=Expense.getInt("amount")
                                ExpenseList.add(
                                    ExpenseModal(
                                        Expense.getString("created_at").substring(0,10),
                                        Expense.getInt("shop_id"),
                                        Expense.getInt("id"),
                                        Expense.getInt("amount"),
                                        Expense.getString("description")))
                            }

                            //Display Total Expense
                            binding.tvTotalExpense.text ="Total Expense:"+TotalExpense.toString()
                            RVexpenseRecyclerView =binding.ExpenseDetailRV
                            var layoutManager:RecyclerView.LayoutManager = LinearLayoutManager(this)
                            RVexpenseRecyclerView!!.layoutManager = layoutManager
                            AdaperExpenseRV =ExpenseAdapterRV(this,ExpenseList)
                            RVexpenseRecyclerView!!.adapter =AdaperExpenseRV
                        }
                    } catch (ex: Exception) {
                        LoadingDialog.isDismiss()
                        Toast.makeText(this,"Fail "+ex.message, Toast.LENGTH_LONG).show()
                        swipe_refresh_layout!!.setRefreshing(false)
                    }

                }, Response.ErrorListener {
                    LoadingDialog.isDismiss()
                    Toast.makeText(this,"Volley Error"+it.message, Toast.LENGTH_LONG).show()
                    swipe_refresh_layout!!.setRefreshing(false)
                }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val gHToken: HashMap<String, String> = HashMap()
                    gHToken["Authorization"] ="Bearer " + sharedPreferance.getString("Token", null).toString()
                    return gHToken
                }

                    override fun getParams(): MutableMap<String, String> {
                        var params:HashMap<String,String> = HashMap()
                        params["shop_id"]=sharedPreferance.getString("ShopID", null).toString()
                        return params
                    }


            }
            queue.add(JsonRequestUpdateProfile)
        }

    //this is used to display DatePicker-------------->
    fun showDatePicker(binding: ActivityExpenseBinding) {

        // DatePicker

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
//            bin.setText("" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year)
//            binding.SelectDateTime.setText("" + dayOfMonth + " " + monthOfYear + ", " + year)
//            searchExpense(dayOfMonth,monthOfYear,year,binding)
            getWorker(dayOfMonth,(monthOfYear+1),year,binding)

        }, year, month, day)

        dpd.show()
        }





}