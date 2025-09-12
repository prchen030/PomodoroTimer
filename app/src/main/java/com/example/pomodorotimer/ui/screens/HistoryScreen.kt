package com.example.pomodorotimer.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.example.pomodorotimer.RecordViewModel


@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordViewModel = viewModel()
){
    ChartViewWithSegmentedButton(modifier = modifier, viewModel = viewModel)
}

@Composable
fun ChartViewWithSegmentedButton(
    modifier: Modifier,
    viewModel: RecordViewModel
){
    Column {

        var selectedIndex by remember { mutableIntStateOf(0) }
        val options = listOf("Day", "Month", "YEAR")
        //val recordsList by viewModel.getRecordsByOption(options[selectedIndex]).collectAsState()
        //var xList = records.map{it.date}
        //var yList = records.map{it.duration}

        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(label) }
                )
            }
        }

        /*ChartView(
            modifier = modifier,
            xList = xList,
            yList = yList,
        )*/
    }
}

@Composable
fun ChartView(
    modifier: Modifier,
    xList: List<String>,
    yList: List<Double>
){
    val testLineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = "Earnings",
            data = yList,
            lineColor = Color(0xFFFF7F50),
            lineType = LineType.DEFAULT_LINE,
            lineShadow = true
        )
    )

    Box(Modifier) {
        LineChart(
            modifier = modifier.fillMaxWidth(),
            linesParameters = testLineParameters,
            isGrid = true,
            gridColor = Color.Blue,
            xAxisData = xList,
            animateChart = true,
            showGridWithSpacer = true,
            yAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
            ),
            xAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W400
            ),
            yAxisRange = 6,
            oneLineChart = false,
            gridOrientation = GridOrientation.VERTICAL
        )
    }
}


/*
@Preview(showBackground = true)
@Composable
fun ChartViewPreview(){
    PomodoroTimerTheme {

    }
}*/
