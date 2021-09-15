package com.example.onlinemarketserviceprovider

public class Constant {
    companion object{
        val  URL:String ="http://192.168.43.193:8080/"
        val  RegisterShop:String =URL+"api/RegisterShop"
        val  LoginShop:String =URL+"api/loginShop"
        val DashboardDetail:String=URL+"api/DashboardDetail?shop_id="
        val MyOrderProduct:String=URL+"api/MyOrderProduct?shop_id="
    }
}

