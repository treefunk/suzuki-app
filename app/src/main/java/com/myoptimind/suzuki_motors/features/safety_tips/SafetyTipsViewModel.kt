package com.myoptimind.suzuki_motors.features.safety_tips

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_motors.features.safety_tips.api.SafetyTipsService
import com.myoptimind.suzuki_motors.features.safety_tips.data.SafetyTip
import com.myoptimind.suzuki_motors.features.shared.api.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SafetyTipsViewModel @ViewModelInject constructor(
        private val safetyTipsRepository: SafetyTipsRepository
): ViewModel() {

    val safetyTipsResult: LiveData<Result<SafetyTipsService.SafetyTipsResponse>> get() = _safetyTipsResult
    private val _safetyTipsResult = MutableLiveData<Result<SafetyTipsService.SafetyTipsResponse>>()
    // for pagination
    private var rowCount = 10
    var offset = 0


    init {
        getSafetyTips()
    }

    fun getSingleSafetyTip(index : Int): SafetyTip? {
        return when(val result = _safetyTipsResult.value){
            is Result.Success -> result.data.data[index]
            else -> null
        }
    }


    private fun getSafetyTips(){
        viewModelScope.launch(Dispatchers.IO){
            try{
                _safetyTipsResult.postValue(Result.Loading)
                val response = safetyTipsRepository.getSafetyTips()
                _safetyTipsResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _safetyTipsResult.postValue(Result.Error(exception))
            }
        }
    }
}