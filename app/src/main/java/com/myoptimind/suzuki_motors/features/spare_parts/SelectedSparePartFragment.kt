package com.myoptimind.suzuki_motors.features.spare_parts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.customer_care.CustomerCareFragment
import com.myoptimind.suzuki_motors.features.dealer_locator.data.DealerLocatorsListItem
import com.myoptimind.suzuki_motors.features.motorcycle_models.ListOfDealersDialogFragment
import com.myoptimind.suzuki_motors.features.shared.SearchableFragment
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.shared.setOnScrollEnd
import com.myoptimind.suzuki_motors.features.spare_parts.data.SparePart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_selected_spare_parts.*
import kotlinx.android.synthetic.main.fragment_selected_spare_parts.tv_filter_value
import kotlinx.android.synthetic.main.fragment_selected_spare_parts.tv_title
import timber.log.Timber

@AndroidEntryPoint
class SelectedSparePartFragment: SearchableFragment() {

    override fun getHint() = "Search Spare Parts by Name"

    private val args: SelectedSparePartFragmentArgs by navArgs()
    private lateinit var viewModel: SparePartsViewModel
    private var adapter: SelectedSparePartAdapter? = null

    private val sparePartList = ArrayList<SparePart>()
    private var total: Int? = null



    override fun onClickSearch(keyword: String) {
        sparePartList.clear()
        viewModel.search(keyword)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title.text = args.title
        adapter = SelectedSparePartAdapter(sparePartList, object: SelectedSparePartAdapter.SelectedSparePartListener {
            override fun onInquiry(id: String) {
                SelectedSparePartFragmentDirections.actionSelectedSparePartFragmentToCustomerCareFragment(CustomerCareFragment.InquiryType.PRODUCT.id).also {
                    findNavController().navigate(it)
                }
            }

            override fun onFindDealer(listOfDealers: List<DealerLocatorsListItem>) {
                ListOfDealersDialogFragment.newInstance(
                        listOfDealers,
                        listOfDealers.map { it.city }.distinct() as List<String>
                ).show(childFragmentManager,"List_of_dealers_spare_part")
            }
        })
        rv_selected_spare_parts.layoutManager =  LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rv_selected_spare_parts.adapter = adapter

        rv_selected_spare_parts.setOnScrollEnd {
            if(total != null && total!! > 0){
                viewModel.increaseRowCount()
            }
        }



        initClickListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.sparePartResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
//                    adapter?.sparePartList = result.data.data.result
                    sparePartList.addAll(result.data.data.result)
                    total = result.data.data.result.size
                    adapter?.notifyDataSetChanged()
                    hideLoading()
                    viewModel.resetSpartPartsResult()
                }
                is Result.Error -> {
                    Timber.e(result.error.message.toString())
                    hideLoading()
                    viewModel.resetSpartPartsResult()
                }
                Result.Loading -> {
                    showLoading()
                }
            }

        }

        viewModel.filterList.observe(viewLifecycleOwner) { filters ->

            val filtersWithBlank = arrayOf("All") + filters.map{ it.name }
            val adapterList = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    filtersWithBlank
            )

            tv_filter_value.apply {
                setText(filtersWithBlank[0])
                setAdapter(adapterList)
                setOnItemClickListener { _, _, index, _ ->
                    sparePartList.clear()
                    viewModel.updateMotorcycle(filtersWithBlank[index])
                }
            }

        }
    }

    private fun initClickListeners() {
        tv_filter_value.setOnClickListener {
            tv_filter_value.showDropDown()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SparePartsViewModel::class.java)
        viewModel.sparePartId = args.id
        viewModel.getSparePart(args.id,null,null,null,viewModel.rowCount)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
//        viewModel.getSparePart(args.id,null,null,null,viewModel.rowCount)
        return inflater.inflate(R.layout.fragment_selected_spare_parts,container,false)
    }
}