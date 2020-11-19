package com.myoptimind.suzuki_app.features.service_centers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.service_centers.data.ServiceCentersListItem

class ServiceCentersAdapter(
        var serviceCentersList: List<ServiceCentersListItem>,
        var listener: ServiceCenterListener? = null
): RecyclerView.Adapter<ServiceCentersAdapter.ViewHolder>() {

    inner class ViewHolder(
            private val v: View,
            private val listener: ServiceCenterListener?
    ): RecyclerView.ViewHolder(v){
        fun bind(serviceCentersListItem: ServiceCentersListItem){
            v.findViewById<TextView>(R.id.label_dealer_name).text = serviceCentersListItem.name
            v.findViewById<TextView>(R.id.tv_address).text = serviceCentersListItem.fullAddress.trim()
            v.findViewById<TextView>(R.id.tv_phone).text = serviceCentersListItem.contactNumber
            v.findViewById<TextView>(R.id.tv_mobile).text = serviceCentersListItem.mobileNumber
            v.findViewById<Button>(R.id.btn_get_directions).setOnClickListener { listener?.onClickDirections(serviceCentersListItem.id) }
            v.findViewById<Button>(R.id.btn_check_dealers_info).apply{
                text = "Service Center's Info"
                setOnClickListener { listener?.onClickDealersInfo(serviceCentersListItem.id) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dealer,parent,false)

        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(serviceCentersList[position])
    }



    override fun getItemCount(): Int = serviceCentersList.size

    interface ServiceCenterListener {
        fun onClickDirections(id: String)
        fun onClickDealersInfo(id: String)
    }

}