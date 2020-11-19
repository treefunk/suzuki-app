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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.myoptimind.suzuki_app.MainActivity
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.InfoDialogFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.shared.validateEmail
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.et_password
import kotlinx.android.synthetic.main.fragment_sign_up.ib_facebook_button
import kotlinx.android.synthetic.main.fragment_sign_up.ib_google_button
import timber.log.Timber
private const val RC_SIGN_IN = 100


private const val DIALOG_TAG_REGISTRATION = "dialog_tag_registration"

class SignUpFragment: BaseLoginFragment(), View.OnClickListener {

    private val viewModel: LoginViewModel by activityViewModels()
    private var callbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

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
                    clearTextFields()
                    setActiveWidgets(true)
                    viewModel.clearLoginResult()
                    hideLoading()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(),result.error.toString(), Toast.LENGTH_SHORT).show()
                    setActiveWidgets(true)
                    viewModel.clearLoginResult()
                    hideLoading()
                }
                Result.Loading -> {
                    setActiveWidgets(false)
                    showLoading()
                }
            }
        }
        viewModel.loginResult.observe(viewLifecycleOwner){ loginResult ->
            when(loginResult){
                is Result.Success -> {
                    Toast.makeText(requireContext(), loginResult.data.meta.message, Toast.LENGTH_SHORT).show()
                    viewModel.clearLoginResult()
                    setActiveWidgets(true)
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

    private fun clearTextFields() {
        et_full_name.setText("")
        et_email.setText("")
        et_password.setText("")
    }

    private fun initClickListeners() {
        btn_submit.setOnClickListener(this)
        tv_sign_in_link.setOnClickListener(this)
        ib_facebook_button.setOnClickListener(this)
        ib_google_button.setOnClickListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up,container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureGoogleClient()

    }

    override fun onClick(view: View?) {
        view?.id.let {
            when(it){
                R.id.btn_submit      -> {
                    if(et_full_name.text.toString().trim().isNotEmpty() &&
                            et_email.text.toString().trim().isNotEmpty() &&
                            et_password.text.toString().trim().isNotEmpty()){
                        if(!et_email.text.toString().validateEmail()){
                            Toast.makeText(requireContext(),"Invalid E-mail format.",Toast.LENGTH_LONG).show()
                            return
                        }
                        if(et_password.text.toString().length <= 5){
                            Toast.makeText(requireContext(),"Password must be at least 6 characters.",Toast.LENGTH_LONG).show()
                            return
                        }
                        viewModel.registerUser(
                                et_full_name.text.toString(),
                                et_email.text.toString(),
                                et_password.text.toString()
                        )
                    }else{
                        Toast.makeText(requireContext(),"Please fill in required fields.",Toast.LENGTH_LONG).show()
                    }

                }
                R.id.ib_facebook_button -> LoginManager.getInstance().logInWithReadPermissions(this@SignUpFragment, listOf("email"))
                R.id.ib_google_button -> googleLogin()
                R.id.tv_sign_in_link -> findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        }
    }

    private fun setActiveWidgets(enabled: Boolean){
        et_full_name.isEnabled = enabled
        et_email.isEnabled = enabled
        et_password.isEnabled = enabled
        btn_submit.isEnabled = enabled
        tv_sign_in_link.isEnabled = enabled
        ib_facebook_button.isEnabled = enabled
        ib_google_button.isEnabled = enabled
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // facebook login
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

    private fun configureGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
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
                Timber.v("ON ERROR")
            }
        })
    }

    private fun googleLogin(){
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


}