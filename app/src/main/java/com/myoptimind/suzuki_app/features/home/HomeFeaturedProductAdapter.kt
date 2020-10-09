package com.myoptimind.suzuki_app.features.home

import com.myoptimind.suzuki_app.features.home.data.HomeFeaturedProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R


class HomeFeaturedProductAdapter(
        var homeFeaturedProducts: List<HomeFeaturedProduct>
) : RecyclerView.Adapter<HomeFeaturedProductAdapter.ViewHolder>() {


    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {

        fun bind(homeFeaturedProduct: HomeFeaturedProduct){
            val img = v.findViewById<ImageView>(R.id.iv_featured_product_image)
            Glide.with(itemView.context)
                    .load(homeFeaturedProduct.image)
                    .centerCrop()
                    .into(img)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_featured_product,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(homeFeaturedProducts[position])
    }

    override fun getItemCount() = homeFeaturedProducts.size
}