package com.example.pomodorotimer.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.example.pomodorotimer.units.ChartViewMode
import com.example.pomodorotimer.viewModel.RecordViewModel
import java.time.LocalDate


@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    recordViewModel: RecordViewModel
){
    ChartViewWithSegmentedButton(modifier = modifier, viewModel = recordViewModel)
}

@Composable
fun ChartViewWithSegmentedButton(
    modifier: Modifier,
    viewModel: RecordViewModel
){
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var selectedIndex by remember { mutableIntStateOf(0) }
        val options = enumValues<ChartViewMode>()
        val today = LocalDate.now()
        val chartMode by viewModel.chartViewMode.collectAsState()
        val chartData by viewModel.historicalData.collectAsState()
        var xList = viewModel.getXAxisData(chartMode, today)
        Log.i("HistoryScreen", "${chartData.size}")

        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = {
                        selectedIndex = index
                        viewModel.setChartViewMode(label)
                        xList = viewModel.getXAxisData(chartMode, today)
                        viewModel.fetchHistoricalData(chartMode, xList)
                    },
                    selected = index == selectedIndex,
                    label = { Text(label.name) }
                )
            }
        }

        ChartView(
            modifier = modifier.padding(20.dp),
            xList = if(chartMode.name == "WEEK"){
                xList.map { it.substring(5,10) }
            } else xList,
            yList = (if(chartData.isEmpty()) {
                List(xList.size){ 0.0 }
            } else{
                chartData.map{ it?.yValue ?: 0.0 }
            })
        )
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
            label = "Duration",
            data = yList,
            lineColor = Color(0xFFFF7F50),
            lineType = LineType.DEFAULT_LINE,
            lineShadow = true
        )
    )

    Box(Modifier) {
        LineChart(
            modifier = modifier,
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
            yAxisRange = 5,
            oneLineChart = false,
            gridOrientation = GridOrientation.GRID
        )
    }
}
