package com.myoptimind.suzuki_app.features.service_centers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.motorcycle_models.data.FilterName
import com.myoptimind.suzuki_app.features.service_centers.api.ServiceCentersService
import com.myoptimind.suzuki_app.shared.api.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ServiceCentersViewModel @ViewModelInject constructor(
        private val serviceCentersRepository: ServiceCentersRepository
): ViewModel() {

    val serviceCentersResult: LiveData<Result<ServiceCentersService.ServiceCentersResponse>> get() = _serviceCentersResult
    private val _serviceCentersResult = MutableLiveData<Result<ServiceCentersService.ServiceCentersResponse>>()

    val filterList: LiveData<List<ServiceCentersService.ServiceCentersResponse.City>> get() = _filterList
    private val _filterList = MutableLiveData<List<ServiceCentersService.ServiceCentersResponse.City>>()

    val services: LiveData<List<ServiceCentersService.ServiceCentersResponse.Service>> get() = _services
    private val _services = MutableLiveData<List<ServiceCentersService.ServiceCentersResponse.Service>>()

    private val _searchKeyword          = MutableLiveData<String>()
    private val _selectedLocation       = MutableLiveData<String>()

    init {
        getServiceCenters(null,null,null, null)
        _selectedLocation.value = ""
        _searchKeyword.value = ""
    }

    fun search(keyword: String){
        _searchKeyword.value = if(keyword.isEmpty()) null else keyword
        getServiceCenters(_searchKeyword.value,_selectedLocation.value, null,null)
    }

    fun updateLocation(location: String){
        if(location.compareTo("None",true) == 0){
            _selectedLocation.value = null
        }else{
            _selectedLocation.value = location
        }

        getServiceCenters(_searchKeyword.value,_selectedLocation.value,null,null)

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

}