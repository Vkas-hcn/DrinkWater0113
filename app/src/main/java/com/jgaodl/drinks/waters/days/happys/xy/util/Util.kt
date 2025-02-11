package com.jgaodl.drinks.waters.days.happys.xy.util

import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {


    fun dateFormat(ms: Long): String {
        val str = SimpleDateFormat("MMM d yyyy", Locale.getDefault()).format(Date(ms))
        return str
    }

    fun dateFormat2(ms: Long): String {
        val str = SimpleDateFormat("MMM d yyyy#HH:mm", Locale.getDefault()).format(Date(ms))
        return str
    }


    fun toast(msg:String){
        Toast.makeText(App.app,msg,Toast.LENGTH_SHORT).show();
    }

    inline fun i(call:() -> String){
//        Log.i("HHHH",call.invoke())
    }
}