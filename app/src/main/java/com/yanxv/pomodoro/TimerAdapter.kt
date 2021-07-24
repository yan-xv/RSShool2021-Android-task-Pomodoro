package com.yanxv.pomodoro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.yanxv.pomodoro.model.Timer

import com.yanxv.pomodoro.databinding.TimerItemBinding

class TimerAdapter(private val listener: TimerViewHolder.Listener):
    ListAdapter<Timer, TimerViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding        = TimerItemBinding.inflate(layoutInflater, parent, false)
        return TimerViewHolder(binding, listener, binding.root.context.resources)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Timer>,
        currentList: MutableList<Timer>
    ) {
        super.onCurrentListChanged(previousList, currentList)
    }

    private companion object {
        private val itemComparator = object : DiffUtil.ItemCallback<Timer>() {
            override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return  oldItem.currentMs == newItem.currentMs &&
                        oldItem.state     == newItem.state &&
                        oldItem.startMs   == newItem.startMs
            }
            override fun getChangePayload(oldItem: Timer, newItem: Timer) = Any()
        }
    }
}