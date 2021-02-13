package com.myoptimind.suzuki_motors.features.suzuki_diary.my_motorcycles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_motors.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_motorcycles.*


@AndroidEntryPoint
class MyMotorcyclesListFragment(): TitleOnlyFragment() {

    override fun getTitle() = "My Suzuki Diary"
    private val viewModel: MyMotorcyclesViewModel by activityViewModels()
    private var adapter: MyMotorcyclesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.initMyRegisteredMotorcycles()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_my_motorcycles,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MyMotorcyclesAdapter(ArrayList())

        rv_my_motorcycles.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        rv_my_motorcycles.adapter = adapter

        initObservers()
        initClickListeners()
    }

    private fun initClickListeners() {
        btn_register_motorcycle.setOnClickListener { navigateToRegister() }
        btn_register_motorcycle_2.setOnClickListener { navigateToRegister() }
    }

    private fun navigateToRegister(){
        MyMotorcyclesListFragmentDirections.actionMyMotorcyclesListFragmentToRegisterMotorcycleFragment().also {
            findNavController().navigate(it)
        }
    }

    private fun initObservers() {
        viewModel.registeredMotorcyclesResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    group_no_motorcycles.visibility = View.GONE
                    group_motorcycles.visibility = View.VISIBLE
                    val registeredMotorcycles = result.data.data
                    if(registeredMotorcycles.isNotEmpty()){
                        adapter = MyMotorcyclesAdapter(registeredMotorcycles,object: MyMotorcyclesAdapter.RegisteredMotorcycleListener {
                            override fun onClickEdit(index: Int) { // pass data
                                MyMotorcyclesListFragmentDirections.actionMyMotorcyclesListFragmentToRegisterMotorcycleFragment(index).also {
                                    findNavController().navigate(it)
                                }
                            }

                            override fun onClickMoreInfo(index: Int) { // pass index
                                MyMotorcyclesListFragmentDirections.actionMyMotorcyclesListFragmentToSelectedRegisteredMotorcycleFragment(index).also {
                                    findNavController().navigate(it)
                                }
                            }
                        })
                        rv_my_motorcycles.adapter = adapter
                        adapter?.notifyDataSetChanged()
                    }else{
                        group_no_motorcycles.visibility = View.VISIBLE
                        group_motorcycles.visibility = View.GONE
                    }
                    hideLoading()
                }
                is Result.Error -> {
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                }
            }
        }
    }


}