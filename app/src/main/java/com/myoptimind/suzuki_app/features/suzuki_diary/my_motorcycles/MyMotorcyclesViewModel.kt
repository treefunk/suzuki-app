package com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.login.LoginRepository
import com.myoptimind.suzuki_app.features.login.api.LoginService
import com.myoptimind.suzuki_app.features.shared.AppSharedPref
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.api.MyMotorcyclesService
import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.data.RegisteredMotorcycle
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyMotorcyclesViewModel @ViewModelInject constructor(
        private val loginRepository: LoginRepository,
        private val myMotorcyclesRepository: MyMotorcyclesRepository,
        private val appSharedPref: AppSharedPref
): ViewModel() {

    /**
     * Register Motorcycle
     */
    val featuredMotorcyclesResult: LiveData<Result<LoginService.FeaturedMotorcycleResponse>> get() = _featuredMotorcyclesResult
    private val _featuredMotorcyclesResult = MutableLiveData<Result<LoginService.FeaturedMotorcycleResponse>>()

    val motorcycleModelsResult: LiveData<Result<MyMotorcyclesService.GetMotorcycleModelsListResponse>> get() = _motorcycleModelsResult
    private val _motorcycleModelsResult = MutableLiveData<Result<MyMotorcyclesService.GetMotorcycleModelsListResponse>>()

    val registerMotorcycleResult: LiveData<Result<MyMotorcyclesService.RegisterMotorcycleResponse>> get() = _registerMyMotorcycleResult
    private val _registerMyMotorcycleResult = MutableLiveData<Result<MyMotorcyclesService.RegisterMotorcycleResponse>>()

    val existingRegisteredMotorcycle: LiveData<RegisteredMotorcycle?> get() = _existingRegisteredMotorcycle
    private val _existingRegisteredMotorcycle = MutableLiveData<RegisteredMotorcycle?>()

    val redirectToList: LiveData<Boolean> get() = _redirectToList
    private val _redirectToList = MutableLiveData<Boolean>()

    /**
     * Registered Motorcycle List
     */

    val registeredMotorcyclesResult: LiveData<Result<MyMotorcyclesService.RegisteredMotorcyclesResponse>> get() = _registeredMotorcyclesResult
    private val _registeredMotorcyclesResult = MutableLiveData<Result<MyMotorcyclesService.RegisteredMotorcyclesResponse>>()

    init {

    }

    fun initMyRegisteredMotorcycles(){
        getRegisteredMotorcycles()
    }

    private fun getRegisteredMotorcycles() {
        viewModelScope.launch(IO){
            try{
                _registeredMotorcyclesResult.postValue(Result.Loading)
                val response = myMotorcyclesRepository.getRegisteredMotorcycles(appSharedPref.getUserId())
                _registeredMotorcyclesResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _registeredMotorcyclesResult.postValue(Result.Error(exception))
            }
        }
    }

    fun  initRegisteredMotorcycleId(index: Int){
        when(val data = _registeredMotorcyclesResult.value){
            is Result.Success -> {
                _existingRegisteredMotorcycle.value = data.data.data[index]
            }
            null -> {
                _existingRegisteredMotorcycle.value = null
            }
        }
    }

    fun clearExistingMotorcycle(){
        _existingRegisteredMotorcycle.value = null
    }

    fun initRegisterMotorcycleModels(){
        getFeaturedMotorcycles()
        getModelList()
    }

    fun registerMyMotorcycle(
            beastNickname: String,
            customerId: String,
            motorcycleModelId: String,
            engineNumber: String,
            frameNumber: String,
            datePurchased: String,
            purchasedIn: String,
            redirectAfterRegister: Boolean = false
    ){
        viewModelScope.launch(IO){
            try{
                _registerMyMotorcycleResult.postValue(Result.Loading)
                var response: MyMotorcyclesService.RegisterMotorcycleResponse?
                response = if(_existingRegisteredMotorcycle.value == null){
                    // insert motorcycle
                    myMotorcyclesRepository.registerMyMotorcycle(
                            beastNickname,
                            customerId,
                            motorcycleModelId,
                            engineNumber,
                            frameNumber,
                            datePurchased,
                            purchasedIn
                    )
                }else{
                    // update motorcycle
                    myMotorcyclesRepository.updateRegisteredMyMotorcycle(
                            _existingRegisteredMotorcycle.value!!.id,
                            beastNickname,
                            customerId,
                            motorcycleModelId,
                            engineNumber,
                            frameNumber,
                            datePurchased,
                            purchasedIn
                    )
                }
                if(response != null){
                    _registerMyMotorcycleResult.postValue(Result.Success(response))
                }
                _redirectToList.postValue(redirectAfterRegister)
            }catch (exception: Exception){
                _registerMyMotorcycleResult.postValue(Result.Error(exception))
            }
        }
    }

    private fun getModelList() {
        viewModelScope.launch(IO){
            try{
                _motorcycleModelsResult.postValue(Result.Loading)
                val response = myMotorcyclesRepository.getMotorcycleModels(limit = 999999)
                _motorcycleModelsResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _motorcycleModelsResult.postValue(Result.Error(exception))
                delay(5000)
                getModelList()
            }
        }
    }

    private fun getFeaturedMotorcycles() {
        viewModelScope.launch(IO){
            try{
                _featuredMotorcyclesResult.postValue(Result.Loading)
                val response = loginRepository.getFeaturedMotorcycles()
                _featuredMotorcyclesResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                _featuredMotorcyclesResult.postValue(Result.Error(exception))
            }
        }
    }

    fun clearResults() {
        _registerMyMotorcycleResult.value = null
    }

    fun clearRedirect() {
        _redirectToList.value = false
    }

}