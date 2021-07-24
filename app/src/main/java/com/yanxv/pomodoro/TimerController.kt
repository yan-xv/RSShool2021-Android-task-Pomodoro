package com.yanxv.pomodoro

import android.text.Editable
import com.yanxv.pomodoro.model.Timer
import com.yanxv.pomodoro.model.TimerState


class TimerController(cbAlarm: ()->Unit): TimerViewHolder.Listener, CountDownTicker.Listener {

    val timerAdapter = TimerAdapter(this)

    private var countDownTicker: CountDownTicker? = CountDownTicker(this)
    private var timers = mutableListOf<Timer>()
    private var nextId = 0
    private val callbackFinish = cbAlarm

    var activeTimer: Timer? = null

    fun addNewTimer(valueMinute: Editable) {
        val minute = valueMinute.toString().toLongOrNull()
        val ms =  if (minute != null) minute * 60 * 1000 else 60 * 1000

        timers.add(Timer(nextId++, ms, TimerState.STOPPED, ms))
        timerAdapter.submitList(timers.toList())
    }

    override fun onTick(millisUntilFinished: Long) {
        //Log.i("onTick", "$millisUntilFinished")
        activeTimer?.let{
            it.currentMs = millisUntilFinished
            it.changeTimer()
        }
    }

    override fun onFinish() {
        activeTimer?.finish()
        callbackFinish()
    }

    override fun onStart(timer: Timer) {
        //Log.i("start()", "$id")
        activeTimer?.let {
            if (it.id != timer.id && it.state == TimerState.STARTED)
                onStop(it)
        }

        //Log.i("start()", "$it")
        countDownTicker?.startTimer(timer.currentMs, UNIT_TEN_MS)
        timer.start()
    }

    override fun onStop(timer: Timer) {
        //Log.i("stop", "$id")
        countDownTicker?.cancelTimer()
        timer.stop()
    }

    override fun onReset(timer: Timer) {
        //Log.i("reset", "$id")
        if (activeTimer?.id == timer.id)
            countDownTicker?.cancelTimer()
        timer.reset()
    }

    override fun onDelete(timer: Timer) {
        if (activeTimer?.id == timer.id)
            countDownTicker?.cancelTimer()
        timers.remove(timer)
        timerAdapter.submitList(timers.toList())
    }

    private fun Timer.changeTimer() {
       timerAdapter.notifyItemChanged(timers.indexOf(this))
    }

    private fun Timer.start() {
        activeTimer = this
        state = TimerState.STARTED
        changeTimer()
    }

    private fun Timer.stop() {
        state = TimerState.STOPPED
        changeTimer()
        activeTimer = null
    }

    private fun Timer.finish() {
        state = TimerState.FINISHED
        currentMs = startMs
        changeTimer()
        activeTimer = null
    }

    private fun Timer.reset() {
        state = TimerState.STOPPED
        currentMs = startMs
        changeTimer()
    }
}
