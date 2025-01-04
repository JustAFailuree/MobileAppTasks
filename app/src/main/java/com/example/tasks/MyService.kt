package com.example.tasks

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class MyService : Service() {


    private  var mBinder : IBinder = LocalBinder()
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    fun count(num : Int) : Int{
        return num * 100
    }

    fun countPi(num : Int) : Double{
        var pointsInCircle = 0
        for (i in 0 until num){
            val x = 2 * Math.random() - 1
            val y = 2 * Math.random() - 1
            if (x*x + y*y <= 1) {
                pointsInCircle += 1
            }
        }
        return 4.0 * pointsInCircle / num
    }

    inner class LocalBinder : Binder(){
        fun getService() : MyService{
            return this@MyService
        }
    }
}