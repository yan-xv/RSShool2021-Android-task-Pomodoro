package com.yanxv.pomodoro


import android.content.Intent

import android.media.RingtoneManager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity(), OnRecyclerViewFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openRecyclerViewFragment()
    }

    private fun openRecyclerViewFragment() {
        // создаем новый экземпляр фрагмента
        val quizFragment = RecyclerViewFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, quizFragment).commit()
    }

    override fun onAppBackgrounded(startTime: Long) {
        val startIntent = Intent(this, ForegroundService::class.java)
        startIntent.putExtra(COMMAND_ID, COMMAND_START)
        startIntent.putExtra(STARTED_TIMER_TIME_MS, startTime)
        startService(startIntent)
    }

    override fun onAppForegrounded() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

    override fun onAppFinishedTimer() {
        //var ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //RingtoneManager.getRingtone(applicationContext, ringURI).play()
    }

    companion object {
        const val NOTIFY_ID = 778
        const val CHANNEL_ID = "channelID"
    }
}