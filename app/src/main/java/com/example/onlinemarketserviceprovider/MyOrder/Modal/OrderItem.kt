package com.example.onlinemarketserviceprovider.MyOrder.Modal

class OrderItem(productName:String,quantiy:Int,price:Int) {
    var ProductName:String= productName
    var Quantiy:Int = quantiy
    var Price:Int=price
    var Total:Int?=null
}