package com.theapache64.klokk.composable

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Clock(
    needleOneDegree: Float = 270f,
    needleTwoDegree: Float = 0f,
    durationInMillis: Int = 500,
    delay: Int = 0,
    easing: Easing = LinearEasing,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colors.background,
    needleColor: Color =  colors.secondary
) {

    val needleOneRadian = (needleOneDegree * Math.PI / 180).toFloat()
    val needleTwoRadian = (needleTwoDegree * Math.PI / 180).toFloat()
    val animationSpec = tween<Float>(durationMillis = durationInMillis, easing = easing, delayMillis = delay)

    val needleOneDegreeAnim by animateFloatAsState(
        needleOneRadian,
        animationSpec = animationSpec
    )

    val needleTwoDegreeAnim by animateFloatAsState(
        needleTwoRadian,
        animationSpec = animationSpec,
    )

    Canvas(
        modifier = modifier
    ) {

        val needleWidth = size.minDimension * 0.05f

        // Background
        val radius = size.minDimension / 2f

        drawCircle(
            color= backgroundColor,
            radius = radius
        )

        // To make the needle origin rounded.
        drawCircle(
            color= backgroundColor,
            radius = needleWidth * 0.487f
        )

        val radius2 = radius - 4

        // Needle One
        drawLine(
            color= needleColor,
            start = center,
            end = Offset(
                // Finding end coordinate for the given radian
                x = center.x + radius2 * sin(needleOneDegreeAnim),
                y = center.y - radius2 * cos(needleOneDegreeAnim),
            ),
            strokeWidth = needleWidth
        )


        // Needle two
        drawLine(
            color= needleColor,
            start = center,
            end = Offset(
                // Finding end coordinate for the given degree
                x = center.x + radius2 * sin(needleTwoDegreeAnim),
                y = center.y - radius2 * cos(needleTwoDegreeAnim),
            ),
            strokeWidth = needleWidth
        )
    }

}


// Preview
fun main(args: Array<String>) {
    Window {
        var needleOneDegree by remember { mutableStateOf(135f) }
        var needleTwoDegree by remember { mutableStateOf(225f) }


        val scope = rememberCoroutineScope()

        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black)
        ) {

            Clock(
                needleOneDegree = needleOneDegree,
                needleTwoDegree = needleTwoDegree,
                modifier = Modifier.size(600.dp),
                durationInMillis = 2000
            )

            Button(
                onClick = {
                    needleOneDegree = 110f
                    needleTwoDegree = 170f

                    /* scope.launch {
                         delay(5000)
                         needleOneDegree = (0..360).random().toFloat()
                         needleTwoDegree = (0..360).random().toFloat()
                     }*/
                }
            ) {
                Text(text = "Animate")
            }
        }
    }
}
