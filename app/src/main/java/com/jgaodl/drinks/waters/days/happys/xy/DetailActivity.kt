package com.jgaodl.drinks.waters.days.happys.xy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jgaodl.drinks.waters.days.happys.xy.bean.Grop
import com.jgaodl.drinks.waters.days.happys.xy.bean.Info
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ActivityDetailBinding
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ItemHomeBinding
import com.jgaodl.drinks.waters.days.happys.xy.util.Local

class DetailActivity : AppCompatActivity() {

    companion object {
        var tmpData: Grop? = null
        fun start(mContext: Context, data: Grop){
            tmpData = data
            mContext.startActivity(Intent(mContext,DetailActivity::class.java))
        }
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener { finish() }

        initRv()
        val mData = tmpData
        if (null!=mData){
            loadList(mData)
        }
    }


    fun loadList(data: Grop){
        val list = data.list
        itemList.clear()
        itemList.addAll(list)
        adapter.notifyDataSetChanged()

        updateUI(data)
    }

    fun updateUI(data: Grop){
        binding.woter.text = "${data.coutn}ml"
        binding.cupss.text = "${data.cups}Cups"
        binding.title.text = "${data.crDateStr}"
        binding.pgTv.text = "${data.rate}%"
        binding.pg.progress = data.rate
    }

    fun initRv(){
        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
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
                itemList.removeAt(position)
                itemUpdateUI(itemList.toMutableList())
            }
        }
    }


    fun itemUpdateUI(list:MutableList<Info>){
        val mData = tmpData
        if (list.isEmpty()){
            finish()
            return
        }
        if (null!=mData){
            val gp = Grop(mData.crDateStr,list)
            loadList(gp)
        }
    }



}