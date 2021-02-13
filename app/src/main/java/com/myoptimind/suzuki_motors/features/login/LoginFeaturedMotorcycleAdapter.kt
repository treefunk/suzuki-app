package com.myoptimind.suzuki_motors.features.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.login.data.LoginFeaturedMotorcycle


class LoginFeaturedMotorcycleAdapter(
        var loginFeaturedMotorcycles: List<LoginFeaturedMotorcycle>
) : RecyclerView.Adapter<LoginFeaturedMotorcycleAdapter.ViewHolder>() {


    class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {

        fun bind(loginFeaturedMotorcycle: LoginFeaturedMotorcycle){
            val img = v.findViewById<ImageView>(R.id.iv_featured_product_image)
            Glide.with(itemView.context)
                    .load(loginFeaturedMotorcycle.thumbnail)
                    .fitCenter()
                    .into(img)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_featured_product,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(loginFeaturedMotorcycles[position])
    }

    override fun getItemCount() = loginFeaturedMotorcycles.size
}