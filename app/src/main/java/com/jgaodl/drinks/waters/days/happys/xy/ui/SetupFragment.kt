package com.jgaodl.drinks.waters.days.happys.xy.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.jgaodl.drinks.waters.days.happys.xy.databinding.FragmentSetupBinding
import com.jgaodl.drinks.waters.days.happys.xy.util.Util

class SetupFragment : Fragment() {

    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        Util.i { "onAttach()" }
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.pp.setOnClickListener {
            ActivityCompat.startActivity(mContext,INtent("https://sites.google.com/view/dropdrink/home").get(),null)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class INtent(val url:String): Intent() {
        fun get(): Intent {
           return Intent.parseUri(url,Intent.URI_INTENT_SCHEME)
        }
    }
}