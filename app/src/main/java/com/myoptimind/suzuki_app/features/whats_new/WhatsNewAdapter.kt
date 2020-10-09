package com.myoptimind.suzuki_app.features.whats_new

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.whats_new.data.Article


class WhatsNewAdapter(
        var articleList: List<Article>
): RecyclerView.Adapter<WhatsNewAdapter.ViewHolder>() {

    inner class ViewHolder(private val v: View): RecyclerView.ViewHolder(v){
        fun bind(article: Article){
            Glide.with(v.context)
                   .load(article.banner)
                   .into(v.findViewById(R.id.iv_main_image))
            v.findViewById<TextView>(R.id.tv_title).text = article.heading
            v.findViewById<TextView>(R.id.tv_type).text = article.category
            v.findViewById<TextView>(R.id.tv_caption).text = article.formattedCreatedAt

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_whats_new,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articleList[position])
    }



    override fun getItemCount(): Int = articleList.size
}