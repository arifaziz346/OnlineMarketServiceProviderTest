package com.example.onlinemarketserviceprovider.MyOrder.Modal

class OrderItem(productName:String,quantiy:Int,price:Int,discount:Int,color:String,size:String,orderNumber:Int) {
    var OrderNumber:Int=orderNumber
    var ProductName:String= productName
    var Quantiy:Int = quantiy
    var Price:Int=price
    var Total:Int?=null
    var Discount:Int=discount
    var Color:String=color
    var Size:String=size
}