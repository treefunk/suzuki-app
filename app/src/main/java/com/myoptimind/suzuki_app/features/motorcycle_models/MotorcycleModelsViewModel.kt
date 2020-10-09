package com.myoptimind.suzuki_app.features.motorcycle_models

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.motorcycle_models.api.MotorcycleModelsService
import com.myoptimind.suzuki_app.features.motorcycle_models.data.FilterName
import com.myoptimind.suzuki_app.shared.api.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MotorcycleModelsViewModel @ViewModelInject constructor(
        private val motorcycleModelsRepository: MotorcycleModelsRepository
): ViewModel() {

    val motorcycleModelsResult: LiveData<Result<MotorcycleModelsService.MotorcycleModelsResponse>> get() = _motorcycleModelsResult
    private val _motorcycleModelsResult = MutableLiveData<Result<MotorcycleModelsService.MotorcycleModelsResponse>>()

    val filterList: LiveData<List<FilterName>> get() = _filterList
    private val _filterList             = MutableLiveData<List<FilterName>>()

    private val _searchKeyword          = MutableLiveData<String>()
    private val _selectedCategory       = MutableLiveData<String>()

    init {
        getMotorcycleModels(null,null,null,null)
        _selectedCategory.value = ""
        _searchKeyword.value = ""
    }

    fun search(keyword: String){
        _searchKeyword.value = if(keyword.isEmpty()) null else keyword
        getMotorcycleModels(_searchKeyword.value,_selectedCategory.value,null,null)
    }

    fun updateCategory(category: String){

        if(category.compareTo("None",true) == 0){
            _selectedCategory.value = null
        }else{
            _selectedCategory.value = category
        }

        getMotorcycleModels(_searchKeyword.value,_selectedCategory.value, null, null)
    }

    fun getMotorcycleModels(
            name: String?,
            category: String?,
            offset: Int?,
            limit: Int?
    ){
        viewModelScope.launch(IO){
            try{
                _motorcycleModelsResult.postValue(Result.Loading)
                val response = motorcycleModelsRepository.getMotorcycleModelList(
                        name,
                        category,
                        offset,
                        limit
                )
                if(_filterList.value.isNullOrEmpty()){
                    _filterList.postValue(response.data.filters)
                }
                _motorcycleModelsResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _motorcycleModelsResult.postValue(Result.Error(exception))
                delay(5000)
                getMotorcycleModels(name,category,offset,limit)
            }
        }
    }
}