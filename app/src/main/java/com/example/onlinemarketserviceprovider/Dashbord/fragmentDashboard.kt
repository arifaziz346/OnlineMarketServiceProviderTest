package com.example.onlinemarketserviceprovider.Dashbord

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.Constant
import com.example.onlinemarketserviceprovider.databinding.FragmentDashboardBinding
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.StringRequest




/**
 * A simple [Fragment] subclass.
 * Use the [fragmentDashboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentDashboard : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val binding=FragmentDashboardBinding.inflate(inflater,container,false)
        getDashboardDetail(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
 }

    //GetDashboardDetail from the server----------------------------->
    private fun getDashboardDetail(binding:FragmentDashboardBinding) {
        val  sharedPreferences:SharedPreferences = requireContext().getSharedPreferences("ShopDetail",AppCompatActivity.MODE_PRIVATE)
        val id =sharedPreferences.getString("ShopID",null)
//        Toast.makeText(requireContext(),"ID:" +id,Toast.LENGTH_SHORT).show()
        val myReq: StringRequest = object : StringRequest(
            Method.GET,
            Constant.DashboardDetail+id,
            Response.Listener {
                try {
                      var jODashboardDetail =JSONObject(it)
                    if(jODashboardDetail.getBoolean("success")){
                        var DashBoardDetail =jODashboardDetail.getJSONObject("DashboardDetail")
                        binding.tvTotalSale.text ="Total Sale: "+ DashBoardDetail.getString("TotalSale")
                        binding.tvOrder.text = DashBoardDetail.getString("TotalOrder")
                        binding.tvAbandonedOrder.text =DashBoardDetail.getString("Abounded")
                        binding.tvProductSold.text =DashBoardDetail.getString("TotalProductSold")
                        binding.tvProfit.text =DashBoardDetail.getString("TotalProfit")
                    }
                }catch (e:JSONException){
                    Toast.makeText(requireContext(),"error:"+e.printStackTrace(),Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(requireContext(),"VolleyFailure:"+it.printStackTrace(),Toast.LENGTH_SHORT).show()
            }
        ) {
//            @Throws(AuthFailureError::class)
//            override fun getParams(): Map<String, String>? {
//                val  sharedPreferences:SharedPreferences = requireContext().getSharedPreferences("ShopDetail",AppCompatActivity.MODE_PRIVATE)
//        val id =sharedPreferences.getString("ShopID",null)
//        val token = sharedPreferences.getString("Token",null)
//                val params: MutableMap<String, String> = HashMap()
//                params["id"] = 201.toString()
//
//                return params
//            }
            val token = sharedPreferences.getString("Token",null)
            //Get Header is used to provide token to the server
            override fun getHeaders(): MutableMap<String, String> {
                val tokenGH: MutableMap<String, String> = HashMap()
                tokenGH["Authorization"] = "Bearer "+token
                return tokenGH
            }
        }
        val queue =Volley.newRequestQueue(requireContext())
        queue.add(myReq)
    }

}