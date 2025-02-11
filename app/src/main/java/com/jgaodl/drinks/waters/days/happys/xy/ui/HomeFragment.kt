package com.jgaodl.drinks.waters.days.happys.xy.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jgaodl.drinks.waters.days.happys.xy.R
import com.jgaodl.drinks.waters.days.happys.xy.ReltActivity
import com.jgaodl.drinks.waters.days.happys.xy.bean.Grop
import com.jgaodl.drinks.waters.days.happys.xy.bean.Info
import com.jgaodl.drinks.waters.days.happys.xy.dialog.AddDialog
import com.jgaodl.drinks.waters.days.happys.xy.databinding.FragmentHomeBinding
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ItemHistoryBinding
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ItemHomeBinding
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ItemLinValeBinding
import com.jgaodl.drinks.waters.days.happys.xy.dialog.TargetDialog
import com.jgaodl.drinks.waters.days.happys.xy.util.Local
import com.jgaodl.drinks.waters.days.happys.xy.util.Util

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
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
        Util.i { "onCreateView()" }
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.goals.setOnClickListener {
            TargetDialog(mContext) {
                Local.setTotalGoals(it)
                load1()
                loadList()
            }.show()
        }
        binding.add.setOnClickListener {
            AddDialog(mContext) {
                Local.setCustomValue(it)
                load2()
            }.show()
        }


        initRv()

        binding.pg.progress = 0
//        binding.pg2.progress = 0

        load1()
        load2()

        return root
    }
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Util.i { "onViewStateRestored()" }
        super.onViewStateRestored(savedInstanceState)
    }
    override fun onStart() {
        Util.i { "onStart()" }
        super.onStart()
        loadList()
    }
    override fun onResume() {
        Util.i { "onResume()" }
        super.onResume()

    }
    override fun onPause() {
        Util.i { "onPause()" }
        super.onPause()
    }
    override fun onStop() {
        Util.i { "onStop()" }
        super.onStop()
    }
    override fun onDestroyView() {
        Util.i { "onDestroyView()" }
        super.onDestroyView()
        _binding = null
    }
    override fun onDetach() {
        Util.i { "onDetach()" }
        super.onDetach()
    }

    override fun onDestroy() {
        Util.i { "onDestroy()" }
        super.onDestroy()
    }


    val defVale = arrayOf(200,400,600)

    var totalGoals = 2500
    fun load1(){
        val ss = Local.getTotalGoals()
        Util.i { "load1() ss=$ss" }

        binding.goals.text = "${ss}ml"
        totalGoals = ss.toInt()
    }

    fun load2(){
        val list = defVale.toMutableList()
        val res = Local.getCustomValue()
        if (res.isEmpty()){ }else{
            val ss = res.toInt()
            list.add(0,ss)
        }
        binding.lin.removeAllViews()
        list.forEachIndexed { index, ii ->
            val bin:ItemLinValeBinding = ItemLinValeBinding.inflate(layoutInflater,binding.lin,false)
            bin.vale.text = "+${ii}ml"
            bin.vale.setOnClickListener {
                addInfo(ii)
                ReltActivity.start(mContext,"${ii}")
            }
            binding.lin.addView(bin.root)
        }
    }

    fun addInfo(value:Int){
        val info = Info(System.currentTimeMillis(),value,totalGoals)
        Local.setInfo(info)

        Util.toast("Record added successfully")
    }



    fun loadList(){
        val list = Local.getInfoListDay()
        itemList.clear()
        itemList.addAll(list)
        adapter.notifyDataSetChanged()
        if (list.isEmpty()){
            binding.empty.visibility = View.VISIBLE
        }else{
            binding.empty.visibility = View.GONE
        }
        upPg()
    }

    fun upPg(){
        Util.i { "upPg()111" }
        val rate = Local.tadayAwly*100/totalGoals

        Util.i { "upPg()222 rate=$rate" }
        binding.pgTv.text = "${rate}%"
        binding.pg.progress = rate
//        binding.pg2.progress = rate
        binding.alway.text = "${Local.tadayAwly}ml"
        Util.i { "upPg()333 rate=$rate" }
    }

    fun initRv(){
        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(mContext)
        binding.rv.adapter = adapter
    }

    val itemList = mutableListOf<Info>()
    inner class VH public constructor(val bin: ItemHomeBinding) : RecyclerView.ViewHolder(bin.root)
    val adapter = object : RecyclerView.Adapter<VH>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return VH(ItemHomeBinding.inflate(layoutInflater,parent,false))
        }
        override fun getItemCount(): Int {
            return itemList.size
        }
        override fun onBindViewHolder(vh: VH, position: Int) {
            val data = itemList.get(position)

            vh.bin.woter.text = "${data.value}ml"
            vh.bin.timeTv.text = "${data.crTimeStr}"


            vh.itemView.setOnClickListener {
                Local.rmInfo(data)
                loadList()
            }
        }
    }

}