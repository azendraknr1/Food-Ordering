package com.example.foodorder.data

import com.example.foodorder.model.Food

object FoodData {
    val categories = listOf("Semua", "Makanan", "Minuman", "Camilan", "Promo")

    val foods = listOf(
        Food(1, "Nasi Goreng Spesial", 18000, "Nasi goreng dengan bumbu spesial, telur, dan sayuran segar.", "Makanan", "🍳"),
        Food(2, "Ayam Geprek", 20000, "Ayam crispy dengan sambal geprek pedas dan nasi hangat.", "Makanan", "🍗"),
        Food(3, "Mie Ayam", 15000, "Mie kenyal dengan topping ayam kecap dan pangsit renyah.", "Makanan", "🍜"),
        Food(4, "Es Teh Manis", 5000, "Teh dingin yang menyegarkan dengan gula pas.", "Minuman", "🧋"),
        Food(5, "Es Jeruk", 7000, "Perasan jeruk segar dengan es batu.", "Minuman", "🍊"),
        Food(6, "Kopi Susu", 12000, "Kopi susu gula aren khas kekinian.", "Minuman", "☕"),
        Food(7, "Pisang Goreng", 10000, "Pisang goreng tepung krispi, disajikan hangat.", "Camilan", "🍌"),
        Food(8, "Kentang Goreng", 12000, "Kentang goreng renyah dengan saus sambal.", "Camilan", "🍟"),
        Food(9, "Paket Hemat Geprek", 25000, "Ayam geprek, nasi, dan es teh dalam satu paket hemat.", "Promo", "🎁")
    )
}