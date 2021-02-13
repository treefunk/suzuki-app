package com.myoptimind.suzuki_motors.features.spare_parts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.shared.data.FilterName
import com.myoptimind.suzuki_motors.features.spare_parts.api.SparePartsService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber


class SparePartsViewModel @ViewModelInject constructor(
        private val sparePartsRepository: SparePartsRepository
): ViewModel() {

    val sparePartResult: LiveData<Result<SparePartsService.SparePartResponse>> get() = _sparePartResult
    private val _sparePartResult = MutableLiveData<Result<SparePartsService.SparePartResponse>>()

    val filterList: LiveData<List<FilterName>> get() = _filterList
    private val _filterList             = MutableLiveData<List<FilterName>>()

    private val _searchKeyword          = MutableLiveData<String>()
    private val _selectedMotorcycle     = MutableLiveData<String>()

    // for pagination
    var rowCount = 10
    var offset = 0

    var sparePartId: String = ""


    init {
        _searchKeyword.value = ""
        _selectedMotorcycle.value = ""
    }

    fun increaseRowCount() {
        Timber.v("increase row count")
        offset += rowCount
        getSparePart(sparePartId,_selectedMotorcycle.value,_searchKeyword.value,offset,rowCount)
    }


    fun search(keyword: String){
        offset = 0
        _searchKeyword.value = if(keyword.isEmpty()) null else keyword
        getSparePart(sparePartId,_selectedMotorcycle.value,_searchKeyword.value,offset,rowCount)
    }

    fun updateMotorcycle(motorcycleName: String){
        offset = 0
        if(motorcycleName.compareTo("All",true) == 0){
            _selectedMotorcycle.value = null
        }else{
            _selectedMotorcycle.value = motorcycleName
        }
        getSparePart(sparePartId,_selectedMotorcycle.value,_searchKeyword.value,offset,rowCount)

    }

    fun getSparePart(
            sparePartId: String,
            motorcycleName: String?,
            name: String?,
            offset: Int?,
            limit: Int?
    ){
        viewModelScope.launch(IO){
            try{
                _sparePartResult.postValue(Result.Loading)
                val response = sparePartsRepository.getSpareParts(
                        sparePartId,motorcycleName,name,offset,limit
                )
                if(_filterList.value.isNullOrEmpty()){
                    _filterList.postValue(response.data.filters)
                }
                _sparePartResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _sparePartResult.postValue(Result.Error(exception))
            }
        }
    }

    fun resetSpartPartsResult() {
        _sparePartResult.value = null
    }
}