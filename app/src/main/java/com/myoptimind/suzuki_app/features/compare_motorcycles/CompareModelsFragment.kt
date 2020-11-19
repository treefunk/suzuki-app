package com.myoptimind.suzuki_app.features.compare_motorcycles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.compare_motorcycles.data.MotorPosition
import com.myoptimind.suzuki_app.features.compare_motorcycles.data.MotorSingleDetail
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_compare_models.*

@AndroidEntryPoint
class CompareModelsFragment() : TitleOnlyFragment() {

    override fun getTitle() = "Compare Models"
    val viewModel: CompareModelsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_compare_models, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.firstMotorcycleList.observe(viewLifecycleOwner) { result ->
            val adapter: ArrayAdapter<String> = ArrayAdapter(
                    requireContext(), android.R.layout.simple_dropdown_item_1line,
                    result.map { it.name }
            )
            tv_filter_value.setAdapter(adapter)
            tv_filter_value.apply {
                setOnClickListener { showDropDown() }
                setOnItemClickListener { _, _, i, _ ->
                    this.setText(adapter.getItem(i), false)
                    tv_filter_value.tag = result[i].id
                    viewModel.initMotorcycle(result[i].id,MotorPosition.FIRST)
                }
            }

            val adapter2: ArrayAdapter<String> = ArrayAdapter(
                    requireContext(), android.R.layout.simple_dropdown_item_1line,
                    result.map { it.name }
            )
            tv_filter_value_2.setAdapter(adapter2)
            tv_filter_value_2.apply {
                setOnClickListener { showDropDown() }
                setOnItemClickListener { _, _, i, _ ->
                    this.setText(adapter2.getItem(i), false)
                    tv_filter_value_2.tag = result[i].id
                    viewModel.initMotorcycle(result[i].id,MotorPosition.SECOND)

                }
            }
        }

        val compareModelsDetailAdapter = CompareModelsDetailAdapter(ArrayList(),ArrayList())
        rv_motor_details_1.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        rv_motor_details_1.adapter = compareModelsDetailAdapter

        viewModel.firstMotorcycleImageAndLogo.observe(viewLifecycleOwner){
            if(it != null){
                iv_motorcycle_1.visibility = View.VISIBLE
                iv_motorcycle_logo_1.visibility = View.VISIBLE
                group_no_motorcycles.visibility = View.GONE
                Glide.with(requireContext())
                        .load(it?.image)
                        .centerInside()
                        .into(iv_motorcycle_1)

                Glide.with(requireContext())
                        .load(it?.logo)
                        .centerInside()
                        .into(iv_motorcycle_logo_1)
            }else{
                iv_motorcycle_1.visibility = View.INVISIBLE
                iv_motorcycle_logo_1.visibility = View.INVISIBLE
            }

        }

        viewModel.secondMotorcycleImageAndLogo.observe(viewLifecycleOwner){
            if(it != null) {
                iv_motorcycle_2.visibility = View.VISIBLE
                iv_motorcycle_logo_2.visibility = View.VISIBLE
                group_no_motorcycles.visibility = View.GONE
                Glide.with(requireContext())
                        .load(it?.image)
                        .centerInside()
                        .into(iv_motorcycle_2)
                Glide.with(requireContext())
                        .load(it?.logo)
                        .centerInside()
                        .into(iv_motorcycle_logo_2)
            }else{
                iv_motorcycle_2.visibility = View.INVISIBLE
                iv_motorcycle_logo_2.visibility = View.INVISIBLE
            }
        }

        viewModel.firstList.observe(viewLifecycleOwner){ list ->
            if(!list.isNullOrEmpty()){
                compareModelsDetailAdapter.firstMotorDetailsList = list
                compareModelsDetailAdapter.notifyDataSetChanged()
            }

        }

        viewModel.secondList.observe(viewLifecycleOwner){ list ->
            if(!list.isNullOrEmpty()){
                compareModelsDetailAdapter.secondMotorDetailsList = list
                compareModelsDetailAdapter.notifyDataSetChanged()
            }
        }
    }

}