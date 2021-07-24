package com.yanxv.pomodoro

import android.content.Context
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanxv.pomodoro.databinding.FragmentRecyclerViewBinding


interface OnRecyclerViewFragment {
    fun onAppBackgrounded(startTime: Long)
    fun onAppForegrounded()
    fun onAppFinishedTimer()
}

class RecyclerViewFragment : Fragment(), LifecycleObserver {
    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!
    private val timerController = TimerController { onFinishTimer() }
    private var listener: OnRecyclerViewFragment? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timerController.timerAdapter
        }

        binding.addNewStopwatchButton.setOnClickListener {
            timerController.addNewTimer(binding.editTextTime.text)
        }
    }

    private fun onFinishTimer():Unit {
        listener?.onAppFinishedTimer()

        playRingtone(RingtoneManager.TYPE_NOTIFICATION)
    }

    private fun playRingtone(typeNotification: Int){
        RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(typeNotification)).play()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        timerController.activeTimer?.let {
            listener?.onAppBackgrounded(it.currentMs)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        listener?.onAppForegrounded()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnRecyclerViewFragment)
            context
        else
            throw RuntimeException("$context must implement FragmentRecyclerViewBinding")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RecyclerViewFragment().apply {
                arguments = Bundle().apply {}
            }

        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }
}