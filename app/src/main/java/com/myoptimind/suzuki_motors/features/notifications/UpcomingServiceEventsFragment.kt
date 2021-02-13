package com.myoptimind.suzuki_motors.features.notifications

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
import com.myoptimind.suzuki_motors.features.extract_data.ExtractDataAdapter

import com.myoptimind.suzuki_motors.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.ServiceHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_upcoming_service_events.*

@AndroidEntryPoint
class UpcomingServiceEventsFragment (): TitleOnlyFragment() {
    override fun getTitle() = "UPCOMING SERVICE EVENTS"
    private var adapter: ExtractDataAdapter? = null


    private val viewModel: ServiceHistoryViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_upcoming_service_events,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getServiceHistoryList(3)
        adapter = ExtractDataAdapter(ArrayList())
        rv_upcoming_service_events.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_upcoming_service_events.adapter

        initObservers()

    }

    private fun initObservers() {
        viewModel.upcomingEventsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val serviceHistoryList = result.data.data
                    if (serviceHistoryList.isNotEmpty()) {
                        group_no_upcoming.visibility = View.INVISIBLE
                        rv_upcoming_service_events.visibility = View.VISIBLE

                        adapter = ExtractDataAdapter(serviceHistoryList, object : ExtractDataAdapter.ExtractDataListener {
                            override fun onMoreInfo(index: Int) {
                                UpcomingServiceEventsFragmentDirections.actionUpcomingServiceEventsFragmentToSelectedServiceHistoryFragment(index,getTitle()).also {
                                    findNavController().navigate(it)
                                }
                            }
                        })
                        rv_upcoming_service_events.adapter = adapter
                        adapter?.notifyDataSetChanged()
                    } else {
                        group_no_upcoming.visibility = View.VISIBLE
                        rv_upcoming_service_events.visibility = View.INVISIBLE
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