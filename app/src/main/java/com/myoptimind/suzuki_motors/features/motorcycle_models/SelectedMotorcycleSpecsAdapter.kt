package com.myoptimind.suzuki_motors.features.motorcycle_models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.motorcycle_models.data.Spec

class SelectedMotorcycleSpecsAdapter(
        val specList: List<Spec>
): RecyclerView.Adapter<SelectedMotorcycleSpecsAdapter.ViewHolder>(){

    inner class ViewHolder(
            private val v: View
    ): RecyclerView.ViewHolder(v){

        fun bind(spec: Spec){
            v.findViewById<TextView>(R.id.tv_spec_name).text = spec.field
            v.findViewById<TextView>(R.id.tv_spec_value).text = spec.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_motorcycle_specification,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(specList[position])
    }

    override fun getItemCount() = specList.size
}