package com.example.onlinemarketserviceprovider.MyOrder.Modal

class Order(orderNumber:Int,orderBy:String,orderDate:String,phoneNumber:String,city:String,address:String) {
var OrderNumber:Int =orderNumber
var OrderBy:String = orderBy
var OrderDate:String = orderDate
var Phone:String=phoneNumber
var City:String = city
var address:String = address
var OrderItems:List<OrderItem>? = null
}