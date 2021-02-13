package com.myoptimind.suzuki_motors.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_motors.MainActivity
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.InfoDialogFragment
import com.myoptimind.suzuki_motors.features.shared.LogoOnlyFragment
import kotlinx.android.synthetic.main.fragment_full_menu.*

class FullMenuFragment : LogoOnlyFragment(), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        initImages()
    }

    override fun onClick(view: View?) {
        when(view){
            iv_motorcycle_models -> findNavController().navigate(R.id.action_global_motorcycleModelsFragment)
            iv_spare_parts -> {
                //todo: enable this after update
                InfoDialogFragment.newInstance("Sorry!","Contents for spare parts are coming soon.").show(parentFragmentManager,"spare_parts_disable")
//                findNavController().navigate(R.id.action_global_sparePartsFragment)
            }
            iv_dealer_locator -> findNavController().navigate(R.id.action_global_dealerLocatorsFragment)
            iv_whats_new -> findNavController().navigate(R.id.action_fullMenuFragment_to_whatsNewFragment)
            iv_customer_care -> findNavController().navigate(R.id.action_fullMenuFragment_to_customerCareFragment)
            iv_service_centers -> findNavController().navigate(R.id.action_global_serviceCentersFragment)
            iv_safety_tips -> findNavController().navigate(R.id.action_fullMenuFragment_to_safetyTipsFragment)
            iv_my_suzuki_diary -> findNavController().navigate(R.id.action_fullMenuFragment_to_fragmentSuzukiDiary)
            iv_compare_models -> findNavController().navigate(R.id.action_fullMenuFragment_to_compareModelsFragment)

            iv_extract_data -> findNavController().navigate(R.id.action_fullMenuFragment_to_extractDataFragment)
            ib_close -> findNavController().popBackStack()
        }
    }

    private fun initImages() {
        iv_motorcycle_models.initImage(R.drawable.ic_menu_motorcycles)
        iv_spare_parts.initImage(R.drawable.ic_menu_spare_parts)
        iv_dealer_locator.initImage(R.drawable.ic_menu_dealer_locator)
        iv_whats_new.initImage(R.drawable.ic_menu_whats_new)
        iv_customer_care.initImage(R.drawable.ic_menu_customer_care)
        iv_service_centers.initImage(R.drawable.ic_menu_service_centers)
        iv_safety_tips.initImage(R.drawable.ic_menu_safety_tips)
        iv_my_suzuki_diary.initImage(R.drawable.ic_menu_suzuki_diary)
        iv_compare_models.initImage(R.drawable.ic_menu_compare_models)
        iv_extract_data.initImage(R.drawable.ic_menu_extract_data)
        ib_close.initImage(R.drawable.ic_close)
    }

    private fun ImageView.initImage(imageDrawable: Int){
        this.setOnClickListener(this@FullMenuFragment)
        Glide.with(this@FullMenuFragment)
                .load(imageDrawable)
                .fitCenter()
                .into(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_full_menu,container,false)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideBottomNav(true);
    }



    override fun onPause() {
        super.onPause()
        (activity as MainActivity).hideBottomNav(false)
    }


}