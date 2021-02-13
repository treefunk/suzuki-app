package com.myoptimind.suzuki_motors.features.service_centers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_motors.features.service_centers.api.ServiceCentersService
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.shared.data.City
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber

class ServiceCentersViewModel @ViewModelInject constructor(
        private val serviceCentersRepository: ServiceCentersRepository
): ViewModel() {

    val serviceCentersResult: LiveData<Result<ServiceCentersService.ServiceCentersResponse>> get() = _serviceCentersResult
    private val _serviceCentersResult = MutableLiveData<Result<ServiceCentersService.ServiceCentersResponse>>()

    val filterList: LiveData<List<City>> get() = _filterList
    private val _filterList = MutableLiveData<List<City>>()

    val services: LiveData<List<ServiceCentersService.ServiceCentersResponse.Service>> get() = _services
    private val _services = MutableLiveData<List<ServiceCentersService.ServiceCentersResponse.Service>>()

    private val _searchKeyword          = MutableLiveData<String>()
    private val _selectedLocation       = MutableLiveData<String>()

    // for pagination
    private var rowCount = 10
    var offset = 0

    init {
        _selectedLocation.value = ""
        _searchKeyword.value = ""
        getServiceCenters(_searchKeyword.value,_selectedLocation.value,null,rowCount)
    }

    fun increaseRowCount() {
        Timber.v("increase row count")
        offset += rowCount
       getServiceCenters(_searchKeyword.value,_selectedLocation.value,offset,rowCount)
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
                _serviceCentersResult.postValue(Result.Loading)
                val response = serviceCentersRepository.getServiceCenters(name,location,offset,limit)

                if(_filterList.value.isNullOrEmpty()){
                    _filterList.postValue(response.data.cities)
                }

                if(_services.value.isNullOrEmpty()){
                    _services.postValue(response.data.services)
                }

                _serviceCentersResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _serviceCentersResult.postValue(Result.Error(exception))
            }
        }


    }

    fun resetResults(){
        _serviceCentersResult.value = null
    }

}