package com.yanxv.pomodoro.model

enum class TimerState { STOPPED, STARTED, FINISHED }

data class Timer(
    val id: Int,
    var currentMs: Long,
    var state: TimerState,
    var startMs: Long = 0
)