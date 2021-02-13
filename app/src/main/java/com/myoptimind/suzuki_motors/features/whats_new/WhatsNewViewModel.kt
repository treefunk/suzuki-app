package com.myoptimind.suzuki_motors.features.whats_new

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_motors.features.whats_new.api.WhatsNewService
import com.myoptimind.suzuki_motors.features.whats_new.data.Article
import com.myoptimind.suzuki_motors.features.shared.api.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class WhatsNewViewModel @ViewModelInject constructor(
        private val whatsNewRepository: WhatsNewRepository
): ViewModel(){

    val whatsNewResult: LiveData<Result<WhatsNewService.WhatsNewResponse>> get() = _whatsNewResult
    private val _whatsNewResult = MutableLiveData<Result<WhatsNewService.WhatsNewResponse>>()

    val filterList: LiveData<List<WhatsNewService.WhatsNewResponse.Filter>> get() = _filterList
    private val _filterList             = MutableLiveData<List<WhatsNewService.WhatsNewResponse.Filter>>()

    private var whatsNewList = MutableLiveData<List<Article>>()
    private val _selectedCategory       = MutableLiveData<String>()

    // for pagination
    var rowCount = 10
    var offset = 0

    init {
        _selectedCategory.value = ""
//        getWhatsNew(_selectedCategory.value,offset,rowCount)
    }

    fun getInitialWhatsNew(){
        getWhatsNew(_selectedCategory.value,offset,rowCount)
    }

    fun increaseRowCount(){
//        offset += rowCount
        getWhatsNew(_selectedCategory.value,offset,rowCount)
    }

    fun updateCategory(categoryId: String){
        offset = 0
        if(categoryId.compareTo("All",true) == 0){
            _selectedCategory.value = null
        }else{
            _selectedCategory.value = categoryId
        }

        getWhatsNew(_selectedCategory.value, offset,rowCount)
    }

    fun getSingleArticle(index: Int): Article? {
        return when(val result = whatsNewResult.value){
            is Result.Success -> result.data.data.result[index]
            else -> null
        }
    }


    fun getWhatsNew(
            categoryId: String?,
            offset: Int?,
            limit: Int?
    ){
        viewModelScope.launch(IO){
            try{
                _whatsNewResult.postValue(Result.Loading)
                val response = whatsNewRepository.getWhatsNew(
                        categoryId,
                        offset,
                        limit
                )

                if(_filterList.value.isNullOrEmpty()){
                    _filterList.postValue(response.data.filters)
                }

                _whatsNewResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _whatsNewResult.postValue(Result.Error(exception))
            }
        }
    }

    fun resetResult() {
        _whatsNewResult.value = null
    }

}