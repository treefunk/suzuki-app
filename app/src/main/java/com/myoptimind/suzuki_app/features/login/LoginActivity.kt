package com.myoptimind.suzuki_app.features.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.login.data.LoginFeaturedMotorcycle
import com.myoptimind.suzuki_app.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val adapter = LoginFeaturedMotorcycleAdapter(ArrayList())
        vp_featured_motorcycle.adapter = adapter
        vp_featured_motorcycle.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(tablayout_login_featured,vp_featured_motorcycle){ _, _ ->
            // Empty
        }.attach()


        viewModel.featuredMotorcycle.observe(this) { result ->
            when(result){
                is Result.Success -> {
                    adapter.loginFeaturedMotorcycles = result.data.data
                    vp_featured_motorcycle.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            val activeItem = adapter.loginFeaturedMotorcycles[position]
                            initActiveFeaturedMotorcycle(activeItem)
                        }
                    })
                    adapter.notifyDataSetChanged()
                }
                is Result.Error -> Timber.v(result.toString()) // TODO
                Result.Loading -> Timber.v(result.toString()) // TODO
            }
        }
    }

    private fun initActiveFeaturedMotorcycle(activeItem: LoginFeaturedMotorcycle) {
        Glide.with(this)
                .load(activeItem.background)
                .centerCrop()
                .into(iv_header_background_image)

        Glide.with(this)
                .load(activeItem.logo)
                .centerInside()
                .into(iv_header_logo)
    }


}