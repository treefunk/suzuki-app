package com.myoptimind.suzuki_motors.features.motorcycle_models

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.dealer_locator.DealerLocatorsAdapter
import com.myoptimind.suzuki_motors.features.dealer_locator.data.DealerLocatorsListItem
import com.myoptimind.suzuki_motors.features.shared.BaseDialogFragment
import com.myoptimind.suzuki_motors.features.spare_parts.SelectedSparePartFragment
import com.myoptimind.suzuki_motors.features.spare_parts.SelectedSparePartFragmentDirections
import kotlinx.android.synthetic.main.dialog_list_of_dealers.*
import timber.log.Timber


private const val ARGS_DEALER_LOCATOR_LIST = "args_dealer_locator_list"
private const val ARGS_DEALER_LOCATOR_FILTERS = "args_dealer_locator_filters"

class ListOfDealersDialogFragment : BaseDialogFragment() {

    companion object {
        fun newInstance(dealerLocators: List<DealerLocatorsListItem>, filters: List<String>): ListOfDealersDialogFragment {
            val args = Bundle()
            args.putParcelableArrayList(ARGS_DEALER_LOCATOR_LIST,ArrayList(dealerLocators))
            args.putStringArrayList(ARGS_DEALER_LOCATOR_FILTERS, ArrayList(filters))
            val fragment = ListOfDealersDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    val adapter: DealerLocatorsAdapter? = null
    val viewModel: ListOfDealersDialogViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        arguments.also {
            viewModel.setListOfDealers(it?.getParcelableArrayList(ARGS_DEALER_LOCATOR_LIST)!!)
            viewModel.setFilters(it.getStringArrayList(ARGS_DEALER_LOCATOR_FILTERS)!!)
        }
        return inflater.inflate(R.layout.dialog_list_of_dealers,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_list_of_dealers.adapter = DealerLocatorsAdapter(ArrayList())
        rv_list_of_dealers.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)

        viewModel.listOfDealersResult.observe(viewLifecycleOwner){ dealers ->
            rv_list_of_dealers.adapter = DealerLocatorsAdapter(dealers, object: DealerLocatorsAdapter.DealerLocatorListener{
                override fun onClickDirections(id: String) {
                    if(parentFragment is SelectedSparePartFragment){
                        Timber.v("Selected spare part")
                        SelectedSparePartFragmentDirections.actionSelectedSparePartFragmentToSelectedDealerLocatorFragment(id).also {
                            findNavController().navigate(it)
                        }
                    }else if(parentFragment is SelectedMotorcycleFragment){
                        Timber.v("Selected motorcycle")
                        SelectedMotorcycleFragmentDirections.actionSelectedMotorcycleFragmentToSelectedDealerLocatorFragment2(id).also {
                            findNavController().navigate(it)
                        }
                    }
                }

                override fun onClickDealersInfo(id: String) {
                    if(parentFragment is SelectedSparePartFragment){
                        Timber.v("Selected spare part")
                        SelectedSparePartFragmentDirections.actionSelectedSparePartFragmentToSelectedDealerLocatorFragment(id).also {
                            findNavController().navigate(it)
                        }
                    }else if(parentFragment is SelectedMotorcycleFragment){
                        Timber.v("Selected motorcycle")
                        SelectedMotorcycleFragmentDirections.actionSelectedMotorcycleFragmentToSelectedDealerLocatorFragment2(id).also {
                            findNavController().navigate(it)
                        }
                    }
                }
            })
            adapter?.notifyDataSetChanged()
        }


        et_search_dealer.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(et: Editable?) {
                viewModel.searchListOfDealers(et.toString())
            }
        })

        ib_close.setOnClickListener {
            this.dismiss()
        }




        viewModel.filters.observe(viewLifecycleOwner) { filters ->

            if(filters != null){
                val filtersWithBlank = arrayOf("All") + filters
                val adapterList = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        filtersWithBlank
                )

                ac_select_city.setOnClickListener {
                    ac_select_city.showDropDown()
                }

                ac_select_city.apply {
                    setText("")
                    setAdapter(adapterList)
                    setOnItemClickListener { _, _, index, _ ->
                        viewModel.updateCity(filtersWithBlank[index])
                    }
                    adapterList.notifyDataSetChanged()
                }
            }


        }


    }
}