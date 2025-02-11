package com.jgaodl.drinks.waters.days.happys.xy


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ActivityReltBinding
import com.jgaodl.drinks.waters.days.happys.xy.util.Local

class ReltActivity : AppCompatActivity() {

    companion object {
        var tmpData: String = ""
        fun start(mContext: Context, data: String){
            tmpData = data
            mContext.startActivity(Intent(mContext,ReltActivity::class.java))
        }
    }

    private lateinit var binding: ActivityReltBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReltBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener { finish() }

        binding.relt.text = "+${tmpData}ML"
        loadList()

    }


    fun loadList(){
        val list = Local.getInfoListDay()


        upPg()
    }

    fun upPg(){
        val totalGoals = Local.getTotalGoals().toInt()
        val rate = Local.tadayAwly*100/totalGoals

        binding.pgTv.text = "${rate}%"

    }


}