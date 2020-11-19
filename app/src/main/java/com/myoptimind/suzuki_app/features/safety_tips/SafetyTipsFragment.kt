package com.myoptimind.suzuki_app.features.safety_tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.customer_care.CustomerCareFragment
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_safety_tips.*
import timber.log.Timber

@AndroidEntryPoint
class SafetyTipsFragment: TitleOnlyFragment() {

    override fun getTitle() = "SAFETY TIPS WITH SUZUKI"
    private val viewModel: SafetyTipsViewModel by activityViewModels()
    private var adapter: SafetyTipsListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_safety_tips,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SafetyTipsListAdapter(ArrayList(), object: SafetyTipsListAdapter.SafetyTipsListener{
            override fun onClickItem(index: Int) {
                SafetyTipsFragmentDirections.actionSafetyTipsFragmentToSelectedSafetyTipsFragment(index).also {
                    findNavController().navigate(it)
                }
            }
        })
        rv_safety_tips.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rv_safety_tips.adapter = adapter

        initClickListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.safetyTipsResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    adapter?.safetyTipsList = result.data.data
                    adapter?.notifyDataSetChanged()
                    hideLoading()
                }
                is Result.Error -> {
                    Timber.e(result.error.message.toString())
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                }
            }

        }
    }

    private fun initClickListeners() {
        btn_learn_to_ride.setOnClickListener {
            SafetyTipsFragmentDirections.actionSafetyTipsFragmentToCustomerCareFragment(CustomerCareFragment.InquiryType.LEARN_TO_RIDE.id).also {
                findNavController().navigate(it)
            }
        }
        btn_schedule_seminar.setOnClickListener {
            SafetyTipsFragmentDirections.actionSafetyTipsFragmentToCustomerCareFragment(CustomerCareFragment.InquiryType.LEARN_TO_RIDE.id).also {
                findNavController().navigate(it)
            }
        }
    }
}