package com.example.foodorder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorder.model.CartItem
import com.example.foodorder.viewmodel.FoodOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(vm: FoodOrderViewModel, onBack: () -> Unit, onCheckout: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Keranjang") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
            }
        )
        if (vm.cart.isEmpty()) {
            EmptyState(
                emoji = "🛒",
                message = "Keranjang masih kosong",
                modifier = Modifier.weight(1f)
            )
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
                items(vm.cart, key = { it.food.id }) { item ->
                    CartRow(
                        item = item,
                        onMinus = { vm.updateQuantity(item.food.id, -1) },
                        onPlus = { vm.updateQuantity(item.food.id, 1) },
                        onDelete = { vm.removeFromCart(item.food.id) }
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 700.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SummaryRow("Subtotal", rupiah(vm.subtotal))
                    SummaryRow("Ongkir", rupiah(vm.deliveryFee))
                    HorizontalDivider(Modifier.padding(vertical = 4.dp))
                    SummaryRow("Total", rupiah(vm.total), bold = true)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth()) {
                        Text("Checkout")
                    }
                }
            }
        }
    }
}

@Composable
private fun CartRow(item: CartItem, onMinus: () -> Unit, onPlus: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EmojiBox(emoji = item.food.emoji, modifier = Modifier.size(64.dp), fontSize = 32.sp)
            Column(Modifier.weight(1f)) {
                Text(item.food.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    rupiah(item.food.price),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onMinus) {
                        Icon(Icons.Filled.Remove, contentDescription = "Kurangi")
                    }
                    Text(item.quantity.toString(), style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = onPlus) {
                        Icon(Icons.Filled.Add, contentDescription = "Tambah")
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(vm: FoodOrderViewModel, onBack: () -> Unit, onOrderPlaced: () -> Unit) {
    var address by rememberSaveable { mutableStateOf("") }
    var payment by rememberSaveable { mutableStateOf("Transfer Bank") }
    var showConfirm by remember { mutableStateOf(false) }
    val methods = listOf("Transfer Bank", "E-Wallet (OVO, GoPay, DANA)", "QRIS")

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Checkout") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
            }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .widthIn(max = 600.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Alamat Pengiriman", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Alamat lengkap") },
                leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
            Text("Metode Pembayaran", style = MaterialTheme.typography.titleMedium)
            methods.forEach { method ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = payment == method,
                            onClick = { payment = method },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = payment == method, onClick = null)
                    Spacer(Modifier.width(12.dp))
                    Text(method, style = MaterialTheme.typography.bodyLarge)
                }
            }
            Text("Ringkasan Pesanan", style = MaterialTheme.typography.titleMedium)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    vm.cart.forEach {
                        SummaryRow("${it.food.name} x${it.quantity}", rupiah(it.food.price * it.quantity))
                    }
                    SummaryRow("Ongkir", rupiah(vm.deliveryFee))
                    HorizontalDivider(Modifier.padding(vertical = 4.dp))
                    SummaryRow("Total Pembayaran", rupiah(vm.total), bold = true)
                }
            }
        }
        Button(
            onClick = { showConfirm = true },
            enabled = address.isNotBlank() && vm.cart.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp)
        ) {
            Text("Buat Pesanan")
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Konfirmasi Pesanan") },
            text = { Text("Total ${rupiah(vm.total)} dengan pembayaran $payment. Lanjutkan?") },
            confirmButton = {
                Button(onClick = {
                    vm.placeOrder(address, payment)
                    showConfirm = false
                    onOrderPlaced()
                }) { Text("Ya, Pesan") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Batal") }
            }
        )
    }
}