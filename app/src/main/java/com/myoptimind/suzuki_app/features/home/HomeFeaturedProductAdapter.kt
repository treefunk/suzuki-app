package com.myoptimind.suzuki_app.features.home

import com.myoptimind.suzuki_app.features.home.data.HomeFeaturedProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R


class HomeFeaturedProductAdapter(
        var homeFeaturedProducts: List<HomeFeaturedProduct>,
        var homeFeaturedListener: HomeFeaturedListener? = null
) : RecyclerView.Adapter<HomeFeaturedProductAdapter.ViewHolder>() {


    class ViewHolder(private val v: View,private val listener: HomeFeaturedListener?): RecyclerView.ViewHolder(v) {

        fun bind(homeFeaturedProduct: HomeFeaturedProduct){
            val img = v.findViewById<ImageView>(R.id.iv_featured_product_image)
            img.setOnClickListener {
                    when(homeFeaturedProduct.contentType){
                        "motorcycle" -> listener?.onClick(ContentType.MOTORCYCLE)
                        "dealer" -> listener?.onClick(ContentType.DEALER)
                        "event" -> listener?.onClick(ContentType.EVENT)
                        "service_center" -> listener?.onClick(ContentType.SERVICE_CENTER)
                        "safety_tip" -> listener?.onClick(ContentType.SAFETY_TIP)
                        "article" -> listener?.onClick(ContentType.SAFETY_TIP)
                    }
            }
            Glide.with(itemView.context)
                    .load(homeFeaturedProduct.image)
                    .optionalCenterCrop()
                    .into(img)

        }
    }

    enum class ContentType(title: String) {
        MOTORCYCLE("motorcycle"),
        DEALER("dealer"),
        EVENT("event"),
        SERVICE_CENTER("service_center"),
        SAFETY_TIP("safety_tip"),
        ARTICLE("article")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_featured_product,parent,false),homeFeaturedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(homeFeaturedProducts[position])
    }

    override fun getItemCount() = homeFeaturedProducts.size

    interface HomeFeaturedListener{
        fun onClick(contentType: ContentType)
    }
}