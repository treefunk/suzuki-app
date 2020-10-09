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
import com.myoptimind.suzuki_app.shared.InfoDialogFragment
import com.myoptimind.suzuki_app.shared.api.Result
import kotlinx.android.synthetic.main.fragment_sign_up.*


private const val DIALOG_TAG_REGISTRATION = "dialog_tag_registration"

class SignUpFragment: Fragment(), View.OnClickListener {

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()

        viewModel.registrationResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    InfoDialogFragment.newInstance(
                            "Thank you for your\n registration",
                            "An email confirmation has been sent to your registered email."
                    ).show(childFragmentManager, DIALOG_TAG_REGISTRATION)
//                    findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(),result.error.toString(), Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> {
                    //todo
                }
            }
        }
    }

    private fun initClickListeners() {
        btn_submit.setOnClickListener(this)
        tv_sign_in_link.setOnClickListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up,container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onClick(view: View?) {
        view?.id.let {
            when(it){
                R.id.btn_submit      -> {
                    viewModel.registerUser(
                            et_full_name.text.toString(),
                            et_email.text.toString(),
                            et_password.text.toString()
                    )
                }
                R.id.tv_sign_in_link -> findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        }
    }

}