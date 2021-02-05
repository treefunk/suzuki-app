package com.myoptimind.suzuki_app.features.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.myoptimind.suzuki_app.MainActivity
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.login.data.LoginFeaturedMotorcycle
import com.myoptimind.suzuki_app.features.shared.AppSharedPref
import com.myoptimind.suzuki_app.features.shared.MyFirebaseMessagingService
import com.myoptimind.suzuki_app.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view_loading
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var job: Job
    @Inject
    lateinit var sharedPref: AppSharedPref

    private suspend fun switchScreen(){
        for (i in 1..3){
//            Log.v("Splashscreen", "${i}.. before switch")
            delay(1000)
        }
        if(isUserLoggedIn()){
            redirectAuthenticatedUser()
        }else{
            setContentView(R.layout.activity_login)
            loadFeatured()
        }

    }

    private fun loadFeatured() {
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
                    hideLoading()
                }
                is Result.Error -> hideLoading()
                Result.Loading -> showLoading()
            }
        }
    }
    private fun redirectAuthenticatedUser(){
        Timber.v("User Id ${sharedPref.getUserId()} is currently logged in..")



        val mainActivityIntent = Intent(this, MainActivity::class.java)
        intent.extras?.getString(MyFirebaseMessagingService.EXTRA_WHATS_NEW).also {
            Timber.d("extras - $it")
            if(it != null)
                mainActivityIntent.putExtra(MyFirebaseMessagingService.EXTRA_WHATS_NEW,it)
        }
        this@LoginActivity.finish()


        startActivity(
                mainActivityIntent
        )
    }

    private fun isUserLoggedIn(): Boolean = sharedPref.getUserId().isNotEmpty()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel.getFeaturedMotorcycles()

        setContentView(R.layout.fragment_splash_screen)


        job = lifecycleScope.launchWhenResumed {
            switchScreen()
        }
//        setContentView(R.layout.activity_login)



    }

    internal fun showLoading(){
        if(!view_loading.isVisible){
            view_loading.visibility = View.VISIBLE
        }
    }

    internal fun hideLoading(){
        if(view_loading.isVisible){
            view_loading.visibility = View.GONE
        }
    }

    private fun initActiveFeaturedMotorcycle(activeItem: LoginFeaturedMotorcycle) {
        Glide.with(this)
                .load(activeItem.background)
                .fitCenter()
                .into(iv_header_background_image)

        Glide.with(this)
                .load(activeItem.logo)
                .fitCenter()
                .into(iv_header_logo)
    }


}