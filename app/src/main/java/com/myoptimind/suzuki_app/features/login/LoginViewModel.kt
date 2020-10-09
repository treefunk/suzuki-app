package com.myoptimind.suzuki_app.features.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.login.api.LoginService
import com.myoptimind.suzuki_app.shared.AppSharedPref
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import com.myoptimind.suzuki_app.shared.api.Result

class LoginViewModel @ViewModelInject constructor(
    private val loginRepository: LoginRepository,
    private val appSharedPref: AppSharedPref
) : ViewModel() {

    val featuredMotorcycle = MutableLiveData<Result<LoginService.FeaturedMotorcycleResponse>>()

    val loginResult          = MutableLiveData<Result<LoginService.LoginResponse>>()
    val registrationResult   = MutableLiveData<Result<LoginService.RegisterUserResponse>>()
    val forgotPasswordResult = MutableLiveData<Result<LoginService.ForgotPasswordResponse>>()

    init {
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
                appSharedPref.storeUserLogin(response.data.id)
            }catch (exception: Exception){
                loginResult.postValue(Result.Error(exception))
            }
        }
    }

    fun registerUser(fullname: String, emailAddress: String, password:String){
        viewModelScope.launch(IO) {
            try{
                val response = loginRepository.registerUser(fullname,emailAddress,password)
                registrationResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                registrationResult.postValue(Result.Error(exception))
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
                forgotPasswordResult.postValue(Result.Error(exception))
            }
        }
    }
}