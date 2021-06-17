package fr.delcey.fizzbuzz

import android.app.Application

class MainApplication : Application() {

    companion object {
        lateinit var sInstance: Application
            private set
    }

    init {
        sInstance = this
    }
}