package com.myoptimind.suzuki_motors.features.safety_tips

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.safety_tips.data.SafetyTip

class SafetyTipsListAdapter(
        var safetyTipsList: List<SafetyTip>,
        var listener: SafetyTipsListener? = null
): RecyclerView.Adapter<SafetyTipsListAdapter.ViewHolder>() {

    inner class ViewHolder(
            private val v: View,
            private val listener: SafetyTipsListener?
    ): RecyclerView.ViewHolder(v){
        fun bind(safetyTip: SafetyTip, position: Int){
            Glide.with(v.context)
                    .load(safetyTip.thumbnail)
                    .centerCrop()
                    .into(v.findViewById(R.id.iv_main_image))
            v.findViewById<TextView>(R.id.tv_title).text = safetyTip.heading
            v.findViewById<TextView>(R.id.tv_caption).text = safetyTip.formattedCreatedAt
            v.setOnClickListener {
                listener?.onClickItem(position)
            }
        }

    }

    interface SafetyTipsListener {
        fun onClickItem(index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_safety_tip,parent,false)

        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(safetyTipsList[position],position)
    }

    override fun getItemCount(): Int = safetyTipsList.size
}