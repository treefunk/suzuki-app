package com.myoptimind.suzuki_app.features.whats_new

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.whats_new.api.WhatsNewService
import com.myoptimind.suzuki_app.shared.api.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class WhatsNewViewModel @ViewModelInject constructor(
        private val whatsNewRepository: WhatsNewRepository
): ViewModel(){

    val whatsNewResult: LiveData<Result<WhatsNewService.WhatsNewResponse>> get() = _whatsNewResult
    private val _whatsNewResult = MutableLiveData<Result<WhatsNewService.WhatsNewResponse>>()

    val filterList: LiveData<List<WhatsNewService.WhatsNewResponse.Filter>> get() = _filterList
    private val _filterList             = MutableLiveData<List<WhatsNewService.WhatsNewResponse.Filter>>()

    private val _selectedCategory       = MutableLiveData<String>()


    init {
        getWhatsNew(null,null,null)
        _selectedCategory.value = ""
    }

    fun updateCategory(categoryId: String){

        if(categoryId.compareTo("None",true) == 0){
            _selectedCategory.value = null
        }else{
            _selectedCategory.value = categoryId
        }

        getWhatsNew(_selectedCategory.value, null, null)
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

}