package com.example.foodorder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navArgument
import com.example.foodorder.data.FoodData
import com.example.foodorder.viewmodel.FoodOrderViewModel
import kotlinx.coroutines.launch

object Routes {
    const val HOME = "home"
    const val ORDERS = "orders"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val DETAIL = "detail/{foodId}"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    fun detail(id: Int) = "detail/$id"
}

private data class NavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun FoodOrderApp(vm: FoodOrderViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val items = listOf(
        NavItem(Routes.HOME, "Beranda", Icons.Filled.Home),
        NavItem(Routes.ORDERS, "Pesanan", Icons.AutoMirrored.Filled.ReceiptLong),
        NavItem(Routes.FAVORITES, "Favorit", Icons.Filled.Favorite),
        NavItem(Routes.PROFILE, "Profil", Icons.Filled.Person)
    )
    val showNav = items.any { it.route == currentRoute }

    val goTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    val addWithSnackbar: (com.example.foodorder.model.Food) -> Unit = { food ->
        vm.addToCart(food)
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar("${food.name} ditambahkan ke keranjang")
        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val wide = maxWidth >= 600.dp
        Row(Modifier.fillMaxSize()) {
            if (wide && showNav) {
                NavigationRail {
                    Spacer(Modifier.weight(1f))
                    items.forEach { item ->
                        NavigationRailItem(
                            selected = currentRoute == item.route,
                            onClick = { goTo(item.route) },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                    Spacer(Modifier.weight(1f))
                }
            }
            Scaffold(
                modifier = Modifier.weight(1f),
                contentWindowInsets = WindowInsets.navigationBars,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (!wide && showNav) {
                        NavigationBar {
                            items.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = { goTo(item.route) },
                                    icon = { Icon(item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) }
                                )
                            }
                        }
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Routes.HOME,
                    modifier = Modifier.padding(padding)
                ) {
                    composable(Routes.HOME) {
                        HomeScreen(
                            vm = vm,
                            onFoodClick = { navController.navigate(Routes.detail(it)) },
                            onCartClick = { navController.navigate(Routes.CART) },
                            onAdd = addWithSnackbar
                        )
                    }
                    composable(Routes.ORDERS) { OrdersScreen(vm) }
                    composable(Routes.FAVORITES) {
                        FavoritesScreen(
                            vm = vm,
                            onFoodClick = { navController.navigate(Routes.detail(it)) },
                            onAdd = addWithSnackbar
                        )
                    }
                    composable(Routes.PROFILE) { ProfileScreen(vm) }
                    composable(
                        route = Routes.DETAIL,
                        arguments = listOf(navArgument("foodId") { type = NavType.IntType })
                    ) { entry ->
                        val id = entry.arguments?.getInt("foodId") ?: 0
                        val food = FoodData.foods.firstOrNull { it.id == id } ?: return@composable
                        DetailScreen(
                            food = food,
                            isFavorite = food.id in vm.favorites,
                            onBack = { navController.popBackStack() },
                            onToggleFavorite = {
                                vm.toggleFavorite(food.id)
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        if (food.id in vm.favorites) "Ditambahkan ke favorit"
                                        else "Dihapus dari favorit"
                                    )
                                }
                            },
                            onAddToCart = { vm.addToCart(food, it) },
                            onViewCart = { navController.navigate(Routes.CART) }
                        )
                    }
                    composable(Routes.CART) {
                        CartScreen(
                            vm = vm,
                            onBack = { navController.popBackStack() },
                            onCheckout = { navController.navigate(Routes.CHECKOUT) }
                        )
                    }
                    composable(Routes.CHECKOUT) {
                        CheckoutScreen(
                            vm = vm,
                            onBack = { navController.popBackStack() },
                            onOrderPlaced = {
                                navController.navigate(Routes.ORDERS) { popUpTo(Routes.HOME) }
                                scope.launch { snackbarHostState.showSnackbar("Pesanan berhasil dibuat!") }
                            }
                        )
                    }
                }
            }
        }
    }
}