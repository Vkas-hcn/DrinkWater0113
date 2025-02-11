package com.jgaodl.drinks.waters.days.happys.xy.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager

import com.jgaodl.drinks.waters.days.happys.xy.databinding.DialogAddBinding
import com.jgaodl.drinks.waters.days.happys.xy.util.Util

class AddDialog public constructor(val mContext: Context,val call:(String)->Unit): Dialog(mContext) {

    private lateinit var binding: DialogAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancel.setOnClickListener { dismiss() }
        binding.confirm.setOnClickListener {

           val tt = binding.input.text.toString().trim()

            if (tt.isEmpty()){
                Util.toast("Please enter the amount of water consumed each time")
            }else{
                call.invoke(tt)
                dismiss()
            }
        }

        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val attr = it.attributes
            attr.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes = attr
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
}