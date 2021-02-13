package com.myoptimind.suzuki_motors.features.motorcycle_models

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.motorcycle_models.data.MotorcycleModelListItem
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.shared.SearchableFragment
import com.myoptimind.suzuki_motors.features.shared.setOnScrollEnd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_motorcycle_models.*
import timber.log.Timber

@AndroidEntryPoint
class MotorcycleModelsFragment : SearchableFragment() {

    override fun getHint() = "Search Model by Name"

    private val viewModel: MotorcycleModelsViewModel by viewModels()
    private var adapter: MotorcycleModelsAdapter? = null
    private val motorcycleModels = ArrayList<MotorcycleModelListItem>()
    private var total: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MotorcycleModelsAdapter(motorcycleModels, object: MotorcycleModelsAdapter.MotorcycleModelListener{
            override fun onClickItem(id: String) {
                MotorcycleModelsFragmentDirections.actionMotorcycleModelsFragmentToSelectedMotorcycleFragment(id).also {
                    findNavController().navigate(it)
                }
            }
        })
        rv_motorcycle_models.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        rv_motorcycle_models.adapter = adapter
/*        rv_motorcycle_models.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                        if(total != null && total!! > 0){
                            viewModel.increaseRowCount()
                        }
                }
            }
        })*/

        rv_motorcycle_models.setOnScrollEnd(){
            if(total != null && total!! > 0){
                viewModel.increaseRowCount()
            }
        }


        initClickListeners()
        initObservers()
    }

    private fun initObservers() {

        viewModel.motorcycleModelsResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
//                    adapter?.motorcyclesModelsList = result.data.data.result
                    motorcycleModels.addAll(result.data.data.result)
                    total = result.data.meta.total
                    adapter?.notifyDataSetChanged()
                    hideLoading()
                    viewModel.resetResult()
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

        viewModel.filterList.observe(viewLifecycleOwner) { filters ->

            val filtersWithBlank = arrayOf("All") + filters.map{ it.name }
            val adapterList = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    filtersWithBlank
            )

            tv_filter_value.apply {
                setAdapter(adapterList)
                setOnItemClickListener { _, _, index, _ ->
                    motorcycleModels.clear()
                    viewModel.updateCategory(filtersWithBlank[index])
                }
            }

        }
    }

    private fun initClickListeners() {
        tv_filter_value.setOnClickListener {
            tv_filter_value.showDropDown()
        }
    }


    override fun onClickSearch(keyword: String) {
        motorcycleModels.clear()
        viewModel.search(keyword)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_motorcycle_models, container, false)
    }



}