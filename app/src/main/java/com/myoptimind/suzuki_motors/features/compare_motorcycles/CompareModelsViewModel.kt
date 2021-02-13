package com.myoptimind.suzuki_motors.features.compare_motorcycles

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.myoptimind.suzuki_motors.features.compare_motorcycles.data.ImageAndLogo
import com.myoptimind.suzuki_motors.features.compare_motorcycles.data.MotorPosition
import com.myoptimind.suzuki_motors.features.compare_motorcycles.data.MotorSingleDetail
import com.myoptimind.suzuki_motors.features.suzuki_diary.my_motorcycles.MyMotorcyclesRepository
import com.myoptimind.suzuki_motors.features.suzuki_diary.my_motorcycles.api.MyMotorcyclesService
import com.myoptimind.suzuki_motors.features.suzuki_diary.my_motorcycles.data.MotorcycleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import timber.log.Timber
import retrofit2.Callback
import retrofit2.Response

class CompareModelsViewModel @ViewModelInject constructor(
        private val compareModelsRepository:  CompareModelsRepository,
        private val myMotorcyclesRepository: MyMotorcyclesRepository
): ViewModel() {

    private val _motorcycleModelsResult = MutableLiveData<MyMotorcyclesService.GetMotorcycleModelsListResponse>()


    // Drop Down Data
    val firstMotorcycleList: LiveData<List<MotorcycleModel>> get() = _firstMotorcycleList
    private val _firstMotorcycleList = MediatorLiveData<List<MotorcycleModel>>()
    val secondMotorcycleList: LiveData<List<MotorcycleModel>> get() = _secondMotorcycleList
    private val _secondMotorcycleList = MediatorLiveData<List<MotorcycleModel>>()


    // FIRST MOTORCYCLE
        // IMAGE AND LOGO
    val firstMotorcycleImageAndLogo: LiveData<ImageAndLogo?> get() =  _firstMotorcycleImageAndLogo
    private val _firstMotorcycleImageAndLogo = MutableLiveData<ImageAndLogo?>()

        // SPECS
    val firstList: LiveData<List<MotorSingleDetail>?> get() = _firstList
    private val _firstList = MutableLiveData<List<MotorSingleDetail>?>()


    // SECOND MOTORCYCLE
        // IMAGE AND LOGO
    val secondMotorcycleImageAndLogo: LiveData<ImageAndLogo?> get() =  _secondMotorcycleImageAndLogo
    private val _secondMotorcycleImageAndLogo = MutableLiveData<ImageAndLogo?>()

        // SPECS
    val secondList: LiveData<List<MotorSingleDetail>?> get() = _secondList
    private val _secondList = MutableLiveData<List<MotorSingleDetail>?>()



//    private val _secondMotorcycleModelResult = MutableLiveData<>

    init {
        getModelList()
    }

    private fun getModelList() {

        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = myMotorcyclesRepository.getMotorcycleModels(limit = 999999)
                _motorcycleModelsResult.postValue(response)
                _firstMotorcycleList.postValue(response.data.result)
                _secondMotorcycleList.postValue(response.data.result)
            }catch (exception: Exception){
                delay(5000)
                getModelList()
            }
        }
    }

    fun initMotorcycle(
            motorcycleId: String,
            motorPos: MotorPosition
    ){
        viewModelScope.launch(IO){
            try{
                compareModelsRepository.getMotorcycleCompareDetails(motorcycleId).enqueue(object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        val r = response.body()
                        val motorcycle = r?.getAsJsonObject("data")?.getAsJsonObject("motorcycle")

                        if (motorcycle != null) {

                            if (motorPos == MotorPosition.FIRST) {

                                ImageAndLogo(
                                        motorcycle.get("thumbnail").asString,
                                        motorcycle.get("logo").asString
                                ).also {
                                    _firstMotorcycleImageAndLogo.postValue(it)
                                }

                                val list = motorcycle.getAsMotorcycleList()
                                _firstList.postValue(list)

                            } else if (motorPos == MotorPosition.SECOND) {

                                ImageAndLogo(
                                        motorcycle.get("thumbnail").asString,
                                        motorcycle.get("logo").asString
                                ).also {
                                    _secondMotorcycleImageAndLogo.postValue(it)
                                }

                                val list = motorcycle.getAsMotorcycleList()
                                _secondList.postValue(list)
                            }
                        }

                        Timber.v("value of r = ${motorcycle.toString()}")
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Timber.v("Something went wrong")
                    }
                })

            }catch (exception: Exception){
                Timber.v(exception)
                exception.printStackTrace()
            }
        }
    }



    fun JsonObject.getAsMotorcycleList(): List<MotorSingleDetail>? {
        this.remove("id")
        this.remove("thumbnail")
        this.remove("logo")
        this.remove("category")
        val keyset = this.keySet()
        return keyset?.map { key ->
            MotorSingleDetail(key.replace("_", " ").capitalizeWords(), this.get(key).asString)
        }
    }

    fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }


    fun initSecondMotorcycle(
            motorcycleId: String
    ){
/*        viewModelScope.launch(IO){
            try{
                val response = compareModelsRepository.getMotorcycleCompareDetails(motorcycleId)
                Timber.v("response: " + response)
            }catch (exception: Exception){
                Timber.v(exception)
                exception.printStackTrace()
            }
        }*/
    }


}