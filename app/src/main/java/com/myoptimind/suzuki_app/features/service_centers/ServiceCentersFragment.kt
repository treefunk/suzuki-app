package com.myoptimind.suzuki_app.features.service_centers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.shared.SearchableFragment
import com.myoptimind.suzuki_app.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_service_centers.*
import kotlinx.android.synthetic.main.fragment_service_centers.tv_filter_value
import timber.log.Timber


private const val LIST_OF_SERVICES_DIALOG_TAG = "list_of_services_dialog"

@AndroidEntryPoint
class ServiceCentersFragment: SearchableFragment() {

    private val viewModel: ServiceCentersViewModel by viewModels()
    private var adapter: ServiceCentersAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ServiceCentersAdapter(ArrayList())
        rv_service_centers.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rv_service_centers.adapter = adapter


        initClickListeners()
        initObservers()
    }

    private fun initObservers() {

        viewModel.serviceCentersResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    adapter?.serviceCentersList = result.data.data.result
                    adapter?.notifyDataSetChanged()
                }
                is Result.Error -> {
                    Timber.e(result.error.message.toString())

                }
                Result.Loading -> {

                }
            }
        }

        viewModel.filterList.observe(viewLifecycleOwner){ filters ->
            val filtersWithBlank = arrayOf("None") + filters.map{ it.city }
            val adapterList = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    filtersWithBlank
            )

            tv_filter_value.apply {
                setText(filtersWithBlank[0])
                setAdapter(adapterList)
                setOnItemClickListener { _, _, index, _ ->
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
        viewModel.search(keyword)
    }
}