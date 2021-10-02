package com.example.onlinemarketserviceprovider.Dashbord.Model

class ExpenseModal(month:String, shopId:Int, expenseId:Int, amount:Int, description:String) {
    var shop_id:Int =shopId
    var expense_id:Int =expenseId
    var amount:Int =amount
    var description:String =description
    var month:String=month
}