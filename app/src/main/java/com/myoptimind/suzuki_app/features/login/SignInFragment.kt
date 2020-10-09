package com.myoptimind.suzuki_app.features.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.myoptimind.suzuki_app.MainActivity
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.shared.AppSharedPref
import com.myoptimind.suzuki_app.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_in.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment: Fragment(), View.OnClickListener {

    @Inject lateinit var sharedPref: AppSharedPref
    var callbackManager: CallbackManager? = null

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        if(isUserLoggedIn()){
//            redirectAuthenticatedUser()
        }
        super.onCreate(savedInstanceState)
    }

    private fun checkSocialMediaLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
        FacebookSdk.sdkInitialize(requireContext().applicationContext)

        callbackManager = CallbackManager.Factory.create()

        ib_facebook_button.apply {
            setReadPermissions("email")
            registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Log.v("fblogin", result.toString())
                    val request = GraphRequest.newMeRequest(
                            result!!.accessToken
                    ) { `object`, response ->
                        // Application code
                        Log.v("fblogin", "response ${response.rawResponse}")
                    }

//                    parameters.putString("fields", "id,name,link")
//                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    Log.v("fblogin", "cancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.v("fblogin", "error")
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        initObservers()
    }

    override fun onClick(view: View?) {
        view?.id.let{
            when(it){
                R.id.tv_create_account  -> findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
                R.id.tv_forgot_password -> findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
                R.id.btn_sign_in        -> viewModel.loginUser(et_email_mobile.text.toString(),et_password.text.toString())
                R.id.ib_facebook_button -> checkSocialMediaLogin()
            }
        }
    }

    private fun isUserLoggedIn(): Boolean = sharedPref.getUserId() != null

    private fun redirectAuthenticatedUser(){
        Timber.v("User Id ${sharedPref.getUserId()} is currently logged in..")
        startActivity(
                Intent(requireContext(),MainActivity::class.java)
        )
    }

    private fun initObservers() {
        viewModel.loginResult.observe(viewLifecycleOwner){ loginResult ->
            when(loginResult){
                is Result.Success -> {
                    Toast.makeText(requireContext(),"successfully logged in",Toast.LENGTH_SHORT).show()
                    startActivity(
                            Intent(requireContext(),MainActivity::class.java)
                    )
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(),"Invalid Username or password",Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> {
                    //todo
                }
            }
        }
    }

    private fun initClickListeners() {
        tv_create_account.setOnClickListener(this)
        tv_forgot_password.setOnClickListener(this)
        btn_sign_in.setOnClickListener(this)
        ib_facebook_button.setOnClickListener(this)
    }


}