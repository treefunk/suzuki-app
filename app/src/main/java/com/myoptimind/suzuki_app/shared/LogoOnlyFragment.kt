package com.myoptimind.suzuki_app.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myoptimind.suzuki_app.MainActivity
import timber.log.Timber

abstract class LogoOnlyFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        try{
            val parentActivity = activity as MainActivity
            parentActivity.showLogoOnly()
        }catch (exception: TypeCastException){
            Timber.e("Parent Activity must be of type \"MainActivity\"!!")
        }
        super.onResume()
    }

}