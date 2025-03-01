package com.prafullkumar.foodlog.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prafullkumar.foodlog.R
import com.prafullkumar.foodlog.domain.FoodType
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    addFoodViewModel: AddFoodViewModel,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add ${
                            addFoodViewModel.mealType.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            }
                        }"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {
            VegNonVegSection(addFoodViewModel)
            FoodNameSection(addFoodViewModel)
            CalorieAndMacrosSection(addFoodViewModel)
            SaveFoodEntry(viewModel = addFoodViewModel, navController = navController)
        }
    }
}

@Composable
fun NumberSection(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    labelType: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            modifier = Modifier,
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
        )
        Text(
            text = labelType,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun SaveFoodEntry(
    modifier: Modifier = Modifier,
    viewModel: AddFoodViewModel,
    navController: NavController
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Save Food Entry",
                fontSize = 18.sp
            )
            FilledTonalButton(
                onClick = {
                    viewModel.insertFoodLog()
                    navController.popBackStack()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_save_24),
                    contentDescription = "Save"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save")
            }
        }
    }
}

@Composable
fun CalorieAndMacrosSection(
    viewModel: AddFoodViewModel
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NumberSection(
                modifier = Modifier.fillMaxWidth(),
                label = "0",
                value = viewModel.newFoodItem.calories.toString(),
                labelType = "Calories(kcal)",
                onValueChange = {
                    viewModel.newFoodItem = viewModel.newFoodItem.copy(calories = it)
                }
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NumberSection(
                    modifier = Modifier.weight(0.3f),
                    label = "0",
                    value = viewModel.newFoodItem.protein.toString(),
                    labelType = "Protein(g)",
                    onValueChange = {
                        viewModel.newFoodItem = viewModel.newFoodItem.copy(protein = it)
                    }
                )
                NumberSection(
                    modifier = Modifier.weight(0.3f),
                    label = "0",
                    value = viewModel.newFoodItem.carbs.toString(),
                    labelType = "Carbs(g)",
                    onValueChange = {
                        viewModel.newFoodItem = viewModel.newFoodItem.copy(carbs = it)
                    }
                )
                NumberSection(
                    modifier = Modifier.weight(0.3f),
                    label = "0",
                    value = viewModel.newFoodItem.fats.toString(),
                    labelType = "Fat(g)",
                    onValueChange = {
                        viewModel.newFoodItem = viewModel.newFoodItem.copy(fats = it)
                    }
                )
            }
        }
    }
}

@Composable
fun FoodNameSection(
    viewModel: AddFoodViewModel
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(25)
            ),
        value = viewModel.newFoodItem.foodName,
        onValueChange = { viewModel.newFoodItem = viewModel.newFoodItem.copy(foodName = it) },
        label = { Text("Food Name") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
            Icon(Icons.Default.Edit, contentDescription = "Food Name")
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun VegNonVegSection(
    addFoodViewModel: AddFoodViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth(), space = 6.dp) {
            FoodType.entries.forEachIndexed { index, type ->
                SegmentedButton(
                    shape = RoundedCornerShape(50),
                    onClick = {
                        addFoodViewModel.newFoodItem = addFoodViewModel.newFoodItem.copy(
                            foodType = type.name
                        )
                    },
                    selected = addFoodViewModel.newFoodItem.foodType == type.name
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (type == FoodType.VEG) ImageVector.vectorResource(
                                R.drawable.baseline_grass_24
                            ) else ImageVector.vectorResource(
                                R.drawable.baseline_restaurant_24
                            ),
                            contentDescription = type.name
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = type.name.lowercase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
                    }
                }
            }
        }
    }
}