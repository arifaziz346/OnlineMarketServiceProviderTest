package com.example.onlinemarketserviceprovider.Products.Modal

class Product(
    productId:Int,
    shopId:Int,
    productName:String,
    color:String,
    description:String,
    quantity:Int,
    size:String,
    sellPrice:Int,
    costPrice:Int,
    discount:Int,
    productPhotos: ArrayList<String>
) {
    var ProductID:Int=productId
    var ShopId:Int=shopId
    var ProductName:String=productName
    var Color:String=color
    var Description:String=description
    var Quantity:Int=quantity
    var Discount:Int=discount
    var Size:String=size
    var SellPrice:Int=sellPrice
    var CostPrice:Int=costPrice
    var Product_Photos:ArrayList<String> = productPhotos
}