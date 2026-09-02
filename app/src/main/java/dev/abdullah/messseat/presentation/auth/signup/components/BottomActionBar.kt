package dev.abdullah.messseat.presentation.auth.signup.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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

@Composable
internal fun BottomActionBar(
    modifier: Modifier = Modifier,
    primaryBtnText: String,
    suggestBtnHintText: String = "Already have an account? ",
    suggestBtnText: String = "Login",
    enabled: Boolean = true,
    showSuggestBtnHint: Boolean = false,
    showBackButton: Boolean = true,
    isLoading: Boolean,
    onPrimaryClick: () -> Unit,
    onBackClick: () -> Unit = {},
    onSuggestBtnClick: () -> Unit
) {


    val loginHiltPadding by animateDpAsState(
        targetValue = if (showSuggestBtnHint) 16.dp else 0.dp,
        animationSpec = tween(200),
        label = "loginHint"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (showBackButton) {
                    OutlinedButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .height(56.dp)
                            .weight(0.35f),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                Button(
                    onClick = onPrimaryClick,
                    enabled = enabled,
                    modifier = Modifier
                        .height(56.dp)
                        .weight(if (showBackButton) 0.65f else 1f),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 1.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        } else {
                            Text(
                                text = primaryBtnText,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showSuggestBtnHint,
                modifier = Modifier.padding(top = loginHiltPadding)
            ) {

//                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = suggestBtnHintText,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = suggestBtnText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable(onClick = onSuggestBtnClick)
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }


}