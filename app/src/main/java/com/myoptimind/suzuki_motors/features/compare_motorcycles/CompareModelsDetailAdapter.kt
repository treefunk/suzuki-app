package com.myoptimind.suzuki_motors.features.compare_motorcycles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.compare_motorcycles.data.MotorSingleDetail
import kotlin.math.max

class CompareModelsDetailAdapter(
        var firstMotorDetailsList: List<MotorSingleDetail>,
        var secondMotorDetailsList: List<MotorSingleDetail>
): RecyclerView.Adapter<CompareModelsDetailAdapter.ViewHolder>() {

    inner class ViewHolder(
            private val v: View
    ): RecyclerView.ViewHolder(v){

        fun bindFirstMotorcycle(firstDetail: MotorSingleDetail?){
            initDetails(
                    v.findViewById<TextView>(R.id.tv_first_label),
                    v.findViewById<TextView>(R.id.tv_first_value),
                    firstDetail
            )
        }

        fun bindSecondMotorcycle(secondDetail: MotorSingleDetail?){
            initDetails(
                    v.findViewById<TextView>(R.id.tv_second_label),
                    v.findViewById<TextView>(R.id.tv_second_value),
                    secondDetail
            )
        }

        private fun initDetails(label: TextView, value: TextView, details: MotorSingleDetail?){
            if(details != null){
                label.visibility = View.VISIBLE
                value.visibility = View.VISIBLE
                label.text = details._label
                value.text = details._value
            }else{
                label.visibility = View.INVISIBLE
                value.visibility = View.INVISIBLE
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_motor_detail,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(firstMotorDetailsList.isNotEmpty()){
            holder.bindFirstMotorcycle(firstMotorDetailsList[position])
        }
        if(secondMotorDetailsList.isNotEmpty()){
            holder.bindSecondMotorcycle(secondMotorDetailsList[position])
        }
    }



    override fun getItemCount(): Int = max(firstMotorDetailsList.size,secondMotorDetailsList.size)

}