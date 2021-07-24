package com.yanxv.pomodoro

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.yanxv.pomodoro.databinding.TimerItemBinding
import com.yanxv.pomodoro.model.Timer
import com.yanxv.pomodoro.model.TimerState


class TimerViewHolder(
    private val binding: TimerItemBinding,
    private val listener: Listener,
    private val resources: Resources
): RecyclerView.ViewHolder(binding.root) {

    private var bStarted = false
    fun bind(timer: Timer) {
        binding.timer.text = timer.currentMs.displayTime()
        binding.progressBarCircular.setPeriod(timer.startMs)
        binding.progressBarCircular.setCurrent(timer.currentMs)
        when(timer.state) {
            TimerState.STARTED->startTimer()
            TimerState.FINISHED->stopTimer(true)
            else->stopTimer(false)

        }
        initButtonsListeners(timer)
    }

    private fun initButtonsListeners(timer: Timer) {
        with(binding) {
            startPauseButton.setOnClickListener {
                if (timer.state == TimerState.STARTED) {
                    listener.onStop(timer)
                } else {
                    listener.onStart(timer)
                }
            }

            restartButton.setOnClickListener { listener.onReset(timer) }
            deleteButton.setOnClickListener { listener.onDelete(timer) }
        }
    }

    private fun setColorView(color: Int) {
        with(binding) {
            itemLayout.setBackgroundColor(color)
            startPauseButton.setBackgroundColor(color)
            restartButton.setBackgroundColor(color)
            deleteButton.setBackgroundColor(color)
            progressBarCircular.setColorCenter(color)
        }
    }

    private fun startTimer() {
        val drawable = resources.getDrawable(R.drawable.ic_baseline_pause_24, null)
        with(binding) {
            startPauseButton.setImageDrawable(drawable)
            blinkingIndicator.isInvisible = false
            if (!bStarted)
                (blinkingIndicator.background as? AnimationDrawable)?.start()
            progressBarCircular.setFinished(false)
        }

        setColorView(Color.WHITE)

        bStarted = true;
    }

    private fun stopTimer(bFinished: Boolean) {
        val drawable = resources.getDrawable(R.drawable.ic_baseline_play_arrow_24, null)
        with(binding) {
            startPauseButton.setImageDrawable(drawable)
            blinkingIndicator.isInvisible = true
            (blinkingIndicator.background as? AnimationDrawable)?.stop()
            if (bFinished)
                progressBarCircular.setFinished(true)
            else
                progressBarCircular.setFinished(false)
        }
        if (bFinished)
            setColorView(Color.GRAY)
        else
            setColorView(Color.WHITE)

        bStarted = false;
    }

    interface Listener {
        fun onStart(timer: Timer)
        fun onStop(timer: Timer)
        fun onReset(timer: Timer)
        fun onDelete(timer: Timer)
    }
}