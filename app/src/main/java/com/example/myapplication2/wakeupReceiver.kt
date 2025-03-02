package com.example.myapplication2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class wakeupReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            setAllarms();
        }
    }
}