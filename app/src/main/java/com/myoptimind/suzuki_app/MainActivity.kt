package com.myoptimind.suzuki_app

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.facebook.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.partial_nav_bottom.*
import kotlinx.android.synthetic.main.partial_nav_top.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager
    var searchListener: SearchListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ib_menu_motorcycle.setOnClickListener {
            Navigation.findNavController(this,R.id.nav_host_container).navigate(R.id.action_global_motorcycleModelsFragment)
        }

        ib_menu_servicecenters.setOnClickListener {
            Navigation.findNavController(this,R.id.nav_host_container).navigate(R.id.action_global_serviceCentersFragment)
        }

        fab_submenu.setOnClickListener{
            findNavController(R.id.nav_host_container).navigate(R.id.action_global_fullMenuFragment)
        }
        ib_nav_back.setOnClickListener{
            onBackPressed()
        }

        ib_search.setOnClickListener {
            searchListener?.onSearch(et_top_search.text.toString())
        }

        initClickListeners()
    }

    private fun initClickListeners() {

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }




    fun hideBottomNav(hidden: Boolean){
        val visibility = if(hidden) View.GONE  else View.VISIBLE
        group_bottom_nav?.visibility = visibility
        fab_submenu.visibility = visibility
    }

    fun showSearch(show: Boolean){
        val visibility = if(show) View.VISIBLE  else View.GONE
        group_search.visibility = visibility
        if(ib_nav_back.visibility == View.GONE){
            ib_nav_back.visibility = View.VISIBLE
        }
        iv_nav_logo.visibility = View.GONE
//        nav_top_title.visibility = View.GONE
    }

    fun showLogoOnly(){
        if(ib_nav_back.visibility == View.VISIBLE){
            ib_nav_back.visibility = View.GONE
        }
        nav_top_title.visibility = View.GONE
        iv_nav_logo.visibility = View.VISIBLE
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    interface SearchListener {
        fun onSearch(keyword: String)
    }

}