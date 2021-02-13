package com.example.faircon.framework.presentation.ui.main.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.faircon.framework.presentation.components.MyOverlineText
import com.example.faircon.framework.presentation.components.MySlider
import com.example.faircon.framework.presentation.theme.FairconTheme

class ControllerFragment : Fragment() {

    val viewModel by viewModels<ControllerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FairconTheme(
                    darkTheme = true,
                    displayProgressBar = viewModel.shouldDisplayProgressBar.value
                ) {

                    val sliderValues = viewModel
                        .viewState
                        .observeAsState(viewModel.getCurrentViewStateOrNew())
                        .value
                        .sliderValues

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {

                        Spacer(modifier = Modifier.height(60.dp))

                        ShowSlider(
                            name = "Fan Speed",
                            initialPosition = sliderValues.fanSpeed,
                            unit = "RPM",
                            valueRange = 300f..400f,
                            steps = 19,
                            onValueChangeEnd = { viewModel.setFanSpeed(it) }
                        )

                        ShowSlider(
                            name = "Temperature",
                            initialPosition = sliderValues.temperature,
                            unit = "C",
                            valueRange = 15f..25f,
                            steps = 9,
                            onValueChangeEnd = { viewModel.setTemperature(it) }
                        )

                        ShowSlider(
                            name = "Tec Voltage",
                            initialPosition = sliderValues.tecVoltage,
                            unit = "V",
                            valueRange = 0f..12f,
                            steps = 23,
                            onValueChangeEnd = { viewModel.setTecVoltage(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowSlider(
    name: String,
    initialPosition: Float,
    unit: String,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChangeEnd: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.Start
    ) {

        val currentValue = remember { mutableStateOf(initialPosition) }

        MyOverlineText(text = "$name : ${currentValue.value} $unit")

        MySlider(
            initialPosition = initialPosition,
            valueRange = valueRange,
            steps = steps,
            onValueChangeEnd = {
                onValueChangeEnd(it)
                currentValue.value = it
            }
        )
    }
}
