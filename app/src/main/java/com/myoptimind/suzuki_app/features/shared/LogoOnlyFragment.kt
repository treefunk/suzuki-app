package com.myoptimind.suzuki_app.features.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myoptimind.suzuki_app.MainActivity
import timber.log.Timber

abstract class LogoOnlyFragment: BaseFragment() {

    override fun onResume() {
        parentActivity.showLogoOnly()
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        hideLoading()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}