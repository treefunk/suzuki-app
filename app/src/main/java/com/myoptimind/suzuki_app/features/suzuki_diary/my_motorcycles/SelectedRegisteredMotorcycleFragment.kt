package com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.data.RegisteredMotorcycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_selected_registered_motorcycle.*

@AndroidEntryPoint
class SelectedRegisteredMotorcycleFragment(): TitleOnlyFragment() {

    override fun getTitle() = "My Suzuki Diary"
    val viewModel: MyMotorcyclesViewModel by activityViewModels()
    val args: SelectedRegisteredMotorcycleFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_selected_registered_motorcycle,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(args.index != -1){
            viewModel.initRegisteredMotorcycleId(args.index)
        }else{
            viewModel.clearExistingMotorcycle()
        }

        viewModel.existingRegisteredMotorcycle.observe(viewLifecycleOwner){ registeredMotorcycle ->
            if(registeredMotorcycle != null){
                tv_beast_name.text = registeredMotorcycle.beastNickname
                tv_motorcycle_model.text = registeredMotorcycle.motorcycleModelName
                tv_engine_number.text = registeredMotorcycle.engineNumber
                tv_frame_number.text = registeredMotorcycle.frameNumber
                tv_date_purchased.text = registeredMotorcycle.datePurchased
                tv_purchased_in.text = registeredMotorcycle.purchasedIn
            }

        }
    }
}