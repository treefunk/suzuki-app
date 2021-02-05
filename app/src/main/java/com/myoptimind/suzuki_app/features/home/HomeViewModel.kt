package com.myoptimind.suzuki_app.features.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.home.api.HomeService
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.shared.getMessage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException


class HomeViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val homeFeaturedResult = MutableLiveData<Result<HomeService.HomeFeaturedResponse>>()


    init {
        getHomeFeatured()
    }

    fun getHomeFeatured() {
        viewModelScope.launch(IO){
            homeFeaturedResult.postValue(Result.Loading)
            try{
                val response = homeRepository.getHomeFeatured()
                homeFeaturedResult.postValue(Result.Success(response))
            }catch (exception : Exception){
                if(exception is HttpException){
                    val message = exception.getMessage()
                    homeFeaturedResult.postValue(Result.Error(Exception(message)))
                }else{
                    homeFeaturedResult.postValue(Result.Error(exception))
                }
                delay(10000)
                getHomeFeatured()
            }
        }
    }

}