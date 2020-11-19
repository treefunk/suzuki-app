package com.myoptimind.suzuki_app.features.spare_parts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.dealer_locator.data.DealerLocatorsListItem
import com.myoptimind.suzuki_app.features.spare_parts.data.SparePart

class SelectedSparePartAdapter(
        var sparePartList: List<SparePart>,
        var listener: SelectedSparePartListener? = null
): RecyclerView.Adapter<SelectedSparePartAdapter.ViewHolder> () {

    inner class ViewHolder(
            private val v: View,
            val listener: SelectedSparePartListener?
    ) : RecyclerView.ViewHolder(v) {

        fun bind(sparePart: SparePart){
            Glide.with(v.context)
                    .load(sparePart.thumbnail)
                    .fitCenter()
                    .into(v.findViewById(R.id.iv_main))

            v.findViewById<TextView>(R.id.tv_motorcycle_name).text = sparePart.name
            v.findViewById<TextView>(R.id.tv_model_name).text = "Model No: ${sparePart.modelNumber}"
            v.findViewById<TextView>(R.id.tv_price_name).text = "Php ${sparePart.price}"

            v.findViewById<LinearLayout>(R.id.ll_inquire).setOnClickListener { listener?.onInquiry(sparePart.id) }
            v.findViewById<LinearLayout>(R.id.ll_find_a_dealer).setOnClickListener { listener?.onFindDealer(sparePart.listOfDealers) }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedSparePartAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_spare_part_category,parent,false)

        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: SelectedSparePartAdapter.ViewHolder, position: Int) {
        holder.bind(sparePartList[position])
    }

    override fun getItemCount() = sparePartList.size

    interface SelectedSparePartListener {
        fun onInquiry(id: String)
        fun onFindDealer(listOfDealers: List<DealerLocatorsListItem>)
    }
}