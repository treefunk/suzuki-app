package com.myoptimind.suzuki_app.features.sidenav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import kotlinx.android.synthetic.main.fragment_content_only.*

class TermsAndConditionsFragment(): TitleOnlyFragment() {
    override fun getTitle() = "Terms and Conditions"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_body.text = "Terms and conditions"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_content_only,container,false)
    }
}