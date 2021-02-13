package com.myoptimind.suzuki_motors.features.motorcycle_models

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.myoptimind.suzuki_motors.features.motorcycle_models.api.MotorcycleModelsService
import com.myoptimind.suzuki_motors.features.motorcycle_models.data.ColorVariant
import com.myoptimind.suzuki_motors.features.shared.api.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SelectedMotorcycleViewModel @ViewModelInject constructor(
        private val motorcycleModelsRepository: MotorcycleModelsRepository
): ViewModel(){

    val motorcycleModelResult: LiveData<Result<MotorcycleModelsService.GetSingleMotorcycleModelResponse>> get() = _motorcycleModelResult
    private val _motorcycleModelResult = MutableLiveData<Result<MotorcycleModelsService.GetSingleMotorcycleModelResponse>>()

    val activeColorVariantIndex: LiveData<Int> get() = _activeColorVariantIndex
    private val _activeColorVariantIndex = MutableLiveData<Int>()

    val activeColorVariant: LiveData<ColorVariant> get() = _activeColorVariant
    private val _activeColorVariant = MediatorLiveData<ColorVariant>()

    val activeSpecTab: LiveData<SpecTab> get() = _activeSpecTab
    private val _activeSpecTab = MutableLiveData<SpecTab>()

    private var colorVariants = ArrayList<ColorVariant>()

    init {
        _activeSpecTab.value = SpecTab.ENGINE_SPEC
        _activeColorVariantIndex.value = 0

        _activeColorVariant.addSource(_activeColorVariantIndex){ index ->
            if(colorVariants.isNotEmpty()){
                _activeColorVariant.value = colorVariants[index]
            }
        }

    }


    fun initMotorcycleModel(
            id: String
    ){
        viewModelScope.launch(IO){
            try{
                _motorcycleModelResult.postValue(Result.Loading)
                val response = motorcycleModelsRepository.getSingleMotorcycleModel(id)
                _motorcycleModelResult.postValue(Result.Success(response))
                colorVariants = ArrayList(response.data.colorVariants)
            }catch (exception: Exception){
                _motorcycleModelResult.postValue(Result.Error(exception))
            }

        }
    }

    fun setActiveSpecTab(specTab: SpecTab){
        _activeSpecTab.value = specTab
    }

    fun setActiveColorVariantIndex(index: Int){
        _activeColorVariantIndex.value = index
    }



}