package com.example.mnewsapp.presentation.components
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeShimmerEffect(
    modifier: Modifier = Modifier,
    shimmerColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {

    val shimmerColors = listOf(
        shimmerColor.copy(alpha = 0.6f),
        shimmerColor.copy(alpha = 0.2f),
        shimmerColor.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart
        ),
        label = "Shimmer Loading animation"
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    Column(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        Spacer(modifier = Modifier.fillMaxWidth()
            .height(47.dp))

        Box(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brush)
                .height(242.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.fillMaxWidth()
            .height(25.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(242.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        )
        {


            items(count = 5){

                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(brush)
                        .fillMaxHeight()


                )

            }


        }







    }

//    Box(
//        modifier = modifier
//    ) {
//
//    }
}

@Preview
@Composable
private fun PreviewShimmerEffect() {

    HomeShimmerEffect(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}