package com.myoptimind.suzuki_app.features

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.login.LoginRepository
import com.myoptimind.suzuki_app.features.login.api.LoginService
import com.myoptimind.suzuki_app.features.shared.AppSharedPref
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.ServiceHistoryRepository
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.api.ServiceHistoryService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(
        private val loginRepository: LoginRepository,
        private val appSharedPref: AppSharedPref,
        private val serviceHistoryRepository: ServiceHistoryRepository
) : ViewModel() {

    val updateProfilePictureResult: LiveData<Result<LoginService.EditProfilePictureResponse>> get() = _updateProfilePictureResult
    private val _updateProfilePictureResult = MutableLiveData<Result<LoginService.EditProfilePictureResponse>>()



    fun updateProfilePicture(uploadedPhoto: File) {
        val requestBody: RequestBody = uploadedPhoto.asRequestBody("image/*".toMediaType())
        val multipart = MultipartBody.Part.createFormData(
                "profile_picture",
                uploadedPhoto.name,
                requestBody
        )

        viewModelScope.launch(IO){
            try{
                _updateProfilePictureResult.postValue(Result.Loading)
                val response = loginRepository.updateProfilePicture(
                        appSharedPref.getUserId(),
                        multipart
                )
                _updateProfilePictureResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _updateProfilePictureResult.postValue(Result.Error(exception))
            }
        }
    }



    fun resetResult(){
        _updateProfilePictureResult.value = null
    }
}