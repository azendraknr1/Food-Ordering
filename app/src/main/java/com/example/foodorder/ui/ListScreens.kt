package com.example.foodorder.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.foodorder.data.FoodData
import com.example.foodorder.model.Food
import com.example.foodorder.viewmodel.FoodOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(vm: FoodOrderViewModel) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Pesanan") })
        if (vm.orders.isEmpty()) {
            EmptyState(emoji = "🧾", message = "Belum ada pesanan", modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .widthIn(max = 700.dp)
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(vm.orders, key = { it.id }) { order ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Pesanan #${order.id}", style = MaterialTheme.typography.titleMedium)
                            order.items.forEach {
                                SummaryRow("${it.food.name} x${it.quantity}", rupiah(it.food.price * it.quantity))
                            }
                            HorizontalDivider(Modifier.padding(vertical = 4.dp))
                            SummaryRow("Total", rupiah(order.total), bold = true)
                            Text("Alamat: ${order.address}", style = MaterialTheme.typography.bodyMedium)
                            Text("Pembayaran: ${order.payment}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    vm: FoodOrderViewModel,
    onFoodClick: (Int) -> Unit,
    onAdd: (Food) -> Unit
) {
    val favoriteFoods = FoodData.foods.filter { it.id in vm.favorites }
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Favorit") })
        if (favoriteFoods.isEmpty()) {
            EmptyState(emoji = "💜", message = "Belum ada makanan favorit", modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .widthIn(max = 700.dp)
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteFoods, key = { it.id }) { food ->
                    FoodCard(food = food, onClick = { onFoodClick(food.id) }, onAdd = { onAdd(food) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(vm: FoodOrderViewModel) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Tema Aplikasi") })
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .widthIn(max = 600.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ThemeRow(
                title = "Mode Gelap",
                subtitle = if (vm.isDarkTheme) "Dark Mode (Gelap)" else "Light Mode (Terang)",
                checked = vm.isDarkTheme,
                enabled = true,
                onCheckedChange = { vm.isDarkTheme = it }
            )
            ThemeRow(
                title = "Warna Dinamis",
                subtitle = "Mengikuti wallpaper (Android 12+)",
                checked = vm.useDynamicColor,
                enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                onCheckedChange = { vm.useDynamicColor = it }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary
                ).forEach { color ->
                    Box(
                        Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color)
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
        }
    }
}