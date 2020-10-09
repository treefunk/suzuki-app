package com.myoptimind.suzuki_app.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.myoptimind.suzuki_app.MainActivity
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.shared.LogoOnlyFragment
import com.myoptimind.suzuki_app.shared.SearchableFragment
import kotlinx.android.synthetic.main.fragment_full_menu.*

class FullMenuFragment : LogoOnlyFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_whats_new.setOnClickListener {
            findNavController().navigate(R.id.action_fullMenuFragment_to_whatsNewFragment)
        }

        iv_customer_care.setOnClickListener {
            findNavController().navigate(R.id.action_fullMenuFragment_to_customerCareFragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val parentActivity = activity as MainActivity
        parentActivity.hideBottomNav(true)
        return inflater.inflate(R.layout.fragment_full_menu,container,false)
    }

    override fun onDestroy() {
        val parentActivity = activity
        parentActivity as MainActivity
        parentActivity.hideBottomNav(false)
        super.onDestroy()
    }
}