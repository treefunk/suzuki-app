package com.myoptimind.suzuki_app.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.InfoDialogFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import kotlinx.android.synthetic.main.fragment_forgot_password.*

private const val DIALOG_TAG_FORGOT_PASSWORD = "dialog_forgot_password"
class ForgotPasswordFragment: BaseLoginFragment(), View.OnClickListener {

    val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forgot_password,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()

        viewModel.forgotPasswordResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    InfoDialogFragment.newInstance(
                            "Thank you for your inquiry",
                            result.data.meta.message
                    ).show(childFragmentManager, DIALOG_TAG_FORGOT_PASSWORD)
                    viewModel.clearLoginResult()
                    setActiveWidgets(true)
                    hideLoading()
                }
                is Result.Error -> {
                    InfoDialogFragment.newInstance(
                            result.error.message.toString(),
                            ""
                    ).show(childFragmentManager, DIALOG_TAG_FORGOT_PASSWORD)
                    viewModel.clearLoginResult()
                    setActiveWidgets(true)
                    hideLoading()
                }
                Result.Loading -> {
                    setActiveWidgets(false)
                    showLoading()
                }
            }
        }
    }

    private fun setActiveWidgets(enabled: Boolean) {
        et_email_mobile.isEnabled = enabled
        btn_send.isEnabled = enabled
        tv_create_account.isEnabled = enabled
        tv_sign_in_link.isEnabled = enabled
    }

    private fun initClickListeners() {
        tv_create_account.setOnClickListener(this)
        tv_sign_in_link.setOnClickListener(this)
        btn_send.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        view?.id.let {
            when(it){
                R.id.tv_create_account -> findNavController().navigate(R.id.action_forgotPasswordFragment_to_signUpFragment)
                R.id.tv_sign_in_link   -> findNavController().navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
                R.id.btn_send          -> {
                    if(et_email_mobile.text.trim().isNotEmpty()){
                        viewModel.requestForgotPassword(et_email_mobile.text.toString().trim())
                    }else{
                        Toast.makeText(requireContext(),"Please input your email address.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}