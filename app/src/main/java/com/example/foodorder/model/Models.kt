package com.example.foodorder.model

data class Food(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    val category: String,
    val emoji: String
)

data class CartItem(val food: Food, val quantity: Int)

data class Order(
    val id: Int,
    val items: List<CartItem>,
    val total: Int,
    val address: String,
    val payment: String
)