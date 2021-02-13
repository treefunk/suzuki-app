package com.myoptimind.suzuki_motors.features.suzuki_diary.service_history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_motors.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_service_history_maintenance.*
import timber.log.Timber

@AndroidEntryPoint
class AddServiceHistoryMaintenanceFragment(): TitleOnlyFragment() {

    val viewModel: ServiceHistoryViewModel by activityViewModels()


    override fun getTitle() = "My Suzuki Diary"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_add_service_history_maintenance,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initClickListeners()

        changeMaintenance(MaintenanceTab.PREVENTIVE) // Default Tab

    }

    private fun initObservers() {
        viewModel.postServiceHistoryResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    Toast.makeText(requireContext(),result.data.meta.message,Toast.LENGTH_LONG).show()
                    Timber.v("Attempting to navigate..")
                    viewModel.clearResults()
                    AddServiceHistoryMaintenanceFragmentDirections.actionAddServiceHistoryMaintenanceFragmentToServiceHistoryListFragment(viewModel.currentIndex).also {
                        findNavController().navigate(it)
                    }
                    viewModel.getUpcomingEvents()
                    hideLoading()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(),result.error.message,Toast.LENGTH_LONG).show()
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                }
            }
        }

        viewModel.updateServiceHistoryResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    Toast.makeText(requireContext(),result.data.meta.message,Toast.LENGTH_LONG).show()
                    Timber.v("Attempting to navigate..")
                    viewModel.clearResults()
                    AddServiceHistoryMaintenanceFragmentDirections.actionAddServiceHistoryMaintenanceFragmentToServiceHistoryListFragment(viewModel.currentIndex).also {
                        findNavController().navigate(it)
                    }
                    hideLoading()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(),result.error.message,Toast.LENGTH_LONG).show()
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                }
            }
        }


    }

    private fun initClickListeners() {
        label_preventive_maintenance.setOnClickListener { changeMaintenance(MaintenanceTab.PREVENTIVE) }
        label_emergency_service.setOnClickListener { changeMaintenance(MaintenanceTab.EMERGENCY) }

        chipgroup_change_oil.initSelectedChip { viewModel.getMaintenanceDetails().changeOil = it }
        chipgroup_tires.initSelectedChip { viewModel.getMaintenanceDetails().tires = it }
        chipgroup_brakes.initSelectedChip { viewModel.getMaintenanceDetails().brakes = it }
        chipgroup_chains_sprockets.initSelectedChip { viewModel.getMaintenanceDetails().chainsAndSprockets = it }
        chipgroup_air_filter.initSelectedChip { viewModel.getMaintenanceDetails().airFilter = it }
        chipgroup_spark_plugs.initSelectedChip { viewModel.getMaintenanceDetails().sparkPlugs = it }
        chipgroup_exhaust_muffler.initSelectedChip { viewModel.getMaintenanceDetails().exhaust_muffler = it }
        chipgroup_suspension.initSelectedChip { viewModel.getMaintenanceDetails().suspension = it }
        chipgroup_chassis_bolts_nuts.initSelectedChip { viewModel.getMaintenanceDetails().chassisBoltsNuts = it }

        et_notes.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }
            override fun afterTextChanged(etNotes: Editable?) {
                viewModel.getMaintenanceDetails().notes = etNotes.toString()
            }
        })


        btn_save.setOnClickListener {
            viewModel.saveServiceHistoryMaintenance()
            Toast.makeText(requireContext(),"Details successfully saved.",Toast.LENGTH_LONG).show()
        }

        btn_submit.setOnClickListener {
            Timber.v("clicked Submit")
            viewModel.submit()
        }

        tv_back.setOnClickListener { requireActivity().onBackPressed() }
    }


    private fun changeMaintenance(maintenanceTab: MaintenanceTab){

        viewModel.updateMaintenanceTab(maintenanceTab)

        label_preventive_maintenance.disableSpecState()
        label_emergency_service.disableSpecState()

        val activeDetails = viewModel.serviceHistoryMaintenanceHash[maintenanceTab]

        if(activeDetails != null){
            chipgroup_change_oil.selectChipByValue(activeDetails.changeOil)
            chipgroup_tires.selectChipByValue(activeDetails.tires)
            chipgroup_brakes.selectChipByValue(activeDetails.brakes)
            chipgroup_chains_sprockets.selectChipByValue(activeDetails.chainsAndSprockets)
            chipgroup_air_filter.selectChipByValue(activeDetails.airFilter)
            chipgroup_spark_plugs.selectChipByValue(activeDetails.sparkPlugs)
            chipgroup_exhaust_muffler.selectChipByValue(activeDetails.exhaust_muffler)
            chipgroup_suspension.selectChipByValue(activeDetails.suspension)
            chipgroup_chassis_bolts_nuts.selectChipByValue(activeDetails.chassisBoltsNuts)
            et_notes.setText(activeDetails.notes)
        }


        val activeTextView = view?.findViewById<TextView>(maintenanceTab.labelId)

        if(activeTextView != null){
            val drawableTransparent = requireContext().getDrawable(android.R.color.transparent)
            val whiteTextColor = ContextCompat.getColor(requireContext(), R.color.color_blue_1000)

            activeTextView.background = drawableTransparent
            activeTextView.setTextColor(whiteTextColor)
        }



    }

    enum class MaintenanceTab(
            val labelId: Int,
            val title: String
    ){
        PREVENTIVE(R.id.label_preventive_maintenance,"preventive"),
        EMERGENCY(R.id.label_emergency_service,"emergency")
    }

    private fun ChipGroup.initSelectedChip(
            onselect: ( selectedString: String ) -> Unit
    ){
        this.setOnCheckedChangeListener{ _, checkedId ->
            val selected = when(checkedId){
                R.id.choice_checked -> "1"
                R.id.choice_completed,R.id.choice_clean -> "2"
                R.id.choice_replace -> "3"
                else -> "0"
            }
            onselect(selected)
        }
    }

    private fun ChipGroup.selectChipByValue(value: String){
        if(this.childCount == 2){ // assuming the choices are "checked" and "completed"
            when(value){
                "0" -> this.clearCheck()
                "1" -> this.check(R.id.choice_checked)
                "2" -> this.check(R.id.choice_completed)
            }
        }else{
            when(value){ // assuming the choices are "checked", "clean" and "replace"
                "0" -> this.clearCheck()
                "1" -> this.check(R.id.choice_checked)
                "2" -> this.check(R.id.choice_clean)
                "3" -> this.check(R.id.choice_replace)
            }
        }

    }

    private fun TextView.disableSpecState(){
        val drawableWhite = ContextCompat.getDrawable(requireContext(),android.R.color.white)
        val greyTextColor = ContextCompat.getColor(requireContext(), R.color.color_grey_disabled)

        this.background = drawableWhite
        this.setTextColor(greyTextColor)
    }


}