package com.prafullkumar.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.prafullkumar.common.domain.enums.ActivityLevel
import com.prafullkumar.common.domain.enums.Gender
import com.prafullkumar.common.domain.enums.Goal
import com.prafullkumar.common.domain.model.UserData
import com.tdee.profile.R
import org.koin.androidx.compose.getViewModel

@Composable
fun ProfileScreen(
    navController: NavController
) {
    val viewModel = getViewModel<ProfileViewModel>()
    ProfileScreenContent(viewModel, navController)
}

@Composable
fun ProfileScreenContent(
    viewModel: ProfileViewModel,
    navController: NavController
) {
    val userData by viewModel.userInfo.collectAsState()
    if (userData.isNullOrEmpty()) {
        Text(text = "No Data")
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(Modifier.fillMaxSize()) {
                Text(text = "ME", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                ProfileLoginHeader(viewModel, userData!![0])
                Spacer(modifier = Modifier.height(16.dp))
                SettingsCard(navController, viewModel, userData!![0])
                Spacer(modifier = Modifier.height(16.dp))
                if (viewModel.userLoggedIn) {
                    FilledTonalButton(onClick = viewModel::logout) {
                        Text("Logout")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCard(navController: NavController, viewModel: ProfileViewModel, userInfo: UserData) {

    var showGoalSelectionDialog by remember { mutableStateOf(false) }
    var showActivitySelectionDialog by remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(
            16.dp
        )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Settings", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)

            GoalAndActivityCard(
                title = "Goal",
                value = Goal.valueOf(userInfo.userGoal).value
            ) {
                showGoalSelectionDialog = true
            }
            GoalAndActivityCard(
                title = "Activity Level",
                value = ActivityLevel.valueOf(userInfo.userActivityLevel).value
            ) {
                showActivitySelectionDialog = true
            }

            PersonalInfoRow(
                infoType = "Gender",
                value = Gender.valueOf(userInfo.userGender).value
            ) {
                viewModel.showGenderDialog = true

            }

            PersonalInfoRow(
                infoType = "Current Weight",
                value = userInfo.userWeight.toString() + " kg"
            ) {
                viewModel.showWeightDialog = true

            }

            PersonalInfoRow(
                infoType = "Height",
                value = userInfo.userHeight.toDouble().toString() + " cm"
            ) {
                viewModel.showHeightDialog = true
            }
        }
    }
    if (viewModel.showHeightDialog) {
        HeightInputDialog(
            currentHeight = userInfo.userHeight.toDouble(),
            onDismiss = { viewModel.showHeightDialog = false },
            onHeightUpdated = { newHeight ->
                viewModel.updateUserHeight(newHeight)
                viewModel.showHeightDialog = false
            }
        )
    }
    if (viewModel.showWeightDialog) {
        WeightInputDialog(
            currentWeight = userInfo.userWeight.toFloat(),
            onDismiss = { viewModel.showWeightDialog = false },
            onWeightUpdated = { newWeight ->
                viewModel.updateUserWeight(newWeight)
                viewModel.showWeightDialog = false
            }
        )
    }
    if (viewModel.showGenderDialog) {
        GenderSelectionDialog(
            onDismiss = { viewModel.showGenderDialog = false },
            onGenderSelected = { gender ->
                viewModel.updateUserGender(gender)
                viewModel.showGenderDialog = false
            }
        )
    }
    if (showGoalSelectionDialog) {
        GoalAndActivitySelectionDialog(
            currentIdx = Goal.entries.indexOfFirst { it.name == userInfo.userGoal },
            values = Goal.entries.map { it.value },
            title = "Goal"
        ) { index ->
            viewModel.updateUserGoal(Goal.entries[index].name)
            showGoalSelectionDialog = false
        }
    }
    if (showActivitySelectionDialog) {
        GoalAndActivitySelectionDialog(
            currentIdx = ActivityLevel.entries.indexOfFirst { it.name == userInfo.userActivityLevel },
            values = ActivityLevel.entries.map { it.value },
            title = "Activity Level"
        ) { index ->
            viewModel.updateUserActivityLevel(ActivityLevel.entries[index].name)
            showActivitySelectionDialog = false
        }
    }
}

@Composable
fun GoalAndActivityCard(
    title: String,
    value: String,
    onClick: () -> Unit
) {

    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(title)
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(value, fontWeight = FontWeight.Bold)
                IconButton(onClick = {

                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Edit"
                    )
                }
            }
        }
    }
}
@Composable
fun GoalAndActivitySelectionDialog(
    currentIdx: Int,
    values: List<String>,
    title: String,
    onClick: (Int) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(currentIdx) }
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Select $title") },
        text = {
            Column {

                values.forEachIndexed { index, value ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index }
                        )
                        Text(
                            text = value,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedIndex >= 0) onClick(selectedIndex)
                },
                enabled = selectedIndex >= 0
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PersonalInfoRow(
    infoType: String,
    value: String,
    onClick: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(infoType, fontWeight = W400)
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(value)
                IconButton(onClick = onClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Change Info"
                    )
                }
            }
        }
    }
}

@Composable
fun GenderSelectionDialog(
    onDismiss: () -> Unit,
    onGenderSelected: (Gender) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Gender") },
        text = {
            Column {
                Gender.entries.forEach { gender ->
                    TextButton(
                        onClick = { onGenderSelected(gender) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(gender.value)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun WeightInputDialog(
    currentWeight: Float,
    onDismiss: () -> Unit,
    onWeightUpdated: (Float) -> Unit
) {
    var weightInput by remember {
        mutableStateOf(currentWeight.toString())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Weight") },
        text = {
            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it.replace(',', '.') },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    weightInput.toFloatOrNull()?.let { onWeightUpdated(it) }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun HeightInputDialog(
    currentHeight: Double,
    onDismiss: () -> Unit,
    onHeightUpdated: (Double) -> Unit
) {
    var heightInput by remember {
        mutableStateOf(currentHeight.toString())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Height") },
        text = {
            OutlinedTextField(
                value = heightInput,
                onValueChange = { heightInput = it.replace(',', '.') },
                label = { Text("Height (cm)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    heightInput.toDoubleOrNull()?.let { onHeightUpdated(it) }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
@Composable
fun ProfileLoginHeader(
    viewModel: ProfileViewModel,
    userInfo: UserData
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (viewModel.userLoggedIn) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(viewModel.user?.photoUrl)
                        .build(),
                    contentDescription = "Profile Picture",
                    alpha = 1f,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .alpha(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userInfo.userName.first().uppercase(),
                        fontSize = 25.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = userInfo.userName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (viewModel.userLoggedIn) {
                    Text("Last Synced")
                }
            }
            IconButton(onClick = viewModel::syncData) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_sync_24),
                    contentDescription = "Logout"
                )
            }
        }
    }
}