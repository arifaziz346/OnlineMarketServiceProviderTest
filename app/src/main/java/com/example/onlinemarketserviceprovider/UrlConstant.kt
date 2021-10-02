package com.example.onlinemarketserviceprovider

public class UrlConstant {
    companion object{
        val  URL:String ="http://192.168.43.193:8080/"
        val  RegisterShop:String =URL+"api/RegisterShop"
        val  LoginShop:String =URL+"api/loginShop"
        val  GetProduct:String = URL+"api/GetProduct?shop_id="
        val  GenerateFCMToken:String = URL+"api/GenerateFCMToken"
        val DashboardDetail:String=URL+"api/DashboardDetail?shop_id="
        val MyOrderProduct:String=URL+"api/MyOrderProduct?shop_id="
        val CancelOrder:String=URL+"api/CancelOrder"
        val TransferOrder:String=URL+"api/TransferOrder"
        val OderProductDetail:String=URL+"api/OderProductDetail"
        val CreateProduct:String=URL+"api/CreateProduct"
        val DeleteProduct:String=URL+"api/DeleteProduct"
        val EditProduct:String=URL+"api/EditProduct"
        val SearchMyProduct:String=URL+"api/SearchMyProduct"
        val GetShopDetail:String=URL+"api/GetShopDetail?shop_id="
        val UpdateProfile:String=URL+"api/UpdateProfile"
        val UpdateStock:String=URL+"api/UpdateStock"
        val GetTransferredOrder:String=URL+"api/GetTransferredOrder?shop_id="
        val GetMonthWiseSaleDetail:String=URL+"api/GetMonthWiseSaleDetail?shop_id="
        val GetExpense:String=URL+"api/GetExpense"
        val SearchExpense:String=URL+"api/SearchExpense"
        val CreateExpense:String=URL+"api/CreateExpense"

    }
}

