package dev.abdullah.messseat.core.presentation.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

interface ProgressStep {
    val label: String
}

@Composable
fun <T : ProgressStep> StepProgressIndicator(
    modifier: Modifier = Modifier,
    currentStep: T,
    steps: List<T>,
    hiddenStepCount: Int = 0,
) {
    val currentIndex = steps.indexOf(currentStep)
    val totalSteps = steps.size + hiddenStepCount

    if (currentIndex == -1 || totalSteps == 0) return

    val currentStepNumber = currentIndex + 1 + hiddenStepCount


    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Step $currentStepNumber of $totalSteps",
                style = typos.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )
            )

            if (currentStep.label.isNotEmpty())
                Text(
                    text = currentStep.label,
                    style = typos.labelSmall.copy(
                        color = colors.onBackground.copy(0.5f),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.4.sp
                    )
                )

        }

        Spacer(Modifier.height(8.dp))

        // --- progress nodes: step 2 to 5

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            for (i in steps.indices) {
                val isCompleted = i < currentIndex
                val isCurrent = i == currentIndex

                val dotColor by animateColorAsState(
                    targetValue = when {
                        isCompleted || isCurrent -> colors.primary
                        else -> colors.onBackground.copy(0.5f)
                    },
                    animationSpec = tween(300),
                    label = "dotColor"
                )

                // --- step dot ---
                Box(
                    modifier = Modifier
                        .size(if (isCurrent) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                        .border(
                            width = if (isCurrent) 2.dp else 0.dp,
                            color = if (isCurrent) colors.primaryContainer else Color.Transparent,
                            shape = CircleShape
                        )
                )

                // --- connecting line ---
                if (i < steps.lastIndex) {
                    val lineProgress by animateFloatAsState(
                        targetValue = if (i < currentIndex) 1f else 0f,
                        animationSpec = tween(300),
                        label = "lineProgress"
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(colors.onBackground.copy(0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(lineProgress)
                                .height(3.dp)
                                .background(colors.primary)
                        )
                    }
                }
            }

        }

    }
}