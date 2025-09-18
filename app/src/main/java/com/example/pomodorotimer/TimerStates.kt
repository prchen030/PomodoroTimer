package com.example.pomodorotimer

enum class TimerStates(val default: Int) {
    POMODORO(25) {
        override fun getColor() = R.color.pomodoro_red
    },
    SHORT_BREAK(5) {
        override fun getColor() = R.color.short_break_green
    },
    LONG_BREAK(15) {
        override fun getColor() = R.color.long_break_blue
    };

    abstract fun getColor(): Int
}