package com.example.foodorder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorder.model.Food

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    food: Food,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAddToCart: (Int) -> Unit,
    onViewCart: () -> Unit
) {
    var quantity by rememberSaveable { mutableIntStateOf(1) }
    var showDialog by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
            },
            actions = {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorit"
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .widthIn(max = 600.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            EmojiBox(
                emoji = food.emoji,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                fontSize = 96.sp
            )
            Spacer(Modifier.height(16.dp))
            Text(food.name, style = MaterialTheme.typography.headlineSmall)
            Text(
                rupiah(food.price),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            Text(food.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(onClick = { if (quantity > 1) quantity-- }) {
                    Icon(Icons.Filled.Remove, contentDescription = "Kurangi")
                }
                Text(
                    quantity.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                FilledTonalIconButton(onClick = { quantity++ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Tambah")
                }
            }
        }
        Button(
            onClick = {
                onAddToCart(quantity)
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp)
        ) {
            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Add to Cart - ${rupiah(food.price * quantity)}")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Berhasil Ditambahkan!") },
            text = { Text("${food.name} telah ditambahkan ke keranjang.") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    onViewCart()
                }) { Text("Lihat Keranjang") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Lanjut Belanja") }
            }
        )
    }
}