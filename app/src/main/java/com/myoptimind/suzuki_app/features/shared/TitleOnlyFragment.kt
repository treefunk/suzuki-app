package com.myoptimind.suzuki_app.features.shared

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.myoptimind.suzuki_app.MainActivity
import kotlinx.android.synthetic.main.partial_nav_top.*
import timber.log.Timber

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