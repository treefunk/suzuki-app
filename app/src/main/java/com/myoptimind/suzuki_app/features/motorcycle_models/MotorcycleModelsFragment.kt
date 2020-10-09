package com.myoptimind.suzuki_app.features.motorcycle_models

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
import com.myoptimind.suzuki_app.shared.api.Result
import com.myoptimind.suzuki_app.shared.SearchableFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_motorcycle_models.*
import timber.log.Timber

@AndroidEntryPoint
class MotorcycleModelsFragment : SearchableFragment() {

    private val viewModel: MotorcycleModelsViewModel by viewModels()
    private var adapter: MotorcycleModelsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MotorcycleModelsAdapter(ArrayList())
        rv_motorcycle_models.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        rv_motorcycle_models.adapter = adapter


        initClickListeners()
        initObservers()
    }

    private fun initObservers() {

        viewModel.motorcycleModelsResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    adapter?.motorcyclesModelsList = result.data.data.result
                    adapter?.notifyDataSetChanged()
                }
                is Result.Error -> {
                    Timber.e(result.error.message.toString())
                }
                Result.Loading -> {
                    //
                }
            }

        }

        viewModel.filterList.observe(viewLifecycleOwner) { filters ->

            val filtersWithBlank = arrayOf("None") + filters.map{ it.name }
            val adapterList = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    filtersWithBlank
            )

            tv_filter_value.apply {
                setText(filtersWithBlank[0])
                setAdapter(adapterList)
                setOnItemClickListener { _, _, index, _ ->
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
        viewModel.search(keyword)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_motorcycle_models, container, false)
    }



}