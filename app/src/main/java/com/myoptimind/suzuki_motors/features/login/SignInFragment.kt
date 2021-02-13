package com.myoptimind.suzuki_motors.features.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.myoptimind.suzuki_motors.MainActivity
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.AppSharedPref
import com.myoptimind.suzuki_motors.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_in.*
import timber.log.Timber
import javax.inject.Inject


private const val RC_SIGN_IN = 100

@AndroidEntryPoint
class SignInFragment: BaseLoginFragment(), View.OnClickListener {

    @Inject lateinit var sharedPref: AppSharedPref
    private var callbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("smallest width qualifier - ${resources.configuration.smallestScreenWidthDp}")
        Timber.v("screen height dp - ${resources.configuration.screenHeightDp}")
        configureGoogleClient()



        super.onCreate(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            val name  = if(account.displayName != null) account.displayName else account.givenName
            val email = if(account.email != null) account.email else return
            val id    = if(account.id != null) account.id else return
            if(account.email != null && account.id != null && account.displayName != null){
                viewModel.loginSocialUser(
                        account.email.toString(),
                        account.id.toString(),
                        account.displayName.toString()
                )
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.e(e.message)
        }
    }

    private fun checkIfLoggedInFacebook(){

    }

    private fun facebookLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        callbackManager = CallbackManager.Factory.create()


        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Timber.v(" " + result.toString())
                val request = GraphRequest.newMeRequest(
                        result!!.accessToken
                ) { _, response ->
                    // Application code
                    Log.v("fblogin", "response ${response.rawResponse}")
                    val res = response.jsonObject
                    viewModel.loginSocialUser(res.getString("email"), res.getString("id"), res.getString("first_name") + " " + res.getString("last_name"))
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,email,first_name,last_name")
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {
                Timber.v("fb login cancelled")
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(requireContext(),error?.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun configureGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun googleLogin(){
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        facebookLogin()
        initClickListeners()
        initObservers()
    }

    override fun onClick(view: View?) {
        view?.id.let{
            when(it){
                R.id.tv_create_account -> findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
                R.id.tv_forgot_password -> findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
                R.id.btn_sign_in -> {
                    try{
                        viewModel.loginUser(et_email_mobile.text.toString(), et_password.text.toString())
                    }catch (exception: Exception){
                        Toast.makeText(requireContext(),exception.message,Toast.LENGTH_SHORT).show()
                    }
                }
//                R.id.ib_facebook_button -> facebookLogin()
                R.id.ib_google_button -> googleLogin()
            }
        }
    }

    private fun initObservers() {
        viewModel.loginResult.observe(viewLifecycleOwner){ loginResult ->
            when(loginResult){
                is Result.Success -> {
                    Toast.makeText(requireContext(), loginResult.data.meta.message, Toast.LENGTH_SHORT).show()
                    viewModel.clearLoginResult()
                    setActiveWidgets(true)
                    requireActivity().finish()
                    startActivity(
                            Intent(requireContext(), MainActivity::class.java)
                    )
                    hideLoading()
                }
                is Result.Error -> {
                    Timber.e("error ${loginResult.error.message}")
                    Toast.makeText(requireContext(), loginResult.error.message, Toast.LENGTH_SHORT).show()
                    mGoogleSignInClient?.signOut()
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

    private fun setActiveWidgets(enabled: Boolean){
        et_email_mobile.isEnabled = enabled
        et_password.isEnabled = enabled
        btn_sign_in.isEnabled = enabled
        tv_forgot_password.isEnabled = enabled
        tv_create_account.isEnabled = enabled
    }

    private fun initClickListeners() {
        tv_create_account.setOnClickListener(this)
        tv_forgot_password.setOnClickListener(this)
        btn_sign_in.setOnClickListener(this)
        ib_facebook_button.setOnClickListener(this)
        ib_google_button.setOnClickListener(this)
        ib_facebook_button.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this@SignInFragment, listOf("email"))
        }

    }


}