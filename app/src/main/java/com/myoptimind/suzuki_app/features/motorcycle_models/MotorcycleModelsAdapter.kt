package com.myoptimind.suzuki_app.features.motorcycle_models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.motorcycle_models.data.MotorcycleModelListItem

class MotorcycleModelsAdapter(
        var motorcyclesModelsList: List<MotorcycleModelListItem>
): RecyclerView.Adapter<MotorcycleModelsAdapter.ViewHolder>() {

    inner class ViewHolder(private val v: View): RecyclerView.ViewHolder(v){
        fun bind(motorcycleModelListItem: MotorcycleModelListItem){
            Glide.with(v.context)
                    .load(motorcycleModelListItem.thumbnail)
                    .centerInside()
                    .into(v.findViewById<ImageView>(R.id.iv_image))
            v.findViewById<TextView>(R.id.tv_motorcycle_name).text    = motorcycleModelListItem.name
            v.findViewById<TextView>(R.id.tv_motorcycle_caption).text = motorcycleModelListItem.tagLine
            v.findViewById<TextView>(R.id.tv_motorcycle_price).text   = "Php ${motorcycleModelListItem.price}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_motorcycle_model,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(motorcyclesModelsList[position])
    }



    override fun getItemCount(): Int = motorcyclesModelsList.size
}