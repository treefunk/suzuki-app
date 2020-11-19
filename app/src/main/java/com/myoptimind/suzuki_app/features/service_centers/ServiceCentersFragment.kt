package com.myoptimind.suzuki_app.features.service_centers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.dealer_locator.DealerLocatorsAdapter
import com.myoptimind.suzuki_app.features.dealer_locator.DealerLocatorsFragmentDirections
import com.myoptimind.suzuki_app.features.service_centers.data.ServiceCentersListItem
import com.myoptimind.suzuki_app.features.shared.SearchableFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.shared.setOnScrollEnd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_service_centers.*
import kotlinx.android.synthetic.main.fragment_service_centers.tv_filter_value
import timber.log.Timber


private const val LIST_OF_SERVICES_DIALOG_TAG = "list_of_services_dialog"

@AndroidEntryPoint
class ServiceCentersFragment: SearchableFragment() {

    override fun getHint() = "Search Fi Service Centers by Name"

    private val viewModel: ServiceCentersViewModel by viewModels()
    private var adapter: ServiceCentersAdapter? = null
    private val serviceCenterList = ArrayList<ServiceCentersListItem>()
    private var total: Int? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ServiceCentersAdapter(serviceCenterList, object: ServiceCentersAdapter.ServiceCenterListener{
            override fun onClickDirections(id: String) {
                ServiceCentersFragmentDirections.actionServiceCentersFragmentToSelectedServiceCenterFragment(id).also {
                    findNavController().navigate(it)
                }
            }

            override fun onClickDealersInfo(id: String) {
                ServiceCentersFragmentDirections.actionServiceCentersFragmentToSelectedServiceCenterFragment(id).also {
                    findNavController().navigate(it)
                }
            }
        })
        rv_service_centers.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rv_service_centers.adapter = adapter
        rv_service_centers.setOnScrollEnd {
            if(total != null && total!! > 0){
                viewModel.increaseRowCount()
            }
        }


        initClickListeners()
        initObservers()
    }

    private fun initObservers() {

        viewModel.serviceCentersResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
//                    adapter?.serviceCentersList = result.data.data.result
                    serviceCenterList.addAll(result.data.data.result)
                    total = result.data.data.result.size
                    adapter?.notifyDataSetChanged()
                    hideLoading()
                    viewModel.resetResults()
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

        viewModel.filterList.observe(viewLifecycleOwner){ filters ->
            val filtersWithBlank = arrayOf("All") + filters.map{ it.city }
            val adapterList = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    filtersWithBlank
            )

            tv_filter_value.apply {
                setText(filtersWithBlank[0])
                setAdapter(adapterList)
                setOnItemClickListener { _, _, index, _ ->
                    serviceCenterList.clear()
                    viewModel.updateLocation(filtersWithBlank[index])
                }
            }
        }
    }

    private fun initClickListeners() {
        tv_filter_value.setOnClickListener {
            tv_filter_value.showDropDown()
        }
        tv_view_our_services.setOnClickListener {
            ServiceCentersListDialog().show(childFragmentManager, LIST_OF_SERVICES_DIALOG_TAG)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_service_centers,container,false)
    }

    override fun onClickSearch(keyword: String) {
        serviceCenterList.clear()
        viewModel.search(keyword)
    }
}