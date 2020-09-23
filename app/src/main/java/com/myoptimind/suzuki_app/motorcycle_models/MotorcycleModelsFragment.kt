package com.myoptimind.suzuki_app.motorcycle_models

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myoptimind.suzuki_app.R

class MotorcycleModelsFragment : Fragment() {

    companion object {
        fun newInstance() = MotorcycleModelsFragment()
    }

    private lateinit var viewModel: MotorcycleModelsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_motorcycle_models, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MotorcycleModelsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}