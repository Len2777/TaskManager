package com.example.jetpacktraning.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpacktraning.R




@Composable
fun ButtonNavigationBar() {
    val navController = rememberNavController()
    Scaffold(
        contentColor = Color.Transparent,
        containerColor = Color.Transparent,

        bottomBar = { Box(modifier = Modifier.height(85.dp).fillMaxWidth()){NavigationBar(navController)}  }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Task_Screen,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Task_Screen) { ManagerScreen() }
            composable(Routes.Plus_Screen) { AddNewActionSection() }  // <-- !!!
            composable(Routes.Search_Screen) { Text("LOL") }  // <-- !!!
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItemState(
            title = "Time",
            route = Routes.Task_Screen,
            selectedIcon = painterResource(id = R.drawable.time),
            unselectedIcon = painterResource(id = R.drawable.time),
            hasBadge = false,

        ),
        NavItemState(
            title = "Add",
            route = Routes.Plus_Screen,
            selectedIcon = painterResource(id = R.drawable.baseline_add_24),
            unselectedIcon = painterResource(id = R.drawable.baseline_add_24),
            hasBadge = false,

        ),
        NavItemState(
            title = "History",
            route = Routes.Search_Screen,
            selectedIcon = painterResource(id = R.drawable.pie_chart_filled),
            unselectedIcon = painterResource(id = R.drawable.pie_chart_filled),
            hasBadge = false,

        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF070417),
        contentColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {

                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.hasBadge) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            painter = if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFF5532A1)

                )
            )
        }
    }
}

data class NavItemState(
    val title: String,
    val route: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val hasBadge: Boolean,

)

