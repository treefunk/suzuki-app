package com.myoptimind.suzuki_app.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myoptimind.suzuki_app.MainActivity
import kotlinx.android.synthetic.main.partial_nav_top.*
import timber.log.Timber

abstract class SearchableFragment: Fragment() {

    abstract fun onClickSearch(keyword: String)

    override fun onDestroy() {
            val parentActivity = activity
            parentActivity as MainActivity
            parentActivity.showLogoOnly()
            parentActivity.showSearch(false)
            super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        try{
            val parentActivity = activity as MainActivity
            parentActivity.showSearch(true)
            parentActivity.searchListener = object : MainActivity.SearchListener {
                override fun onSearch(keyword: String) {
                    onClickSearch(keyword)
                }
            }
            parentActivity.et_top_search.text.clear()
        }catch (exception: TypeCastException){
            Timber.e("Parent Activity must be of type \"MainActivity\"!!")
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

}