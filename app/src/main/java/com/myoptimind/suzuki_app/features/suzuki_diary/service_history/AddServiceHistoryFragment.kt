package com.myoptimind.suzuki_app.features.suzuki_diary.service_history

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.*
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data.ServiceHistoryDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_service_history.*
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Months
import timber.log.Timber
import kotlin.math.ceil
import kotlin.math.floor


private const val REQUEST_CODE_DATE_PURCHASED = 30
private const val REQUEST_CODE_DATE_PMS       = 40
@AndroidEntryPoint
class AddServiceHistoryFragment : TitleOnlyFragment() {

    override fun getTitle() = "My Suzuki Diary"
    val viewModel: ServiceHistoryViewModel by activityViewModels()
    val args: AddServiceHistoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(args.index != -1){
            viewModel.initServiceHistory(args.index)
        }else{
            viewModel.checkExisting()
        }
        viewModel.getRegisteredMotorcycles()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_add_service_history,container,false)
    }

/*    private fun getNextPMS(nextPmsDate: String?){
        val dt = DateTime()
        val dateOfPurchaseDt = et_date_of_purchase.toDateTime()
        val monthStep: Int
        var mileage = 0.0
        val oneMonthMark = dt!! < dateOfPurchaseDt.plusMonths(1)
        Timber.v("${dt!!} < ${dateOfPurchaseDt.plusMonths(1)}")
        val monthDiff = Months.monthsBetween(dateOfPurchaseDt,dt).months
        val sameDay = dt.dayOfMonth() == dateOfPurchaseDt.dayOfMonth()
        var multiplier: Double = 0.0
        Timber.v("in one month mark $oneMonthMark"  )
        Timber.v("monthdiff $monthDiff")
        Timber.v("sameday $sameDay")
        if(oneMonthMark){
            mileage = 1000.0
            tv_date_pms.setText(dateOfPurchaseDt.plusMonths(1).toReadableDateTime())
        } else if (!oneMonthMark && sameDay){
            multiplier = 1.0
            tv_date_pms.setText(dateOfPurchaseDt.plusMonths(1).toReadableDateTime())
        }else {
            multiplier = ceil(monthDiff / 6.0)
            tv_date_pms.setText(dateOfPurchaseDt.plusMonths(multiplier.toInt()).toReadableDateTime())
        }
        if(multiplier != 0.0){
            mileage = multiplier * 4000
        }
        tv_mileage_pms.setText(mileage.toString())
    }*/

    private fun getNextPMS(purchasedDate : String?){
        val dateOfPurchaseDT = purchasedDate?.toDateTime()
        val dateTodayDT = DateTime()

        val monthsDiff = Months.monthsBetween(dateOfPurchaseDT,dateTodayDT).months
        val daysDiff = Days.daysBetween(dateOfPurchaseDT,dateTodayDT).days

        Timber.v("month diff $monthsDiff")
        Timber.v("days diff $daysDiff")


        var mileage = 0.0
        if(monthsDiff > 1 || (monthsDiff == 1 && daysDiff > 0)){
            var totalMonths = monthsDiff
            if(daysDiff > 0){
                totalMonths++
            }
            val modifier = ceil(totalMonths.toFloat() / 6.0)
            val months = (6 * modifier).toInt()
            tv_date_pms.setText(dateOfPurchaseDT?.plusMonths(months)?.toReadableDateTime())
            mileage = 4000 * modifier
        }else{
            mileage = 1000.0
            if(!cb_manual_input.isChecked){
                tv_date_pms.setText(dateOfPurchaseDT?.plusMonths(1)?.toReadableDateTime())
            }
        }

        if(tv_odometer_reading.text.toString().isBlank()){
            tv_mileage_pms.setText(mileage.toString())
        }else{
            val remainingMileage = mileage - tv_odometer_reading.text.toString().toFloat()
            tv_mileage_pms.setText(remainingMileage.toString())
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        if(args.index != -1){
            tv_title.text = "EDIT SERVICE HISTORY"
        }else{
            tv_title.text = "ADD SERVICE HISTORY"
        }

//        et_date_of_purchase.initDatePicker(parentFragmentManager,this,"date_of_purchase", REQUEST_CODE_DATE_PURCHASED)


        tv_odometer_reading.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun afterTextChanged(et: Editable?) {
                if(et_date_of_purchase.izNotBlank()){
                    getNextPMS(et_date_of_purchase.text.toString())
                }
            }
        })

        cb_manual_input.setOnCheckedChangeListener { _, checked ->
            if(ac_beast_nickname.izNotBlank()){
                if(checked){
                    tv_date_pms.initDatePicker(parentFragmentManager,this,"date_pms", REQUEST_CODE_DATE_PMS,null,DateTime().millis)

                    tv_date_pms.isEnabled = true
//                tv_mileage_pms.setText("")
                    tv_date_pms.setText("")
//                group_mileage_manual.visibility = View.VISIBLE
                }else{
//                group_mileage_manual.visibility = View.GONE
//                tv_mileage_pms.setText("")
                    if(et_date_of_purchase.text.toString().isNotBlank())
                        getNextPMS(et_date_of_purchase.text.toString())
                    tv_date_pms.isEnabled = false
                }
            }else{
                Toast.makeText(requireContext(),"Please select Beast Nickname first.",Toast.LENGTH_LONG).show()
                cb_manual_input.isChecked = false
            }

        }


        tv_next.setOnClickListener {
            if(
                    ac_beast_nickname.izNotBlank() &&
                    tv_odometer_reading.izNotBlank() &&
                    et_date_of_purchase.izNotBlank() &&
                    tv_date_pms.izNotBlank()){

                if(cb_manual_input.isChecked && !tv_mileage_pms.izNotBlank()){
                    Toast.makeText(requireContext(),"Please input mileage left before next PMS field.",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                viewModel.saveStepOne(
                        ac_beast_nickname.tag.toString(),
                        ac_beast_nickname.text.toString(),
                        tv_odometer_reading.text.toString(),
                        et_date_of_purchase.text.toString(),
                        tv_date_pms.text.toString(),
                        tv_mileage_pms.text.toString()
                )

                findNavController().navigate(R.id.addServiceHistoryMaintenanceFragment)

            }else{
                Toast.makeText(requireContext(),"Please fill in required fields.",Toast.LENGTH_LONG).show()
            }

        }

        btn_save.setOnClickListener {
            viewModel.saveHistoryDetails(
                    ServiceHistoryDetails(
                            ac_beast_nickname.tag.toString(),
                            ac_beast_nickname.text.toString(),
                            tv_odometer_reading.text.toString(),
                            et_date_of_purchase.text.toString(),
                            tv_date_pms.text.toString(),
                            tv_mileage_pms.text.toString()
                    )
            )
            Toast.makeText(requireContext(),"Details successfully saved.",Toast.LENGTH_LONG).show()
        }



        initObservers()

    }

    private fun initObservers() {
        viewModel.activeServiceHistoryDetails.observe(viewLifecycleOwner){ serviceHistoryDetails ->
            if(serviceHistoryDetails != null){

                ac_beast_nickname.tag = serviceHistoryDetails.registeredMotorcycleId
                ac_beast_nickname.setText(serviceHistoryDetails.registeredMotorcycleName)

                tv_odometer_reading.setText(serviceHistoryDetails.currentOdometerReading)

                et_date_of_purchase.setText(serviceHistoryDetails.purchasedDate)

                if(serviceHistoryDetails.purchasedDate != null) {
                    getNextPMS(serviceHistoryDetails.purchasedDate)
                }

                tv_date_pms.setText(serviceHistoryDetails.nextPmsDate)


                tv_mileage_pms.setText(serviceHistoryDetails.mileageLeft)
            }


        }

        viewModel.registeredMotorcyclesResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    val adapter: ArrayAdapter<String> = ArrayAdapter(
                            requireContext(),android.R.layout.simple_dropdown_item_1line,
                            result.data.data.map{ it.beastNickname }
                    )
                    ac_beast_nickname.setAdapter(adapter)
                    ac_beast_nickname.apply {
                        setOnClickListener {
                            if(result.data.data.isNotEmpty()){
                                showDropDown()
                            }else{
                                Toast.makeText(requireContext(),"You don't have any registered motorcycle. Please register a motorcycle first.",Toast.LENGTH_LONG).show()
                            }
                        }
                        setOnItemClickListener { adapterView, view, i, l ->
                            this.setText(adapter.getItem(i),false)
                            val item = result.data.data[i]
                            ac_beast_nickname.tag = item.id
                            et_date_of_purchase.setText(item.datePurchased)
                            getNextPMS(item.datePurchased)
                        }
                    }
                    hideLoading()
                }
                is Result.Error -> {
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_DATE_PURCHASED && resultCode == Activity.RESULT_OK){
            et_date_of_purchase.setText(data?.getStringExtra(DatePickerDialogFragment.EXTRA_DATE))
        }

        if(requestCode == REQUEST_CODE_DATE_PMS && resultCode == Activity.RESULT_OK){
            val dateString = data?.getStringExtra(DatePickerDialogFragment.EXTRA_DATE)
            tv_date_pms.setText(dateString)
//            getNextPMS(et_date_of_purchase.text.toString())
        }
    }
}