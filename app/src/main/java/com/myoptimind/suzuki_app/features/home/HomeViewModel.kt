package com.myoptimind.suzuki_app.features.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.home.api.HomeService
import com.myoptimind.suzuki_app.features.shared.api.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class HomeViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val homeFeaturedResult = MutableLiveData<Result<HomeService.HomeFeaturedResponse>>()


    init {
        getHomeFeatured()
    }

    private fun getHomeFeatured() {
        viewModelScope.launch(IO){
            homeFeaturedResult.postValue(Result.Loading)
            try{
                val response = homeRepository.getHomeFeatured()
                homeFeaturedResult.postValue(Result.Success(response))
            }catch (exception : Exception){
                homeFeaturedResult.postValue(Result.Error(exception))
            }
        }
    }

}