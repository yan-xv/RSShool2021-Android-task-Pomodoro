package com.yanxv.pomodoro

import android.os.CountDownTimer

class CountDownTicker(private val listener: Listener) {
    private var ticker: CountDownTimer? = null

    fun cancelTimer() = ticker?.cancel()

    fun startTimer(currentMs: Long, interval: Long) {
        ticker?.cancel()
        ticker = getCountDownTimer(currentMs, interval)
        ticker?.start()
    }

    private fun getCountDownTimer(currentMs: Long, interval: Long): CountDownTimer {
        return object : CountDownTimer(currentMs, interval) {
            override fun onTick(millisUntilFinished: Long) =
                listener.onTick(millisUntilFinished)
            override fun onFinish() = listener.onFinish()
        }
    }

    interface Listener {
        fun onTick(millisUntilFinished: Long)
        fun onFinish()
    }
}