package com.myoptimind.suzuki_motors.features.shared

import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.partial_nav_top.*

abstract class TitleOnlyFragment : BaseFragment() {


    abstract fun getTitle(): String

    fun setNewTitle(title: String) {
        parentActivity.nav_top_title.text = title
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        parentActivity.showTitleOnly()
        parentActivity.nav_top_title.text = getTitle()
        hideLoading()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}