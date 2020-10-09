package com.myoptimind.suzuki_app.features.customer_care

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.shared.InfoDialogFragment
import com.myoptimind.suzuki_app.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_customer_care.*

private const val CUSTOMER_CARE_DIALOG_TAG = "customer_care_dialog_tag"

@AndroidEntryPoint
class CustomerCareFragment: Fragment() {

    private val viewModel: CustomerCareViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_customer_care,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                }
                is Result.Error -> {

                }
                Result.Loading -> {

                }
            }

        }
    }

    private fun initClickListeners() {

        val choices = arrayOf("General Inquiry","Report a bug","Emergency Meeting")

        val adapterList = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                choices
        )

        tv_inquiry_type.apply {
            setAdapter(adapterList)
            setText(choices[0],false)
            setOnItemClickListener { _, _, index, _ ->
                setText(choices[index],false)
            }
        }

        tv_inquiry_type.setOnClickListener {
            tv_inquiry_type.showDropDown()
        }

        btn_submit.setOnClickListener {
            viewModel.submitInquiry(
                    tv_inquiry_type.text.toString(),
                    tv_complete_name.text.toString(),
                    tv_landline.text.toString(),
                    tv_phone_number.text.toString(),
                    tv_email_address.text.toString(),
                    tv_location.text.toString(),
                    tv_your_message.text.toString()
            )
        }
    }
}