package com.myoptimind.suzuki_app.features.login

import android.os.Bundle
import androidx.fragment.app.Fragment

import timber.log.Timber

abstract class BaseLoginFragment: Fragment() {
    internal lateinit var parentActivity: LoginActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        try {
            parentActivity = (activity as LoginActivity)
        } catch (exception: TypeCastException) {
            Timber.e("Parent Activity must be of type \"LoginActivity\"!!")
        }
    }

    fun showLoading(){
        parentActivity.showLoading()
    }

    fun hideLoading(){
        parentActivity.hideLoading()
    }

}