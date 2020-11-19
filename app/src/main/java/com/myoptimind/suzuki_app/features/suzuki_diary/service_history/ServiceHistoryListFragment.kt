package com.myoptimind.suzuki_app.features.suzuki_diary.service_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_service_history_list.*
import kotlinx.android.synthetic.main.fragment_service_history_list.group_motorcycles
import kotlinx.android.synthetic.main.fragment_service_history_list.group_no_motorcycles
import timber.log.Timber

private const val NO_NOTIFY_INDEX = -1
private const val INSERT_NOTIFY_INDEX = -2
@AndroidEntryPoint
class ServiceHistoryListFragment (): TitleOnlyFragment(){

    override fun getTitle() = "My Suzuki Diary"
    private val viewModel: ServiceHistoryViewModel by activityViewModels()
    private var adapter: ServiceHistoryAdapter? = null
    val args: ServiceHistoryListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_service_history_list,container,false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getServiceHistoryList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ServiceHistoryAdapter(ArrayList())
        rv_service_history.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rv_service_history.adapter

        initClickListeners()
        initObservers()

        when(args.indexToUpdate){
            NO_NOTIFY_INDEX -> { }
            INSERT_NOTIFY_INDEX -> adapter?.notifyDataSetChanged()
            else -> adapter?.notifyItemChanged(args.indexToUpdate)
        }

    }


    private fun initObservers() {


        viewModel.serviceHistoryList.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    val serviceHistoryList = result.data.data
                    if(serviceHistoryList.isNotEmpty()){
                        group_no_motorcycles.visibility = View.GONE
                        group_motorcycles.visibility = View.VISIBLE
                        adapter = ServiceHistoryAdapter(serviceHistoryList,object: ServiceHistoryAdapter.ServiceHistoryListener {
                            override fun onEdit(index: Int) {
                                ServiceHistoryListFragmentDirections.actionServiceHistoryListFragmentToAddServiceHistoryFragment(index).also {
                                    findNavController().navigate(it)
                                    viewModel.currentIndex = index
                                }
                            }

                            override fun onMoreInfo(index: Int) {
                                ServiceHistoryListFragmentDirections.actionServiceHistoryListFragmentToSelectedServiceHistoryFragment(index).also {
                                    findNavController().navigate(it)
                                    viewModel.currentIndex = index
                                }
                            }
                        })
                        rv_service_history.adapter = adapter
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

    private fun initClickListeners() {
        btn_add_service_history.setOnClickListener {
            toAddServiceHistoryScreen()
        }
        btn_add_service_history_2.setOnClickListener {
            toAddServiceHistoryScreen()
        }
    }

    private fun toAddServiceHistoryScreen(){
        ServiceHistoryListFragmentDirections.actionServiceHistoryListFragmentToAddServiceHistoryFragment().also {
            findNavController().navigate(it)
            viewModel.currentIndex = INSERT_NOTIFY_INDEX
        }
    }
}