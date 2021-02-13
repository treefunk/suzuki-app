package com.myoptimind.suzuki_motors.features.dealer_locator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.dealer_locator.data.DealerLocatorsListItem

class DealerLocatorsAdapter(
        var dealerLocatorsList: List<DealerLocatorsListItem>,
        var listener: DealerLocatorListener? = null
): RecyclerView.Adapter<DealerLocatorsAdapter.ViewHolder>() {

    inner class ViewHolder(
            private val v: View,
            private val listener: DealerLocatorListener?
    ): RecyclerView.ViewHolder(v){
        fun bind(dealerLocatorsListItem: DealerLocatorsListItem){
            v.findViewById<TextView>(R.id.label_dealer_name).text = dealerLocatorsListItem.name
            v.findViewById<TextView>(R.id.tv_address).text = dealerLocatorsListItem.fullAddress?.trim()
            v.findViewById<TextView>(R.id.tv_phone).text = dealerLocatorsListItem.contactNumber
            v.findViewById<TextView>(R.id.tv_mobile).text = dealerLocatorsListItem.mobileNumber
            v.findViewById<Button>(R.id.btn_get_directions).setOnClickListener { listener?.onClickDirections(dealerLocatorsListItem.id!!) }
            v.findViewById<Button>(R.id.btn_check_dealers_info).setOnClickListener { listener?.onClickDealersInfo(dealerLocatorsListItem.id!!) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dealer,parent,false)

        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dealerLocatorsList[position])
    }



    override fun getItemCount(): Int = dealerLocatorsList.size

    interface DealerLocatorListener {
        fun onClickDirections(id: String)
        fun onClickDealersInfo(id: String)
    }
}