package com.myoptimind.suzuki_motors.features.dealer_locator

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_motors.features.dealer_locator.api.DealerLocatorService
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.shared.data.City
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber

class DealerLocatorsViewModel @ViewModelInject constructor(
        private val dealerLocatorsRepository: DealerLocatorsRepository
): ViewModel() {

    val dealerLocatorsResult: LiveData<Result<DealerLocatorService.DealerLocatorsResponse>> get() = _dealerLocatorsResult
    private val _dealerLocatorsResult = MutableLiveData<Result<DealerLocatorService.DealerLocatorsResponse>>()

    val filterList: LiveData<List<City>> get() = _filterList
    private val _filterList = MutableLiveData<List<City>>()

    // for pagination
    var rowCount = 10
    var offset = 0



    private val _searchKeyword          = MutableLiveData<String>()
    private val _selectedLocation       = MutableLiveData<String>()

    init {
        getServiceCenters(null,null,null, rowCount)
        _selectedLocation.value = ""
        _searchKeyword.value = ""
    }

    fun increaseRowCount() {
        Timber.v("increase row count")
        offset += rowCount
        getServiceCenters(_searchKeyword.value,_selectedLocation.value, offset,rowCount)
    }


    fun search(keyword: String){
        offset = 0
        _searchKeyword.value = if(keyword.isEmpty()) null else keyword
        getServiceCenters(_searchKeyword.value,_selectedLocation.value, offset,rowCount)
    }

    fun updateLocation(location: String){
        offset = 0
        if(location.compareTo("All",true) == 0){
            _selectedLocation.value = null
        }else{
            _selectedLocation.value = location
        }

        getServiceCenters(_searchKeyword.value,_selectedLocation.value,offset,rowCount)

    }

    fun getServiceCenters(
            name: String?,
            location: String?,
            offset: Int?,
            limit: Int?
    ){
        viewModelScope.launch(IO){
            try{
                _dealerLocatorsResult.postValue(Result.Loading)
                val response = dealerLocatorsRepository.getDealerLocators(name,location,offset,limit)

                if(_filterList.value.isNullOrEmpty()){
                    _filterList.postValue(response.data.cities)
                }

                _dealerLocatorsResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _dealerLocatorsResult.postValue(Result.Error(exception))
            }
        }
    }

    fun resetResult(){
        _dealerLocatorsResult.value = null
    }

}