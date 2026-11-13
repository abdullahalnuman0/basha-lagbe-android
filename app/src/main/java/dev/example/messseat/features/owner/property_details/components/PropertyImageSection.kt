package dev.example.messseat.features.owner.property_details.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import dev.example.messseat.R
import dev.example.messseat.core.utils.toImageUrl
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun PropertyImageSection(
    modifier: Modifier = Modifier,
    images: List<String>
) {

    var currentImgIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(images) {
        if (images.size <= 1) return@LaunchedEffect

        while (true) {
            delay(5.seconds)

            currentImgIndex = (currentImgIndex + 1) % images.size
        }
    }

  val image = images.getOrNull(currentImgIndex) ?: images[0]

    Box(
        modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        AnimatedContent(
            targetState = image,
            transitionSpec = {
                (
                        fadeIn(
                            animationSpec = tween(700)
                        ) + slideInHorizontally(
                            animationSpec = tween(700),
                            initialOffsetX = { it / 4 }
                        )
                        ).togetherWith(
                        fadeOut(
                            animationSpec = tween(500)
                        )
                    )
            },
            label = "room_transition"
        ) { targetImg ->
            AsyncImage(
                model = targetImg.toImageUrl(),
                contentDescription = targetImg,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = rememberAsyncImagePainter(
                    R.drawable.room_default_image
                ),
                error = rememberAsyncImagePainter(
                    R.drawable.room_default_image
                )
            )
        }

//        Button(onClick = {}) { Text(images.size.toString()) }
    }

}