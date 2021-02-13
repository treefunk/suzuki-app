package com.myoptimind.suzuki_motors.features.spare_parts

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.LogoOnlyFragment
import com.myoptimind.suzuki_motors.features.spare_parts.data.SparePartCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_spare_parts.*


@AndroidEntryPoint
class SparePartsFragment: LogoOnlyFragment(), View.OnClickListener {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImages()
    }

    private fun initImages() {
        iv_tires.initImage(R.drawable.ic_tires)
        iv_cooling_fan_filter.initImage(R.drawable.ic_cooling_fan_filter)
        iv_engine_oil.initImage(R.drawable.ic_engine_oil)
        iv_drive_belt.initImage(R.drawable.ic_drive_belt)
        iv_battery.initImage(R.drawable.ic_battery)
        iv_brake_fluid.initImage(R.drawable.ic_brake_fluid)
        iv_brake_pad.initImage(R.drawable.ic_brake_pad)
        iv_brake_shoe.initImage(R.drawable.ic_brake_shoe)
        iv_spark_plug.initImage(R.drawable.ic_spark_plug)
        iv_air_cleaner_element.initImage(R.drawable.ic_air_cleaner_element)
        iv_oil_filter.initImage(R.drawable.ic_oil_filter)
    }

    private fun ImageView.initImage(imageDrawable: Int){
        this.setOnClickListener(this@SparePartsFragment)
        Glide.with(this@SparePartsFragment)
                .load(imageDrawable)
                .fitCenter()
                .into(this)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_spare_parts,container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onClick(view: View?) {
        when(view){
            iv_tires -> navigateToSparePart(SparePartCategory.TIRES)
            iv_cooling_fan_filter -> navigateToSparePart(SparePartCategory.COOLING_FAN_FILTER)
            iv_engine_oil -> navigateToSparePart(SparePartCategory.ENGINE_OIL)
            iv_drive_belt -> navigateToSparePart(SparePartCategory.DRIVE_BELT)
            iv_battery -> navigateToSparePart(SparePartCategory.BATTERY)
            iv_brake_fluid -> navigateToSparePart(SparePartCategory.BRAKE_FLUID)
            iv_brake_pad -> navigateToSparePart(SparePartCategory.BRAKE_PAD)
            iv_brake_shoe -> navigateToSparePart(SparePartCategory.BRAKE_SHOE)
            iv_spark_plug -> navigateToSparePart(SparePartCategory.SPARK_PLUG)
            iv_air_cleaner_element -> navigateToSparePart(SparePartCategory.AIR_CLEANER_ELEMENT)
            iv_oil_filter -> navigateToSparePart(SparePartCategory.OIL_FILTER)
        }
    }

    private fun navigateToSparePart(sparePartCategory: SparePartCategory){
        SparePartsFragmentDirections.actionSparePartsFragmentToSelectedSparePartFragment(
                sparePartCategory.id,sparePartCategory.title).also {
            findNavController().navigate(it)
        }
    }

}