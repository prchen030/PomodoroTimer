package com.example.pomodorotimer.data

data class DateTotal(
    val date: String,
    val total: Double
): YValueProvider{
    override val yValue: Double get() = total
}
