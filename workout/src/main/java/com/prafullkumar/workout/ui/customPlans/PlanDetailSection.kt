package com.prafullkumar.workout.ui.customPlans

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prafullkumar.workout.data.ExperienceLevel
import com.prafullkumar.workout.data.PlanType


@Composable
fun PlanDetailsSection(
    viewModel: com.prafullkumar.workout.ui.customPlans.CreatePlanViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Plan Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Plan Type Selector
            Text("Plan Type", style = MaterialTheme.typography.bodyLarge)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(PlanType.entries) { type ->
                    PlanTypeChip(
                        type = type,
                        selected = type == viewModel.newPlan.type,
                        onClick = {
                            viewModel.newPlan = viewModel.newPlan.copy(type = type)
                        }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Experience Level Selector
            Text("Experience Level", style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExperienceLevel.entries.forEach { experienceLevel ->
                    ExperienceLevelChip(
                        level = experienceLevel,
                        selected = experienceLevel == viewModel.newPlan.level,
                        onClick = {
                            viewModel.newPlan = viewModel.newPlan.copy(level = experienceLevel)
                        }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Frequency Selector
            Text("Frequency (days per week)", style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                (1..7).forEach { day ->
                    FrequencyChip(
                        day = day,
                        selected = day == viewModel.newPlan.frequency,
                        onClick = {
                            viewModel.newPlan = viewModel.newPlan.copy(frequency = day)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlanTypeChip(
    type: PlanType,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) Color(0xFF3F51B5).copy(alpha = 0.2f) else Color.Transparent
            )
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFF3F51B5) else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = type.getIcon(),
                fontSize = 20.sp
            )
            Text(
                text = type.name.lowercase().capitalize(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) Color(0xFF3F51B5) else Color.Gray
            )
        }
    }
}

@Composable
fun ExperienceLevelChip(
    level: ExperienceLevel,
    selected: Boolean,
    onClick: () -> Unit
) {
    val levelNames = mapOf(
        ExperienceLevel.BEGINNER to "Beginner",
        ExperienceLevel.INTERMEDIATE to "Intermediate",
        ExperienceLevel.ADVANCED to "Advanced"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) level.getColor().copy(alpha = 0.2f) else Color.Transparent
            )
            .border(
                width = 1.dp,
                color = if (selected) level.getColor() else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = levelNames[level] ?: level.name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) level.getColor() else Color.Gray
        )
    }
}

@Composable
fun FrequencyChip(
    day: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                if (selected) Color(0xFF3F51B5) else Color.Transparent
            )
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFF3F51B5) else Color.Gray,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) Color.White else Color.Gray
        )
    }
}