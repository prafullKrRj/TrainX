package com.prafullkumar.foodlog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prafullkumar.foodlog.FoodRoutes
import com.prafullkumar.foodlog.R
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class FoodScreens {
    Home, History
}

@Composable
fun FoodLogMain(
    navController: NavController
) {
    val mainViewModel = getViewModel<FoodLogMainViewModel>()
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = SimpleDateFormat(
                    "MMMM dd, yyyy",
                    Locale.getDefault()
                ).format(Date(System.currentTimeMillis())),
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(
                onClick = {
                    navController.navigate(FoodRoutes.FoodHistory)
                }
            ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.baseline_history_24),
                    contentDescription = "History"
                )
            }
        }
        FoodLogHome(mainViewModel, navController)
    }
}
