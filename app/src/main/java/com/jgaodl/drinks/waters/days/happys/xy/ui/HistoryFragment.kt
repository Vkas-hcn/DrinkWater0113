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
import com.jgaodl.drinks.waters.days.happys.xy.DetailActivity
import com.jgaodl.drinks.waters.days.happys.xy.R
import com.jgaodl.drinks.waters.days.happys.xy.bean.Grop
import com.jgaodl.drinks.waters.days.happys.xy.databinding.FragmentHistoryBinding
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ItemHistoryBinding
import com.jgaodl.drinks.waters.days.happys.xy.util.Local

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onStart() {
        super.onStart()
        initRv()
        load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun load(){
        val list = Local.getGropList()
        itemList.clear()
        itemList.addAll(list)
        adapter.notifyDataSetChanged()

        if (list.isEmpty()){
            binding.empty.visibility = View.VISIBLE
        }else{
            binding.empty.visibility = View.GONE
        }

        updateUI(list)
    }

    fun initRv(){
        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(mContext)
        binding.rv.adapter = adapter

    }

    fun updateUI(list:MutableList<Grop> ){
        if (list.isEmpty()){
            binding.days.text = "${0}"
            binding.avg.text = "0ml"
            binding.pgTv.text = "0%"
            binding.pg.progress =0
        }else{
            binding.days.text = "${list.size}"
            binding.avg.text = "${Local.tatolWort/list.size}ml"
            binding.pgTv.text = "${Local.tatolRate/list.size}%"
            binding.pg.progress =Local.tatolRate/list.size
        }
    }

    val itemList = mutableListOf<Grop>()
    inner class VH public constructor(val bin:ItemHistoryBinding) : RecyclerView.ViewHolder(bin.root)
    val adapter = object :RecyclerView.Adapter<VH>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return VH(ItemHistoryBinding.inflate(layoutInflater,parent,false))
        }
        override fun getItemCount(): Int {
            return itemList.size
        }
        override fun onBindViewHolder(vh: VH, position: Int) {
            val data = itemList.get(position)

            vh.bin.woter.text = "${data.coutn}ml"
            vh.bin.cupss.text = "${data.cups}Cups"
            vh.bin.timeTv.text = "${data.crDateStr}"
            vh.bin.rateTv.text = "${data.rate}%"
            if(data.coutn>=data.target){
                vh.bin.rateTv.setTextColor(Color.parseColor("#FF12BA88"))
                vh.bin.complet.text = "Complete"
                vh.bin.complet.setBackgroundResource(R.mipmap.item_green_tag)
            }else{
                vh.bin.rateTv.setTextColor(Color.parseColor("#FFFF8023"))
                vh.bin.complet.text = "Unfinished"
                vh.bin.complet.setBackgroundResource(R.mipmap.item_orig_tag)
            }


            vh.itemView.setOnClickListener {
                DetailActivity.start(mContext,data)
            }

        }
    }

}