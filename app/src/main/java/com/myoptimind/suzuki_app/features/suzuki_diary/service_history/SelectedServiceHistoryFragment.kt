package com.myoptimind.suzuki_app.features.suzuki_diary.service_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.AddServiceHistoryMaintenanceFragment.MaintenanceTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_selected_service_history.*
import kotlinx.android.synthetic.main.fragment_selected_service_history.label_emergency_service
import kotlinx.android.synthetic.main.fragment_selected_service_history.label_preventive_maintenance

@AndroidEntryPoint
open class SelectedServiceHistoryFragment(): TitleOnlyFragment(){
    override fun getTitle() = "My Suzuki Diary"
    open val viewModel: ServiceHistoryViewModel by activityViewModels()
    val args: SelectedServiceHistoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(args.index != -1){
            viewModel.initServiceHistory(args.index)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_selected_service_history,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNewTitle(args.title)

        label_preventive_maintenance.setOnClickListener { changeMaintenance(MaintenanceTab.PREVENTIVE) }
        label_emergency_service.setOnClickListener { changeMaintenance(MaintenanceTab.EMERGENCY) }

        viewModel.activeServiceHistoryDetails.observe(viewLifecycleOwner){ details ->
            if(details != null){
                tv_title.text = details.registeredMotorcycleName
                tv_odometer_reading.text = details.currentOdometerReading + if(!details.currentOdometerReading.isNullOrBlank()) " miles" else ""
                tv_date_purchase.text = details.purchasedDate
                tv_date_pms.text = details.nextPmsDate
                tv_mileage_pms.text = details.mileageLeft + if(!details.mileageLeft.isNullOrBlank()) " miles" else ""
            }
        }

        changeMaintenance(MaintenanceTab.PREVENTIVE)
    }

    private fun changeMaintenance(maintenanceTab: MaintenanceTab) {

        viewModel.updateMaintenanceTab(maintenanceTab)

        label_preventive_maintenance.disableSpecState()
        label_emergency_service.disableSpecState()

        val activeTextView = view?.findViewById<TextView>(maintenanceTab.labelId)


        if(activeTextView != null){
            val drawableTransparent = requireContext().getDrawable(android.R.color.transparent)
            val whiteTextColor = ContextCompat.getColor(requireContext(), R.color.color_blue_1000)

            activeTextView.background = drawableTransparent
            activeTextView.setTextColor(whiteTextColor)
        }

        val activeDetails = viewModel.serviceHistoryMaintenanceHash[maintenanceTab]

        if(activeDetails != null){
            tv_change_oil.text = activeDetails.changeOil.valueFromTwochoices()
            tv_tires.text = activeDetails.tires.valueFromThreechoices()
            tv_brakes.text = activeDetails.brakes.valueFromThreechoices()
            tv_chains_sprockets.text = activeDetails.chainsAndSprockets.valueFromThreechoices()
            tv_air_filter.text = activeDetails.airFilter.valueFromThreechoices()
            tv_spark_plugs.text = activeDetails.sparkPlugs.valueFromThreechoices()
            tv_exhaust_muffler.text = activeDetails.exhaust_muffler.valueFromThreechoices()
            tv_suspension.text = activeDetails.suspension.valueFromThreechoices()
            tv_notes.text = activeDetails.notes
        }

    }

    private fun String.valueFromTwochoices(): String {
        return when(this){
            "0" -> "NONE"
            "1" -> "CHECK"
            "2" -> "COMPLETED"
            else -> "UNKNOWN"
        }
    }

    private fun String.valueFromThreechoices(): String {
        return when(this){
            "0" -> "NONE"
            "1" -> "CHECK"
            "2" -> "CLEAN"
            "3" -> "REPLACE"
            else -> "UNKNOWN"
        }
    }

    private fun TextView.disableSpecState(){
        val drawableWhite = ContextCompat.getDrawable(requireContext(),android.R.color.white)
        val greyTextColor = ContextCompat.getColor(requireContext(), R.color.color_grey_disabled)

        this.background = drawableWhite
        this.setTextColor(greyTextColor)
    }
}