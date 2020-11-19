package com.myoptimind.suzuki_app.features.shared

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.myoptimind.suzuki_app.MainActivity
import kotlinx.android.synthetic.main.partial_nav_top.*
import timber.log.Timber

abstract class SearchableFragment : BaseFragment() {

    abstract fun onClickSearch(keyword: String)

    abstract fun getHint(): String


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Timber.v("on prepare options")
        parentActivity.et_top_search.hint = getHint()
        parentActivity.showSearch()
        parentActivity.searchListener = object : MainActivity.SearchListener {
            override fun onSearch(keyword: String) {
                onClickSearch(keyword)
            }
        }
//        parentActivity.et_top_search.text.clear()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hideLoading()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}