package com.example.pomodorotimer.data

data class YearMonthTotal(
    val year: Int,
    val month: Int,
    val total: Double
): YValueProvider{
    override val yValue: Double get() = total
}
