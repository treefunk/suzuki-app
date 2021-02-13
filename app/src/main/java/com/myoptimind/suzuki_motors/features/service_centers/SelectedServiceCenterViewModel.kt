package com.myoptimind.suzuki_motors.features.service_centers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_motors.features.service_centers.api.ServiceCentersService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import com.myoptimind.suzuki_motors.features.shared.api.Result


class SelectedServiceCenterViewModel @ViewModelInject constructor(
        private val serviceCentersRepository: ServiceCentersRepository
): ViewModel() {

    val singleServiceCenterResult: LiveData<Result<ServiceCentersService.SingleServiceCenterResponse>> get() = _singleServiceCenterResult
    private val _singleServiceCenterResult = MutableLiveData<Result<ServiceCentersService.SingleServiceCenterResponse>>()

    fun initSingleServiceCenter(id: String){
        viewModelScope.launch(IO){
            try{
                _singleServiceCenterResult.postValue(Result.Loading)
                val response = serviceCentersRepository.getSingleServiceCenter(id)
                _singleServiceCenterResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _singleServiceCenterResult.postValue(Result.Error(exception))
            }
        }
    }

}
