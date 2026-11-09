package com.example.foodorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodorder.ui.FoodOrderApp
import com.example.foodorder.ui.theme.FoodOrderTheme
import com.example.foodorder.viewmodel.FoodOrderViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: FoodOrderViewModel = viewModel()
            FoodOrderTheme(darkTheme = vm.isDarkTheme, dynamicColor = vm.useDynamicColor) {
                FoodOrderApp(vm)
            }
        }
    }
}