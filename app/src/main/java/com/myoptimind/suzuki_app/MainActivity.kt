package com.myoptimind.suzuki_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_sign_in.*


class MainActivity : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sign_in)

/*        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired*/
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
        FacebookSdk.sdkInitialize(applicationContext)

        callbackManager = CallbackManager.Factory.create()
        btn_facebook.apply {
            setReadPermissions("email")
            registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    Log.v("fblogin",result.toString())
                    val request = GraphRequest.newMeRequest(
                            result!!.accessToken
                    ) { `object`, response ->
                        // Application code
                        Log.v("fblogin","response ${response.rawResponse}")
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,link")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    Log.v("fblogin","cancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.v("fblogin","error")
                }
            })
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}