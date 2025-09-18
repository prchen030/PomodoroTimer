package com.example.pomodorotimer.units

import com.example.pomodorotimer.R

enum class TimerStates(val default: Int) {
    POMODORO(1) {
        override fun getColor() = R.color.pomodoro_red
    },
    SHORT_BREAK(1) {
        override fun getColor() = R.color.short_break_green
    },
    LONG_BREAK(1) {
        override fun getColor() = R.color.long_break_blue
    };

    abstract fun getColor(): Int
}