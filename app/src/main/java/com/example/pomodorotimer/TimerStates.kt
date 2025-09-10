package com.example.pomodorotimer

enum class TimerStates {
    POMODORO {
        override fun getColor() = R.color.pomodoro_red
    },
    SHORT_BREAK {
        override fun getColor() = R.color.short_break_green
    },
    LONG_BREAK {
        override fun getColor() = R.color.long_break_blue
    };

    abstract fun getColor(): Int
}