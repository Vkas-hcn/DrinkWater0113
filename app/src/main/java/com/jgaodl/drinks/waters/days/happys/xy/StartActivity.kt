package com.jgaodl.drinks.waters.days.happys.xy

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity

import com.jgaodl.drinks.waters.days.happys.xy.databinding.ActivityStartBinding
import com.jgaodl.drinks.waters.days.happys.xy.util.Local
import com.jgaodl.drinks.waters.days.happys.xy.util.Util

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ss =  Local.getTotalGoals()
        if (ss.isEmpty()){
            Local.setTotalGoals("2500")
        }

        binding.yes.setOnClickListener {
            val txt=  binding.input.text.toString().trim()
            if (txt.isEmpty()){
                Util.toast("Please enter the daily water consumption plan")
            }else{
                Local.setTotalGoals(txt)
                Local.setIsFirst()
                startActivity(Intent(this,HomeActivity::class.java))
                finish()
            }
        }
        onBackPressedDispatcher.addCallback {
        }
    }

}