package com.jgaodl.drinks.waters.days.happys.xy.util

import com.jgaodl.drinks.waters.days.happys.xy.bean.Grop
import com.jgaodl.drinks.waters.days.happys.xy.bean.Info
import com.tencent.mmkv.MMKV

object Local {
    val KEY_FIRST ="KEY_FIRST"
    fun getIsFirst(): Boolean {
       val ss =  getString(KEY_FIRST)
        if ("100".equals(ss)){
            return true
        }else{
            return false
        }
    }
    fun setIsFirst(){
        putString(KEY_FIRST,"100")
    }


    lateinit var mmkv: MMKV

    private fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }
    private fun getString(key: String): String {
        return mmkv.decodeString(key, "") ?: ""
    }

    private fun putSet(key: String, value: Set<String>) {
        mmkv.encode(key, value)
    }
    private  fun getSet(key: String): MutableSet<String> {
        return mmkv.decodeStringSet(key) ?: mutableSetOf()
    }

    private val KEY_CUSTOM_VALUE = "key_custom_value"
    fun setCustomValue(str:String){
        putString(KEY_CUSTOM_VALUE,str)
    }
    fun getCustomValue(): String {
       return getString(KEY_CUSTOM_VALUE)
    }

    private val KEY_TOTAL_GOALS = "key_total_goals"
    fun setTotalGoals(str:String){
        putString(KEY_TOTAL_GOALS,str)
    }
    fun getTotalGoals(): String {
        return getString(KEY_TOTAL_GOALS)
    }


    private val KEY_INFO = "KEY_INFO"
    fun setInfo(info:Info){
        val set = getSet(KEY_INFO)
        set.add(info.toStr())
        putSet(KEY_INFO,set)
    }

    fun rmInfo(info:Info){
        val set = getSet(KEY_INFO)
        for ((index, ss) in set.withIndex()) {
            if (ss.startsWith("${info.crTime}")){
                set.remove(ss)
                break
            }
        }
        putSet(KEY_INFO,set)
    }

    var tatolWort = 0 // 所以天一共喝了多少水
    var tatolRate = 0 // 所以天的百分比之和
    fun getGropList(): MutableList<Grop> {
        tatolWort = 0
        tatolRate = 0
        val set = getSet(KEY_INFO)

        val map = mutableMapOf<String,MutableList<Info>>()
        for ((index, ss) in set.withIndex()) {
            val array =ss.split("###")
            val crTime:Long =  array.get(0).toLong()
            val value:Int =  array.get(1).toInt()
            val goals:Int =  array.get(2).toInt()
            val info = Info(crTime,value,goals)

            val tmpList =  map.get(info.crDateStr)
            if (tmpList.isNullOrEmpty()){
                map.put(info.crDateStr,mutableListOf(info))
            }else{
                tmpList.add(info)
            }
        }

        val list = mutableListOf<Grop>()
        for (entry in map) {
            val grop = Grop(entry.key,entry.value)
            tatolWort = tatolWort + grop.coutn
            tatolRate = tatolRate + grop.rate
            list.add(grop)
        }

        list.sortByDescending { it.crDateStr }

        return  list
    }


    var tadayAwly = 0 // 当天一共喝了多少水
    fun getInfoListDay(): MutableList<Info> {
        tadayAwly = 0
        val set = getSet(KEY_INFO)

        val todayStr = Util.dateFormat(System.currentTimeMillis())

        val list = mutableListOf<Info>()

        for ((index, ss) in set.withIndex()) {
            val array =ss.split("###")
            val crTime:Long =  array.get(0).toLong()
            val value:Int =  array.get(1).toInt()
            val goals:Int =  array.get(2).toInt()
            val info = Info(crTime,value,goals)

            if (todayStr.equals(info.crDateStr)){
                list.add(info)
                tadayAwly=tadayAwly+info.value
            }
        }

        list.sortByDescending { it.crTime }

        return  list
    }


}