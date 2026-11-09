package com.example.foodorder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodorder.data.FoodData
import com.example.foodorder.model.Food
import com.example.foodorder.viewmodel.FoodOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: FoodOrderViewModel,
    onFoodClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onAdd: (Food) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("FoodOrder", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = onCartClick) {
                    BadgedBox(badge = {
                        if (vm.cartCount > 0) Badge { Text(vm.cartCount.toString()) }
                    }) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Keranjang")
                    }
                }
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 700.dp)
                .weight(1f)
                .align(Alignment.CenterHorizontally),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(
                            "Halo, Selamat Datang 👋",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            "Pesan makanan favoritmu!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = vm.searchQuery,
                    onValueChange = { vm.searchQuery = it },
                    placeholder = { Text("Cari makanan...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(FoodData.categories) { category ->
                        FilterChip(
                            selected = vm.selectedCategory == category,
                            onClick = { vm.selectedCategory = category },
                            label = { Text(category) }
                        )
                    }
                }
            }
            item {
                Text("Rekomendasi", style = MaterialTheme.typography.titleLarge)
            }
            if (vm.filteredFoods.isEmpty()) {
                item {
                    Text("Makanan tidak ditemukan", style = MaterialTheme.typography.bodyLarge)
                }
            }
            items(vm.filteredFoods, key = { it.id }) { food ->
                FoodCard(
                    food = food,
                    onClick = { onFoodClick(food.id) },
                    onAdd = { onAdd(food) }
                )
            }
        }
    }
}