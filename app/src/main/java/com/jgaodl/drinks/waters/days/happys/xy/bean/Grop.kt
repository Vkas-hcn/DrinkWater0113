package com.jgaodl.drinks.waters.days.happys.xy.bean

import com.jgaodl.drinks.waters.days.happys.xy.util.Util

data class Grop public constructor(val crDateStr:String, val list:MutableList<Info>){

    var target = 0 // 当天目标喝水量
    var coutn = 0 // 当天一个喝了多少水
    var cups = 0 // 当天喝了几次水

    var rate = 0
    init {
        init()
    }
    fun init(){

        list.sortBy { it.crTime } //

//        list.forEach {
//            Util.i { it.toStr() }
//        }

        this.  target = list.last().goals // 取当天最后一次喝水中的 ，当天目标喝水量

        var num = 0
        for ((index, info) in list.withIndex()) {
            num = num+info.value
        }
        this.  coutn = num
       this. cups = list.size

        this.  rate = coutn*100/target


    }




}