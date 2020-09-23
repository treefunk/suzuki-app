package com.myoptimind.suzuki_app

/*        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired*/
/*
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
*/