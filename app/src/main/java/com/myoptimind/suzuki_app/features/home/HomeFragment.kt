package com.myoptimind.suzuki_app.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.home.HomeFeaturedProductAdapter
import com.myoptimind.suzuki_app.features.home.HomeViewModel
import com.myoptimind.suzuki_app.features.shared.LogoOnlyFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment: LogoOnlyFragment() {

    val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HomeFeaturedProductAdapter(ArrayList(), object : HomeFeaturedProductAdapter.HomeFeaturedListener {
            override fun onClick(contentType: HomeFeaturedProductAdapter.ContentType) {
                when (contentType) {
                    HomeFeaturedProductAdapter.ContentType.MOTORCYCLE -> {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_container).navigate(R.id.action_global_motorcycleModelsFragment)
                    }
                    HomeFeaturedProductAdapter.ContentType.DEALER -> {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_container).navigate(R.id.action_global_dealerLocatorsFragment)
                    }
                    HomeFeaturedProductAdapter.ContentType.EVENT -> {

                    }
                    HomeFeaturedProductAdapter.ContentType.SERVICE_CENTER -> {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_container).navigate(R.id.action_global_serviceCentersFragment)

                    }
                    HomeFeaturedProductAdapter.ContentType.SAFETY_TIP -> {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_container).navigate(R.id.action_global_safetyTipsFragment)

                    }
                    HomeFeaturedProductAdapter.ContentType.ARTICLE -> {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_container).navigate(R.id.action_global_whatsNewFragment)

                    }
                }
            }
        })
        vp_featured_products.adapter = adapter
        vp_featured_products.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(tablayout_home_featured,vp_featured_products){ _, _ ->
            // Empty
        }.attach()

        viewModel.homeFeaturedResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    adapter.homeFeaturedProducts = result.data.data
                    adapter.notifyDataSetChanged()
                    hideLoading()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(),"error fetching home",Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                }
            }
        }
    }

}