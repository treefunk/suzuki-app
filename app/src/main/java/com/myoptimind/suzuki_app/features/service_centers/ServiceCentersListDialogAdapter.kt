package com.myoptimind.suzuki_app.features.service_centers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.service_centers.api.ServiceCentersService
import kotlinx.android.synthetic.main.dialog_list_of_services.view.*

class ServiceCentersListDialogAdapter(
        var servicesList: List<ServiceCentersService.ServiceCentersResponse.Service>
): RecyclerView.Adapter<ServiceCentersListDialogAdapter.ViewHolder>() {

    inner class ViewHolder(private val v: View): RecyclerView.ViewHolder(v){
        fun bind(service: ServiceCentersService.ServiceCentersResponse.Service){
/*            Glide.with(v.context)
                    .load(service.thumbnail)
                    .centerCrop()
                    .into(v.findViewById<ImageView>(R.id.iv_service_image))*/
            v.findViewById<TextView>(R.id.tv_service_name).text = service.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_service,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(servicesList[position])
    }



    override fun getItemCount(): Int = servicesList.size
}