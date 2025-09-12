package com.example.pomodorotimer.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pomodorotimer.PrefKeys
import com.example.pomodorotimer.SharedDataViewModel
import com.example.pomodorotimer.ui.theme.PomodoroTimerTheme


@Composable
fun EditTextScreen(
    modifier: Modifier = Modifier,
    key: String,
    viewModel: SharedDataViewModel = viewModel()
){
    EditTextView(modifier = modifier, key = key, viewModel = viewModel)
}

@Composable
fun EditTextView(
    modifier: Modifier,
    key: String,
    viewModel: SharedDataViewModel
){
    val value = viewModel.getIntValueByKey(key)
    var text by remember { mutableStateOf(value.toString()) }
    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            viewModel.updateIntValue(key, text.toInt())
        },
        suffix = { Text(" min") },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(textAlign = TextAlign.Left),
    )
}

@Preview(showBackground = true)
@Composable
fun EditScreenPreview(){
    PomodoroTimerTheme {
        EditTextScreen(key = PrefKeys.KEY_POMODORO_TIME)
    }
}

