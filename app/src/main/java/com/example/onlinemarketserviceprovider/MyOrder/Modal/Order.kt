package com.example.onlinemarketserviceprovider.MyOrder.Modal

class Order(orderNumber:Int,orderBy:String,orderDate:String,phoneNumber:String,city:String,address:String,paymentType:Int,
            orderItem:ArrayList<OrderItem>) {
var OrderNumber:Int =orderNumber
var OrderBy:String = orderBy
var OrderDate:String = orderDate
var Phone:String=phoneNumber
var City:String = city
var Address:String = address
var PaymentType:Int =paymentType
    //Get Details of Product
var ItemDetail:ArrayList<OrderItem> =orderItem
}