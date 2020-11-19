package com.myoptimind.suzuki_app.features.motorcycle_models

import android.graphics.Color
import android.graphics.Color.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.motorcycle_models.data.ColorVariant

class SelectedMotorcycleColorVariantsAdapter(
        private val colorVariantList: List<ColorVariant>,
        private val listener: ColorVariantListener? = null
): RecyclerView.Adapter<SelectedMotorcycleColorVariantsAdapter.ViewHolder>(){

    inner class ViewHolder(
            private val v: View,
            private val listener: ColorVariantListener?
    ): RecyclerView.ViewHolder(v){

        fun bind(colorVariant: ColorVariant){

            v.findViewById<View>(R.id.view_color_variant).let {
                it.setBackgroundColor(Color.parseColor(colorVariant.color))
                it.setOnClickListener { listener?.onPressed(colorVariant) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_motorcycle_color_variant,parent,false)
        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(colorVariantList[position])
    }

    override fun getItemCount() = colorVariantList.size
}

interface ColorVariantListener {
    fun onPressed(colorvariant: ColorVariant)
}