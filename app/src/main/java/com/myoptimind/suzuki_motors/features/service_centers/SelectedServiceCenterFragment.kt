package com.myoptimind.suzuki_motors.features.service_centers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_motors.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_selected_service_center.*

@AndroidEntryPoint
class SelectedServiceCenterFragment: TitleOnlyFragment() {

    override fun getTitle() = "FI Service Centers"
    private val args: SelectedServiceCenterFragmentArgs by navArgs()
    private val viewModel: SelectedServiceCenterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel.initSingleServiceCenter(args.id)
        return inflater.inflate(R.layout.fragment_selected_service_center,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        viewModel.singleServiceCenterResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    val serviceCenter = result.data.data
                    tv_service_center_name.text = serviceCenter.name
                    tv_service_center_caption.text = serviceCenter.city
                    tv_address.text = serviceCenter.fullAddress.trim()
                    tv_contact.text = serviceCenter.contactNumber
                    tv_mobile.text = serviceCenter.mobileNumber

                    if(!serviceCenter.latitude.isNullOrEmpty() && !serviceCenter.longitude.isNullOrBlank()){
                        map_placeholder.visibility = View.INVISIBLE
                        mapFragment.getMapAsync { googleMap ->
                            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

                            val latlong = LatLng(serviceCenter.latitude.toDouble(), serviceCenter.longitude.toDouble())
                            googleMap.addMarker(
                                    MarkerOptions()
                                            .position(latlong)
                                            .title(serviceCenter.name)
                            )
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong,16F))
                        }
                    }else{
                        map_placeholder.visibility = View.VISIBLE
                    }


                        btn_call.setOnClickListener {
                            val phone = serviceCenter.contactNumber
                            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "#789854", null))
                            startActivity(intent)
                        }



                        btn_send_email.setOnClickListener {
                            composeEmail(arrayOf("helpdesk@suzuki.com.ph"))
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
    fun composeEmail(addresses: Array<String>) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_EMAIL, addresses)
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }
}