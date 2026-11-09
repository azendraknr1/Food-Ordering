package com.example.foodorder.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.foodorder.data.FoodData
import com.example.foodorder.model.CartItem
import com.example.foodorder.model.Food
import com.example.foodorder.model.Order

class FoodOrderViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")
    var selectedCategory by mutableStateOf("Semua")
    var isDarkTheme by mutableStateOf(false)
    var useDynamicColor by mutableStateOf(false)

    val cart = mutableStateListOf<CartItem>()
    val favorites = mutableStateListOf<Int>()
    val orders = mutableStateListOf<Order>()

    val filteredFoods: List<Food>
        get() = FoodData.foods.filter {
            (selectedCategory == "Semua" || it.category == selectedCategory) &&
                    it.name.contains(searchQuery, ignoreCase = true)
        }

    val cartCount: Int get() = cart.sumOf { it.quantity }
    val subtotal: Int get() = cart.sumOf { it.food.price * it.quantity }
    val deliveryFee: Int get() = if (cart.isEmpty()) 0 else 5000
    val total: Int get() = subtotal + deliveryFee

    fun addToCart(food: Food, quantity: Int = 1) {
        val index = cart.indexOfFirst { it.food.id == food.id }
        if (index >= 0) {
            cart[index] = cart[index].copy(quantity = cart[index].quantity + quantity)
        } else {
            cart.add(CartItem(food, quantity))
        }
    }

    fun updateQuantity(foodId: Int, delta: Int) {
        val index = cart.indexOfFirst { it.food.id == foodId }
        if (index < 0) return
        val newQuantity = cart[index].quantity + delta
        if (newQuantity <= 0) cart.removeAt(index)
        else cart[index] = cart[index].copy(quantity = newQuantity)
    }

    fun removeFromCart(foodId: Int) {
        cart.removeAll { it.food.id == foodId }
    }

    fun toggleFavorite(foodId: Int) {
        if (!favorites.remove(foodId)) favorites.add(foodId)
    }

    fun placeOrder(address: String, payment: String) {
        orders.add(0, Order(orders.size + 1, cart.toList(), total, address, payment))
        cart.clear()
    }
}