package com.example.jetpacktraning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpacktraning.ui.theme.ButtonNavigationBar
import com.example.jetpacktraning.ui.theme.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF070417))
            ) {
                ButtonNavigationBar()

            }
        }
    }

    @Composable
    fun GetBottomBar(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.Task_Screen){
            composable(Routes.Task_Screen) {
                ButtonNavigationBar()
             }


        }
    }
}







