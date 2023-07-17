package com.theapache64.klokk

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.theapache64.klokk.composable.BottomToolBar
import com.theapache64.klokk.composable.Clock
import com.theapache64.klokk.movement.alphabet.TextMatrixGenerator
import com.theapache64.klokk.movement.core.Movement
import com.theapache64.klokk.theme.KlokkTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// Configs
const val COLUMNS = 15
const val ROWS = 8
const val PADDING = 100
const val CLOCK_SIZE = 60
const val CLOCKS_CONTAINER_WIDTH = CLOCK_SIZE * COLUMNS
const val CLOCKS_CONTAINER_HEIGHT = CLOCK_SIZE * ROWS
const val ENJOY_TIME_IN_MILLIS = 1500L
const val IS_DEBUG = true

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Klokk",
        state = rememberWindowState(width = (CLOCKS_CONTAINER_WIDTH + PADDING).dp, height = (CLOCKS_CONTAINER_HEIGHT + PADDING + 40).dp),
    ) {


        /**
         * To control the infinite animation loop
         */
        val infiniteLoopScope = rememberCoroutineScope()

        // To hold and control movement transition
        var activeMovement by remember { mutableStateOf<Movement>(Movement.StandBy) }

        val themeState = remember { mutableStateOf(true) }

        // To control the auto playing animation
        var shouldPlayAutoAnim by remember { mutableStateOf(true) }

        var textInput by remember { mutableStateOf("") }

        // Generating degree matrix using the active movement
        val degreeMatrix = activeMovement.getMatrixGenerator().getVerifiedMatrix()

        KlokkTheme (isDark = themeState.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Building clock matrix
                repeat(ROWS) { i ->
                    Row {
                        repeat(COLUMNS) { j ->
                            val clockData = degreeMatrix[i][j]
                            Clock(
                                needleOneDegree = clockData.degreeOne,
                                needleTwoDegree = clockData.degreeTwo,
                                durationInMillis = activeMovement.durationInMillis,
                                modifier = Modifier.requiredSize(CLOCK_SIZE.dp),
                            )
                        }
                    }
                }

                // The animation loop
                LaunchedEffect(shouldPlayAutoAnim) {
                    println("Animation loop created and started -> will run? $shouldPlayAutoAnim")
                    val defaultWaitTime = activeMovement.durationInMillis.toLong() + ENJOY_TIME_IN_MILLIS
                    val mediumDelay = defaultWaitTime - ENJOY_TIME_IN_MILLIS

                    while (shouldPlayAutoAnim) {
                        delay(ENJOY_TIME_IN_MILLIS)

                        activeMovement = Movement.Trance(Movement.Trance.To.SQUARE) // Show square
                        delay(defaultWaitTime)

                        activeMovement = Movement.Trance(to = Movement.Trance.To.FLOWER) // Then flower
                        delay(mediumDelay) // flower doesn't have enjoy time

                        activeMovement = Movement.Trance(to = Movement.Trance.To.STAR) // To star, through circle (auto)
                        delay(mediumDelay)

                        activeMovement = Movement.Trance(to = Movement.Trance.To.FLY) // then fly
                        delay(defaultWaitTime)

                        activeMovement = Movement.Wave(Movement.Wave.State.START)
                        delay(mediumDelay)

                        activeMovement = Movement.Wave(Movement.Wave.State.END)
                        delay(mediumDelay)

                        // Ripple
                        activeMovement = Movement.Ripple(to = Movement.Ripple.To.START) // then ripple start
                        delay(defaultWaitTime)

                        activeMovement = Movement.Ripple(to = Movement.Ripple.To.END) // then ripple end
                        delay(defaultWaitTime)

                        // Time table
                        activeMovement = Movement.Ripple(to = Movement.Ripple.To.TIME_TABLE) // then ripple start
                        delay(defaultWaitTime)

                        // Ripple again
                        activeMovement = Movement.Ripple(to = Movement.Ripple.To.START) // then ripple start
                        delay(defaultWaitTime)

                        activeMovement = Movement.Ripple(to = Movement.Ripple.To.END) // then ripple end
                        delay(defaultWaitTime)

                        activeMovement = Movement.Time() // then show time
                        delay(defaultWaitTime)
                    }

                }

                BottomToolBar(
                    activeMovement = activeMovement,
                    isAnimPlaying = shouldPlayAutoAnim,

                    // TODO :WIP
                    textInput = textInput,
                    onTextInputChanged = { newInput ->
                        if (newInput.length <= TextMatrixGenerator.MAX_CHARS) {
                            textInput = newInput.trim().toUpperCase()

                            if (textInput.isEmpty()) {
                                // no text
                                shouldPlayAutoAnim = true
                                activeMovement = Movement.StandBy
                            } else {
                                // has some text
                                shouldPlayAutoAnim = false
                                activeMovement = Movement.Text(textInput)
                            }
                        }
                    },

                    onShowTimeClicked = {
                        shouldPlayAutoAnim = false // stop auto play
                        infiniteLoopScope.launch {
                            while (true) {
                                activeMovement = Movement.Time() // then show time
                                delay(activeMovement.durationInMillis.toLong())
                            }
                        }
                    },
                    onPlayClicked = {
                        shouldPlayAutoAnim = true
                        infiniteLoopScope.cancel()
                    },
                    onStopClicked = {
                        shouldPlayAutoAnim = false
                        activeMovement = Movement.StandBy
                    },
                    theme = themeState
                )
            }
        }
    }
}




