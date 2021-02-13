package com.myoptimind.suzuki_motors.features.sidenav.changepass

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_motors.features.login.LoginRepository
import com.myoptimind.suzuki_motors.features.login.api.LoginService
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.shared.getMessage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChangePasswordViewModel @ViewModelInject constructor(
    private val loginRepository: LoginRepository
): ViewModel () {

    val changePasswordResult: LiveData<Result<LoginService.ChangePasswordResponse>> get() = _changePasswordResult
    private val _changePasswordResult = MutableLiveData<Result<LoginService.ChangePasswordResponse>>()

    fun changePassword(
           userId: String,
           oldPassword: String,
           newPassword: String,
           confirmPassword: String
    ){
        viewModelScope.launch(IO){
            try{
                _changePasswordResult.postValue(Result.Loading)
                val response = loginRepository.changePassword(userId,oldPassword,newPassword,confirmPassword)
                _changePasswordResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                if(exception is HttpException){
                    val message = exception.getMessage()
                    _changePasswordResult.postValue(Result.Error(Exception(message)))
                }else{
                    _changePasswordResult.postValue(Result.Error(exception))
                }
            }
        }
    }
}