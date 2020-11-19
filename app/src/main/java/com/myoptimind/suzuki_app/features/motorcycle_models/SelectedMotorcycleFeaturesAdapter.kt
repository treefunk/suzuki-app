package com.myoptimind.suzuki_app.features.motorcycle_models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.motorcycle_models.data.Feature

class SelectedMotorcycleFeaturesAdapter(
        val featureList: List<Feature>,
        val listener: FeatureListener? = null
): RecyclerView.Adapter<SelectedMotorcycleFeaturesAdapter.ViewHolder>(){

    inner class ViewHolder(
            private val v: View,
            private val listener: FeatureListener?
    ): RecyclerView.ViewHolder(v){

        fun bind(feature: Feature){
            Glide.with(v.context)
                    .load(feature.image)
                    .centerCrop()
                    .into(v.findViewById(R.id.iv_motorcycle_feature_image))
            v.setOnClickListener {
                listener?.onClickItem(feature)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_motorcycle_feature,parent,false),listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(featureList[position])
    }

    interface FeatureListener {
        fun onClickItem(feature: Feature)
    }

    override fun getItemCount() = featureList.size
}