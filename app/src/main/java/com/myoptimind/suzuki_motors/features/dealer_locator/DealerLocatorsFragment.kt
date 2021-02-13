package com.myoptimind.suzuki_motors.features.dealer_locator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.dealer_locator.data.DealerLocatorsListItem
import com.myoptimind.suzuki_motors.features.shared.SearchableFragment
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.shared.setOnScrollEnd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_service_centers.*
import timber.log.Timber



@AndroidEntryPoint
class DealerLocatorsFragment: SearchableFragment() {

    override fun getHint() = "Search Dealers by Name"

    private val viewModel: DealerLocatorsViewModel by viewModels()
    private var adapter: DealerLocatorsAdapter? = null
    private val dealerLocatorList = ArrayList<DealerLocatorsListItem>()
    private var total: Int? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DealerLocatorsAdapter(dealerLocatorList, object: DealerLocatorsAdapter.DealerLocatorListener{
            override fun onClickDirections(id: String) {
                DealerLocatorsFragmentDirections.actionDealerLocatorsFragmentToSelectedDealerLocatorFragment(id).also {
                    findNavController().navigate(it)
                }
            }

            override fun onClickDealersInfo(id: String) {
                DealerLocatorsFragmentDirections.actionDealerLocatorsFragmentToSelectedDealerLocatorFragment(id).also {
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

        viewModel.dealerLocatorsResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
//                    adapter?.dealerLocatorsList = result.data.data.result
                    dealerLocatorList.addAll(result.data.data.result)
                    total = result.data.meta.total
                    adapter?.notifyDataSetChanged()
                    viewModel.resetResult()
                    hideLoading()
                }
                is Result.Error -> {
                    Timber.e(result.error.message.toString())
                    viewModel.resetResult()
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
                    dealerLocatorList.clear()
                    viewModel.updateLocation(filtersWithBlank[index])
                }
            }
        }
    }

    private fun initClickListeners() {
        tv_filter_value.setOnClickListener {
            tv_filter_value.showDropDown()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_service_centers,container,false)
        view.findViewById<TextView>(R.id.tv_title).text = "DEALER LOCATOR"
        view.findViewById<TextView>(R.id.tv_view_our_services).visibility = View.GONE
        return view
    }

    override fun onClickSearch(keyword: String) {
        dealerLocatorList.clear()
        viewModel.search(keyword)
    }
}