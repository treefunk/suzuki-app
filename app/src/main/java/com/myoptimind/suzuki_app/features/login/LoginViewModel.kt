package com.myoptimind.suzuki_app.features.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.login.api.LoginService
import com.myoptimind.suzuki_app.features.shared.AppSharedPref
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.shared.getMessage
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel @ViewModelInject constructor(
    private val loginRepository: LoginRepository,
    private val appSharedPref: AppSharedPref
) : ViewModel() {

    val featuredMotorcycle = MutableLiveData<Result<LoginService.FeaturedMotorcycleResponse>>()

    val loginResult          = MutableLiveData<Result<LoginService.LoginResponse>>()
    val registrationResult   = MutableLiveData<Result<LoginService.RegisterUserResponse>>()
    val forgotPasswordResult = MutableLiveData<Result<LoginService.ForgotPasswordResponse>>()

    init {

    }

    fun getFeaturedMotorcycles(){
        viewModelScope.launch(IO) {
            val response = loginRepository.getFeaturedMotorcycles()
            featuredMotorcycle.postValue(Result.Success(response))
        }
    }

    fun loginUser(emailAddress: String, password: String) {
        viewModelScope.launch(IO){
            loginResult.postValue(Result.Loading)
            try{
                val response = loginRepository.authenticateUser(emailAddress,password)
                loginResult.postValue(Result.Success(response))
                val user = response.data
                appSharedPref.storeLoginCredentials(
                        user.id,
                        user.fullname,
                        user.emailAddress,
                        user.profilePicture
                )

            }catch (exception: Exception){
                if(exception is HttpException){
                    val message = exception.getMessage()
                    loginResult.postValue(Result.Error(Exception(message)))
                }else{
                    loginResult.postValue(Result.Error(exception))
                }
            }
        }

    }

    fun loginSocialUser(emailAddress: String, socialToken: String, fullname: String){
        viewModelScope.launch(IO){
            loginResult.postValue(Result.Loading)
            try{
                val response = loginRepository.authenticateSocialLoginUser(emailAddress,socialToken,fullname)
                loginResult.postValue(Result.Success(response))
                val user = response.data
                appSharedPref.storeLoginCredentials(
                        user.id,
                        user.fullname,
                        user.emailAddress,
                        user.profilePicture
                )
            }catch(exception: Exception){
                if(exception is HttpException){
                    val message = exception.getMessage()
                    loginResult.postValue(Result.Error(Exception(message)))
                }else{
                    loginResult.postValue(Result.Error(exception))
                }
            }
        }
    }

    fun registerUser(fullname: String, emailAddress: String, password:String){
        viewModelScope.launch(IO) {
            try{
                val response = loginRepository.registerUser(fullname,emailAddress,password)
                registrationResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                if(exception is HttpException){
                    val message = exception.getMessage()
                    registrationResult.postValue(Result.Error(Exception(message)))
                }else{
                    registrationResult.postValue(Result.Error(exception))
                }
            }
        }
    }

    fun requestForgotPassword(emailAddress: String){
        viewModelScope.launch(IO) {
            try{
                forgotPasswordResult.postValue(Result.Loading)
                val response = loginRepository.requestForgotPassword(emailAddress)
                forgotPasswordResult.postValue(Result.Success(response))

            }catch (exception: Exception){
                if(exception is HttpException){
                    val message = exception.getMessage()
                    forgotPasswordResult.postValue(Result.Error(Exception(message)))
                }else{
                    forgotPasswordResult.postValue(Result.Error(exception))
                }

            }
        }
    }

    fun clearLoginResult(){
        loginResult.value = null
        registrationResult.value = null
        forgotPasswordResult.value = null
    }
}