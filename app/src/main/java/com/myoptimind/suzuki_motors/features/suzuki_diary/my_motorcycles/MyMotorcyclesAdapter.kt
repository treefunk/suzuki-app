package com.myoptimind.suzuki_motors.features.suzuki_diary.my_motorcycles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.suzuki_diary.my_motorcycles.data.RegisteredMotorcycle

class MyMotorcyclesAdapter(
        var myMotorcyclesList: List<RegisteredMotorcycle>,
        var listener: RegisteredMotorcycleListener? = null
): RecyclerView.Adapter<MyMotorcyclesAdapter.ViewHolder>() {

    inner class ViewHolder(
            private val v: View,
            private val listener: RegisteredMotorcycleListener?
    ): RecyclerView.ViewHolder(v){
        fun bind(registeredMotorcycle: RegisteredMotorcycle,index: Int){
            v.findViewById<TextView>(R.id.label_beast_nickname).text = registeredMotorcycle.beastNickname
            v.findViewById<TextView>(R.id.tv_date_purchase).text = registeredMotorcycle.datePurchased
            v.findViewById<Button>(R.id.btn_edit).setOnClickListener { listener?.onClickEdit(index) }
            v.findViewById<Button>(R.id.btn_more_info).setOnClickListener { listener?.onClickMoreInfo(index) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_my_motorcycle,parent,false)

        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myMotorcyclesList[position],position)
    }



    override fun getItemCount(): Int = myMotorcyclesList.size

    interface RegisteredMotorcycleListener {
        fun onClickEdit(index: Int)
        fun onClickMoreInfo(index: Int)
    }

}