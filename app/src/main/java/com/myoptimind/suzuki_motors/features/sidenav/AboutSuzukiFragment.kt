package com.myoptimind.suzuki_motors.features.sidenav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.TitleOnlyFragment
import kotlinx.android.synthetic.main.fragment_content_only.*

class AboutSuzukiFragment(): TitleOnlyFragment() {
    override fun getTitle() = "About Suzuki"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_body.text = "About us here"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_content_only,container,false)
    }
}