package com.myoptimind.suzuki_motors.features.whats_new

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.whats_new.data.Article


class WhatsNewAdapter(
        var articleList: List<Article>,
        var listener: WhatsNewListener? = null
): RecyclerView.Adapter<WhatsNewAdapter.ViewHolder>() {

    inner class ViewHolder(private val v: View,
                           private val listener: WhatsNewListener?
    ): RecyclerView.ViewHolder(v){

        fun bind(article: Article, position: Int){
            Glide.with(v.context)
                   .load(article.banner)
                    .centerCrop()
                   .into(v.findViewById(R.id.iv_main_image))
            v.findViewById<TextView>(R.id.tv_title).text = article.heading
            v.findViewById<TextView>(R.id.tv_type).text = article.category
            if(article.category.equals("promo",true)){
                v.findViewById<TextView>(R.id.tv_caption).text = article.subHeading
            }else{
                v.findViewById<TextView>(R.id.tv_caption).text = article.formattedCreatedAt
            }
            v.setOnClickListener { listener?.onClickWhatsNew(position) }
        }
    }

    interface WhatsNewListener {
        fun onClickWhatsNew(index: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_whats_new,parent,false)

        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articleList[position],position)
    }

    override fun getItemCount(): Int = articleList.size
}