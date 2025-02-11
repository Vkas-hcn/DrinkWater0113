package com.jgaodl.drinks.waters.days.happys.xy.bean

import com.jgaodl.drinks.waters.days.happys.xy.util.Util

  class Info public constructor(val crTime:Long,
                                   val value:Int,//
                                   val goals:Int
){

    var crDateStr = ""
    var crTimeStr = ""

    init {
        val timeStr = Util.dateFormat2(crTime);
        val array = timeStr.split("#")

        crDateStr = array.get(0)
        crTimeStr = array.get(1)
    }

    fun toStr(): String {
        return "${crTime}###${value}###${goals}"
    }
}