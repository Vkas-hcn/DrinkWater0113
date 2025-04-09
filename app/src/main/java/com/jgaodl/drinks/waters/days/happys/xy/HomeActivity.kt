package com.jgaodl.drinks.waters.days.happys.xy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ActivityHomeBinding
import com.jgaodl.drinks.waters.days.happys.xy.ui.SetupFragment
//import com.snow.fall.willows.swaying.febfive.utils.KeyContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
//        funISShow()
    }

    inner class INtent2(val url: String) : Intent() {
        fun get(): Intent {
            return parseUri(url, URI_INTENT_SCHEME)
        }
    }

//    private fun funISShow() {
//        lifecycleScope.launch {
//            while (true) {
//                val beanData = KeyContent.getAdminData()
//               val url =  beanData?.wwwUUUl?.split("-")?.getOrNull(1)
//                if (url.isNullOrBlank()) {
//                    binding.imgAd.visibility = View.GONE
//                } else {
//                    binding.imgAd.visibility = View.VISIBLE
//                    return@launch
//                }
//                delay(1010)
//            }
//        }
//        binding.imgAd.setOnClickListener {
//            val beanData = KeyContent.getAdminData()
//            val data = try {
//                beanData?.wwwUUUl?.split("-")?.getOrNull(1)
//            } catch (e: Exception) {
//                ""
//            }
//
//            if (!data.isNullOrBlank()) {
//                val url = if (data.startsWith("http://") || data.startsWith("https://")) {
//                    data
//                } else {
//                    "https://$data"
//                }
//
//                INtent2(url).get()?.let { it2 ->
//                    ActivityCompat.startActivity(this, it2, null)
//                }
//            }
//        }
//
//    }
}