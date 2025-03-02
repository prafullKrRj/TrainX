package com.prafullkumar.foodlog.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prafullkumar.foodlog.FoodRoutes
import com.prafullkumar.foodlog.domain.MealType

@Composable
fun FoodLogHome(mainViewModel: FoodLogMainViewModel, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CalorieDetails(viewModel = mainViewModel)
        }
        items(MealType.entries, key = {
            it.name
        }) { mealType ->
            MealTypeSection(
                mealType = mealType, viewModel = mainViewModel, navController
            )
        }
    }
}

@Composable
fun MealTypeSection(
    mealType: MealType, viewModel: FoodLogMainViewModel, navController: NavController
) {
    val mealCalories by viewModel.todayFoods.collectAsState()
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(), onClick = {

        }, colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = mealType.icon),
                        contentDescription = mealType.name
                    )
                    Text(
                        text = mealType.name
                    )
                }
                Text("${
                    mealCalories.filter { it.mealType == mealType.name }.sumOf { it.calories }
                } kcal")
            }
            FilledIconButton(
                onClick = {
                    navController.navigate(FoodRoutes.AddFood(mealType.name))
                }, shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = mealType.name
                )
            }
        }
    }
}

@Composable
fun CalorieDetails(
    viewModel: FoodLogMainViewModel
) {
    val currentEatenCalories by viewModel.currentEatenCalories.collectAsState()
    val todayFoods by viewModel.todayFoods.collectAsState()
    val tdeeData by viewModel.tdeeData.collectAsState()
    Card(
        shape = RoundedCornerShape(32.dp), modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    AnnotatedString(
                        text = "\uD83D\uDD25"
                    ) + AnnotatedString(
                        text = "  Calories",
                    )
                )
                Row {
                    Text(
                        text = tdeeData.tdee.toString() + " kcal"
                    )
                }
            }
            CurrentEatenCaloriesBox(currentEatenCalories)

            CurrentMacrosSection(todayFoods.sumOf { it.carbs }, todayFoods.sumOf { it.protein }, todayFoods.sumOf { it.fats })
        }
    }
}

@Composable
private fun CurrentMacrosSection(
    carbs: Int, protein: Int, fat: Int
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Macros"
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MacroBox("Carbs", carbs, Modifier.weight(1f))
            MacroBox("Protein", protein, Modifier.weight(1f))
            MacroBox("Fat", fat, Modifier.weight(1f))
        }
    }
}

@Composable
fun MacroBox(
    macroName: String, macroValue: Int, modifier: Modifier
) {
    Box(
        modifier
            .alpha(1f)
            .clip(CircleShape)
            .border(
                width = 2.dp, color = Color(0xFFD9DFC6), shape = CircleShape
            ), contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(99.dp)
                .clip(CircleShape), contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = macroName
                )
                Text(
                    text = AnnotatedString(
                        text = macroValue.toString(), spanStyle = SpanStyle(
                            fontWeight = FontWeight.Bold, fontSize = 32.sp
                        )
                    ) + AnnotatedString(
                        text = " g", spanStyle = SpanStyle(
                            fontWeight = FontWeight.Normal, fontSize = 16.sp
                        )
                    ), fontWeight = FontWeight.Bold, fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
private fun CurrentEatenCaloriesBox(
    currentEatenCalories: Int
) {
    Box(
        Modifier
            .size(104.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp, color = Color(0xFFD9DFC6), shape = CircleShape
            ), contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(4.dp)
                .clip(CircleShape), contentAlignment = Alignment.Center
        ) {
            Text(
                text = AnnotatedString(
                    text = currentEatenCalories.toString(), spanStyle = SpanStyle(
                        fontWeight = FontWeight.Bold, fontSize = 32.sp
                    )
                ) + AnnotatedString(
                    text = " kcal", spanStyle = SpanStyle(
                        fontWeight = FontWeight.Normal, fontSize = 16.sp
                    )
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
