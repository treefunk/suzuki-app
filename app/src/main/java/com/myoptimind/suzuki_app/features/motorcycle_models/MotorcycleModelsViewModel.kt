package com.myoptimind.suzuki_app.features.motorcycle_models

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.motorcycle_models.api.MotorcycleModelsService
import com.myoptimind.suzuki_app.features.shared.data.FilterName
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.data.MotorcycleModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class MotorcycleModelsViewModel @ViewModelInject constructor(
        private val motorcycleModelsRepository: MotorcycleModelsRepository
) : ViewModel() {

    val motorcycleModelsResult: LiveData<Result<MotorcycleModelsService.MotorcycleModelsResponse>> get() = _motorcycleModelsResult
    private val _motorcycleModelsResult = MutableLiveData<Result<MotorcycleModelsService.MotorcycleModelsResponse>>()

    val filterList: LiveData<List<FilterName>> get() = _filterList
    private val _filterList = MutableLiveData<List<FilterName>>()

    private val _searchKeyword = MutableLiveData<String>()
    private val _selectedCategory = MutableLiveData<String>()

    // for pagination
    private var rowCount = 10
    var offset = 0


    init {
        getMotorcycleModels(null, null, null, rowCount)
        _selectedCategory.value = ""
        _searchKeyword.value = ""
    }

    fun increaseRowCount() {
        Timber.v("increase row count")
        offset += rowCount
        getMotorcycleModels(_searchKeyword.value, _selectedCategory.value, offset, rowCount)
    }

    fun search(keyword: String) {
        _searchKeyword.value = if (keyword.isEmpty()) null else keyword
        offset = 0
        getMotorcycleModels(_searchKeyword.value, _selectedCategory.value, null, rowCount)
    }

    fun updateCategory(category: String) {
        offset = 0
        if (category.compareTo("All", true) == 0) {
            _selectedCategory.value = null
        } else {
            _selectedCategory.value = category
        }
        getMotorcycleModels(_searchKeyword.value, _selectedCategory.value, null, rowCount)
    }

    fun getMotorcycleModels(
            name: String?,
            category: String?,
            offset: Int?,
            limit: Int?
    ) {
        viewModelScope.launch(IO) {
            try {
                _motorcycleModelsResult.postValue(Result.Loading)
                val response = motorcycleModelsRepository.getMotorcycleModelList(
                        name,
                        category,
                        offset,
                        limit
                )
                if (_filterList.value.isNullOrEmpty()) {
                    _filterList.postValue(response.data.filters)
                }
                _motorcycleModelsResult.postValue(Result.Success(response))

            } catch (exception: Exception) {
                _motorcycleModelsResult.postValue(Result.Error(exception))
                delay(5000)
                getMotorcycleModels(name, category, offset, limit)
            }
        }
    }

    fun resetResult() {
        _motorcycleModelsResult.value = null
    }
}