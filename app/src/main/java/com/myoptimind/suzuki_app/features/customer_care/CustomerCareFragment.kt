package com.myoptimind.suzuki_app.features.customer_care

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.login.data.User
import com.myoptimind.suzuki_app.features.shared.*
import com.myoptimind.suzuki_app.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_customer_care.*
import javax.inject.Inject

private const val CUSTOMER_CARE_DIALOG_TAG = "customer_care_dialog_tag"

@AndroidEntryPoint
class CustomerCareFragment(): TitleOnlyFragment() {

    @Inject
    lateinit var appSharedPref: AppSharedPref

    private lateinit var user: User

    override fun getTitle() = "Customer Care"

    private val viewModel: CustomerCareViewModel by viewModels()
    val args: CustomerCareFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_customer_care,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = appSharedPref.getUserFromPrefs()
        tv_complete_name.setText(user.fullname)
        tv_email_address.setText(user.emailAddress)


        initClickListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.submitInquiryResult.observe(viewLifecycleOwner) { result ->
            when (result){
                is Result.Success -> {
                    InfoDialogFragment.newInstance(
                            "Thank you for your\ninquiry!",
                            "One of our customer care agent will get in touch with you soon. Please wait for our email or call."
                    ).show(childFragmentManager, CUSTOMER_CARE_DIALOG_TAG)
//                    findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                    clearFields()
                    hideLoading()
                }
                is Result.Error -> {
                    InfoDialogFragment.newInstance(
                            "Something went wrong, please try again",""
                    ).show(childFragmentManager, CUSTOMER_CARE_DIALOG_TAG)
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                }
            }

        }
    }

    enum class InquiryType(val id: String){
        GENERAL("0"),TEST_RIDE("1"),LEARN_TO_RIDE("2"),PRODUCT("3")
    }

    private fun createInquiryFromString(id: String): InquiryType {
        return when(id){
            "0" -> InquiryType.GENERAL
            "1" -> InquiryType.TEST_RIDE
            "2" -> InquiryType.LEARN_TO_RIDE
            "3" -> InquiryType.PRODUCT
            else -> InquiryType.GENERAL
        }
    }

    private fun initClickListeners() {

        val choices = arrayOf("General Inquiry","Test Ride Inquiry","Learn to Ride/Safety Riding Inquiry","Product Inquiry")

        val adapterList = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                choices
        )

        tv_inquiry_type.apply {
            setAdapter(adapterList)
            when(createInquiryFromString(args.selectedInquiry)){
                InquiryType.GENERAL -> setText(choices[0],false)
                InquiryType.TEST_RIDE -> setText(choices[1],false)
                InquiryType.LEARN_TO_RIDE -> setText(choices[2],false)
                InquiryType.PRODUCT -> setText(choices[3],false)
            }
            setOnItemClickListener { _, _, index, _ ->
                setText(choices[index],false)
            }
        }

        tv_inquiry_type.setOnClickListener {
            tv_inquiry_type.showDropDown()
        }

        btn_submit.setOnClickListener {
            if(validateFields()){
                if(!tv_email_address.text.toString().validateEmail()){
                    Toast.makeText(requireContext(),"Invalid E-mail format.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                viewModel.submitInquiry(
                        tv_inquiry_type.text.toString(),
                        tv_complete_name.text.toString(),
                        tv_landline.text.toString(),
                        tv_phone_number.text.toString(),
                        tv_email_address.text.toString(),
                        tv_location.text.toString(),
                        tv_your_message.text.toString()
                )
            }else{
                Toast.makeText(requireContext(),"Please fill in required fields.", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun validateFields(): Boolean {
        return tv_inquiry_type.izNotBlank() &&
                tv_complete_name.izNotBlank() &&
                tv_landline.izNotBlank() &&
                tv_phone_number.izNotBlank() &&
                tv_email_address.izNotBlank() &&
                tv_your_message.izNotBlank()
    }

    private fun clearFields() {
        tv_inquiry_type.setText("")
        tv_inquiry_type.clearFocus()

        tv_complete_name.setText(user.fullname)
        tv_inquiry_type.clearFocus()

        tv_landline.setText("")
        tv_landline.clearFocus()

        tv_phone_number.setText("")
        tv_phone_number.clearFocus()

        tv_email_address.setText(user.emailAddress)
        tv_email_address.clearFocus()

        tv_location.setText("")
        tv_location.clearFocus()

        tv_your_message.setText("")
        tv_your_message.clearFocus()

    }
}