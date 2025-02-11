package com.jgaodl.drinks.waters.days.happys.xy

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ActivityHomeBinding
import com.jgaodl.drinks.waters.days.happys.xy.ui.SetupFragment
import com.snow.fall.willows.swaying.febfive.utils.KeyContent

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList = null
        navView.itemTextColor = getColorStateList(R.color.nav_text_color)


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        binding.imgAd.setOnClickListener {
            val beanData = KeyContent.getAdminData()?: return@setOnClickListener
            val adurl = beanData.wwwUUUl.split("-")[1]
            KeyContent.showLog("adurl:$adurl")
            ActivityCompat.startActivity(this,
                INtent2(adurl).get(),null)
        }
    }
    inner class INtent2(val url:String): Intent() {
        fun get(): Intent {
            return parseUri(url, URI_INTENT_SCHEME)
        }
    }
}