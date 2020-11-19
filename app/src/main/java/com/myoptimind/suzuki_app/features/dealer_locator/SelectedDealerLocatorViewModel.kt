package com.myoptimind.suzuki_app.features.dealer_locator

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.dealer_locator.api.DealerLocatorService
import com.myoptimind.suzuki_app.features.shared.api.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SelectedDealerLocatorViewModel @ViewModelInject constructor(
        private val dealerLocatorsRepository: DealerLocatorsRepository
): ViewModel() {

    val singleDealerLocatorResult: LiveData<Result<DealerLocatorService.SingleDealerLocatorResponse>> get() = _singleDealerLocatorResult
    private val _singleDealerLocatorResult = MutableLiveData<Result<DealerLocatorService.SingleDealerLocatorResponse>>()

    fun initSingleDealerLocator(id: String){
        viewModelScope.launch(IO){
            try{
                _singleDealerLocatorResult.postValue(Result.Loading)
                val response = dealerLocatorsRepository.getSingleDealerLocator(id)
                _singleDealerLocatorResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _singleDealerLocatorResult.postValue(Result.Error(exception))
            }

        }
    }
}