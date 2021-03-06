package com.myoptimind.suzuki_motors.features.service_centers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_list_of_services.*

@AndroidEntryPoint
class ServiceCentersListDialog : BaseDialogFragment() {

    private val viewModel: ServiceCentersViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ib_close.setOnClickListener {
            this.dismiss()
        }

        viewModel.services.observe(viewLifecycleOwner) { services ->
            val adapter = ServiceCentersListDialogAdapter(services)
            rv_services.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            rv_services.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_list_of_services,container,false)
    }
}