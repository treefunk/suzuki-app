package com.myoptimind.suzuki_motors.features.extract_data

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_motors.features.shared.AppSharedPref
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.ServiceHistoryRepository
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.api.ServiceHistoryService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExtractDataViewModel @ViewModelInject constructor(
        private val serviceHistoryRepository: ServiceHistoryRepository,
        private val appSharedPref: AppSharedPref
): ViewModel() {

    val serviceHistoryList: LiveData<Result<ServiceHistoryService.ServiceHistoryListResponse>> get() = _serviceHistoryList
    private val _serviceHistoryList = MutableLiveData<Result<ServiceHistoryService.ServiceHistoryListResponse>>()

    fun getServiceHistoryList(){
        viewModelScope.launch(Dispatchers.IO){
            try{
                _serviceHistoryList.postValue(Result.Loading)
                val response = serviceHistoryRepository.getServiceHistoryList(appSharedPref.getUserId())
                _serviceHistoryList.postValue(Result.Success(response))
            }catch (exception: Exception){
                _serviceHistoryList.postValue(Result.Error(exception))
            }
        }
    }


}