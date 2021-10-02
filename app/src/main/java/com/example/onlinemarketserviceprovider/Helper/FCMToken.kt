package com.example.onlinemarketserviceprovider.Helper

import android.app.Activity
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.UrlConstant
import org.json.JSONObject
import java.lang.Exception
import com.google.firebase.messaging.FirebaseMessaging

class FCMToken {




   // /todo Generate FCM Token -------for notification
    fun generateFCMToken(token:String,shop_id:String,fcm_token:String,mActivity:Activity){
        var queue = Volley.newRequestQueue(mActivity)
        var JsonObjecFCMToken: StringRequest = object : StringRequest(Method.POST, UrlConstant.GenerateFCMToken,
            Response.Listener {

                try {
                    var jsonOBject= JSONObject(it)
                    if (jsonOBject.getBoolean("success")){

                    }
                } catch(ex: Exception){
                    Toast.makeText(mActivity, "fail to generate FCM token"+ex.message, Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(mActivity, "Volley Error FCM token"+it, Toast.LENGTH_SHORT).show()
            }){




            override fun getHeaders(): MutableMap<String, String> {
                var Header:HashMap<String,String> = HashMap()
                Header["Authorization"] = "Bearer " + token
                return Header
            }




            override fun getParams(): MutableMap<String, String> {
                var Params:HashMap<String,String> = HashMap()
                Params["shop_id"]=shop_id.toString()
                Params["fcm_token"]=fcm_token
                return Params
            }
        }
        queue.add(JsonObjecFCMToken)
    }

}