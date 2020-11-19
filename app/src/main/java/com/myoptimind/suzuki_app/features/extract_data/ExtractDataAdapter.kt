package com.myoptimind.suzuki_app.features.extract_data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data.ServiceHistory
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter

class ExtractDataAdapter(
        var serviceHistoryList: List<ServiceHistory>,
        var listener: ExtractDataListener? = null
): RecyclerView.Adapter<ExtractDataAdapter.ViewHolder>() {

    inner class ViewHolder(
            private val v: View,
            val listener: ExtractDataListener?
    ): RecyclerView.ViewHolder(v){
        fun bind(serviceHistoryListItem: ServiceHistory, index: Int){
            v.findViewById<TextView>(R.id.label_beast_nickname).text = serviceHistoryListItem.beastNickname
            v.findViewById<TextView>(R.id.tv_date_purchase).text = serviceHistoryListItem.purchasedDate

            val parser = DateTimeFormat.forPattern("MMMM dd, yyyy")
            val dt = parser.parseDateTime(serviceHistoryListItem.nextPmsDate)

            v.findViewById<TextView>(R.id.tv_month).text = dt.toString("MMM").toUpperCase()
            v.findViewById<TextView>(R.id.tv_day).text = dt.toString("dd")


            v.findViewById<Button>(R.id.btn_more_info).setOnClickListener { listener?.onMoreInfo(index) }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_extract_data,parent,false)

        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(serviceHistoryList[position],position)
    }
    override fun getItemCount(): Int = serviceHistoryList.size

    interface ExtractDataListener {
        fun onMoreInfo(index: Int)
    }
}