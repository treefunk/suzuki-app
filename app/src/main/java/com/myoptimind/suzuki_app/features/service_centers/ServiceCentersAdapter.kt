package com.myoptimind.suzuki_app.features.service_centers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.service_centers.data.ServiceCentersListItem

class ServiceCentersAdapter(
        var serviceCentersList: List<ServiceCentersListItem>
): RecyclerView.Adapter<ServiceCentersAdapter.ViewHolder>() {

    inner class ViewHolder(private val v: View): RecyclerView.ViewHolder(v){
        fun bind(serviceCentersListItem: ServiceCentersListItem){
            v.findViewById<TextView>(R.id.label_dealer_name).text = serviceCentersListItem.name
            v.findViewById<TextView>(R.id.tv_address).text = serviceCentersListItem.fullAddress
            v.findViewById<TextView>(R.id.tv_phone).text = serviceCentersListItem.contactNumber
            v.findViewById<TextView>(R.id.tv_mobile).text = serviceCentersListItem.mobileNumber
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dealer,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(serviceCentersList[position])
    }



    override fun getItemCount(): Int = serviceCentersList.size
}