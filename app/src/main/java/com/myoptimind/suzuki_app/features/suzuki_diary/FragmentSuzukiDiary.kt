package com.myoptimind.suzuki_app.features.suzuki_diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackgroundTintList
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_suzuki_diary.*
import kotlinx.android.synthetic.main.partial_nav_bottom.*


@AndroidEntryPoint
class FragmentSuzukiDiary(): TitleOnlyFragment(),View.OnClickListener {
    override fun getTitle() = "My Suzuki Diary"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_suzuki_diary,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_my_motorcycles.setOnClickListener(this)
        btn_service_history.setOnClickListener(this)
        Navigation.findNavController(requireActivity(),R.id.fragment_container).addOnDestinationChangedListener { _, destination, _ ->

            btn_my_motorcycles.background.setTint(ContextCompat.getColor(requireContext(), R.color.color_blue_300))
            btn_service_history.background.setTint(ContextCompat.getColor(requireContext(), R.color.color_blue_300))

            when (destination.id) {
                R.id.myMotorcyclesListFragment,R.id.selectedRegisteredMotorcycleFragment,R.id.registerMotorcycleFragment -> {
                    btn_my_motorcycles.background.setTint(ContextCompat.getColor(requireContext(), R.color.color_blue_150))
                }
                R.id.serviceHistoryListFragment, R.id.addServiceHistoryFragment,R.id.addServiceHistoryMaintenanceFragment,R.id.selectedServiceHistoryFragment -> {
                    btn_service_history.background.setTint(ContextCompat.getColor(requireContext(), R.color.color_blue_150))
                }
            }

        }
    }

    override fun onClick(v: View?) {
        when(v){
            btn_my_motorcycles -> Navigation.findNavController(requireActivity(),R.id.fragment_container).navigate(R.id.action_global_myMotorcyclesListFragment)
            btn_service_history -> Navigation.findNavController(requireActivity(),R.id.fragment_container).navigate(R.id.action_global_serviceHistoryListFragment)
        }
    }
}