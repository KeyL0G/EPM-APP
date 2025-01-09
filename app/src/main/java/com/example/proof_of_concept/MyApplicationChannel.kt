package com.example.proof_of_concept

import android.app.Application

class MyApplicationChannel : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}
