package com.myoptimind.suzuki_motors.features.suzuki_diary.service_history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_motors.features.shared.AppSharedPref
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.AddServiceHistoryMaintenanceFragment.MaintenanceTab
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.api.ServiceHistoryService
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.data.ServiceHistory
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.data.ServiceHistoryDetails
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.data.ServiceHistoryMaintenance
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.shared.getMessage
import com.myoptimind.suzuki_motors.features.suzuki_diary.my_motorcycles.MyMotorcyclesRepository
import com.myoptimind.suzuki_motors.features.suzuki_diary.my_motorcycles.api.MyMotorcyclesService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class ServiceHistoryViewModel @ViewModelInject constructor(
        private val serviceHistoryRepository: ServiceHistoryRepository,
        private val myMotorcyclesRepository: MyMotorcyclesRepository,
        private val appSharedPref: AppSharedPref
) : ViewModel() {

    val activeServiceHistoryDetails: LiveData<ServiceHistoryDetails?> get() = _activeServiceHistoryDetails
    private val _activeServiceHistoryDetails = MutableLiveData<ServiceHistoryDetails?>()

    var serviceHistoryMaintenanceHash = HashMap<MaintenanceTab, ServiceHistoryMaintenance>()

    val registeredMotorcyclesResult: LiveData<Result<MyMotorcyclesService.RegisteredMotorcyclesResponse>> get() = _registeredMotorcyclesResult
    private val _registeredMotorcyclesResult = MutableLiveData<Result<MyMotorcyclesService.RegisteredMotorcyclesResponse>>()

    val postServiceHistoryResult: LiveData<Result<ServiceHistoryService.AddServiceHistoryResponse>> get() = _postServiceHistoryResult
    private val _postServiceHistoryResult = MutableLiveData<Result<ServiceHistoryService.AddServiceHistoryResponse>>()

    val updateServiceHistoryResult: LiveData<Result<ServiceHistoryService.UpdateServiceHistoryResponse>> get() = _updateServiceHistoryResult
    private val _updateServiceHistoryResult = MutableLiveData<Result<ServiceHistoryService.UpdateServiceHistoryResponse>>()

    val serviceHistoryList: LiveData<Result<ServiceHistoryService.ServiceHistoryListResponse>> get() = _serviceHistoryList
    private val _serviceHistoryList = MutableLiveData<Result<ServiceHistoryService.ServiceHistoryListResponse>>()

    private val _maintenanceTab = MutableLiveData<MaintenanceTab>()

    val upcomingEventsResult: LiveData<Result<ServiceHistoryService.ServiceHistoryListResponse>> get() = _upcomingEventsResult
    private val _upcomingEventsResult = MutableLiveData<Result<ServiceHistoryService.ServiceHistoryListResponse>>()

    private var serviceHistoryId = "0"
    var currentIndex = -1

    init {
        _maintenanceTab.value = MaintenanceTab.PREVENTIVE
    }

    fun initServiceHistory(index: Int){

        val serviceHistory = getSelectedServiceHistory(index)

        if(serviceHistory != null){

            serviceHistoryId = serviceHistory.id!!

            val serviceHistoryDetails = ServiceHistoryDetails(
                    serviceHistory.registeredMotorcycleId,
                    serviceHistory.beastNickname,
                    serviceHistory.currentOdometerReading,
                    serviceHistory.purchasedDate,
                    serviceHistory.nextPmsDate,
                    serviceHistory.mileageLeft,
                    -1
            )
            _activeServiceHistoryDetails.value = serviceHistoryDetails

            val maintenanceHash = HashMap<MaintenanceTab, ServiceHistoryMaintenance>()

            maintenanceHash[MaintenanceTab.PREVENTIVE] = ServiceHistoryMaintenance(
                    MaintenanceTab.PREVENTIVE.title,
                    serviceHistory.changeOil,
                    serviceHistory.tires,
                    serviceHistory.brakes,
                    serviceHistory.chainsAndSprockets,
                    serviceHistory.airFilter,
                    serviceHistory.sparkPlugs,
                    serviceHistory.exhaust_muffler,
                    serviceHistory.suspension,
                    serviceHistory.chassisBoltsNuts,
                    serviceHistory.notes
            )

            maintenanceHash[MaintenanceTab.EMERGENCY] = ServiceHistoryMaintenance(
                    MaintenanceTab.EMERGENCY.title,
                    serviceHistory.changeOilE,
                    serviceHistory.tiresE,
                    serviceHistory.brakesE,
                    serviceHistory.chainsAndSprocketsE,
                    serviceHistory.airFilterE,
                    serviceHistory.sparkPlugsE,
                    serviceHistory.exhaust_mufflerE,
                    serviceHistory.suspensionE,
                    serviceHistory.chassisBoltsNutsE,
                    serviceHistory.notesE
            )
            serviceHistoryMaintenanceHash = maintenanceHash
        }
    }

    private fun getSelectedServiceHistory(index: Int): ServiceHistory? {
        return when(val result = _serviceHistoryList.value){
            is Result.Success -> result.data.data[index]
            else -> null
        }
    }

    fun getServiceHistoryList(maxDay: Int? = null){
        viewModelScope.launch(IO){
            try{
                _serviceHistoryList.postValue(Result.Loading)
                val response = serviceHistoryRepository.getServiceHistoryList(appSharedPref.getUserId(),maxDay)
                _serviceHistoryList.postValue(Result.Success(response))
            }catch (exception: Exception){
                _serviceHistoryList.postValue(Result.Error(exception))
            }
        }
    }

    fun updateMaintenanceTab(maintenanceTab: MaintenanceTab) {
        _maintenanceTab.value = maintenanceTab
    }

    fun getMaintenanceDetails(): ServiceHistoryMaintenance {

        return serviceHistoryMaintenanceHash[_maintenanceTab.value!!]!!
    }


    fun checkExisting() {
        Timber.v("checking for saved details...")

        viewModelScope.launch(IO) {
            try {
                val serviceHistoryDetails = serviceHistoryRepository.getServiceHistoryDetails()
                val preventiveDetails = serviceHistoryRepository.getServiceHistoryMaintenance(MaintenanceTab.PREVENTIVE)
                val emergencyDetails = serviceHistoryRepository.getServiceHistoryMaintenance(MaintenanceTab.EMERGENCY)

                if (serviceHistoryDetails != null) {
                    _activeServiceHistoryDetails.postValue(serviceHistoryDetails)
                }else{
                    _activeServiceHistoryDetails.postValue(
                            ServiceHistoryDetails(
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                            )
                    )
                }
                if (preventiveDetails != null) {
                    serviceHistoryMaintenanceHash[MaintenanceTab.PREVENTIVE] = preventiveDetails
                } else { // if no saved data is found create a new instance of ServiceHistoryMaintenance with its type
                    serviceHistoryMaintenanceHash[MaintenanceTab.PREVENTIVE] = ServiceHistoryMaintenance(tabType = MaintenanceTab.PREVENTIVE.title)
                }

                if (emergencyDetails != null) {
                    serviceHistoryMaintenanceHash[MaintenanceTab.EMERGENCY] = emergencyDetails
                } else {
                    serviceHistoryMaintenanceHash[MaintenanceTab.EMERGENCY] = ServiceHistoryMaintenance(tabType = MaintenanceTab.EMERGENCY.title)
                }


            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }

        serviceHistoryId = "0"

    }

    fun saveHistoryDetails(serviceHistoryDetails: ServiceHistoryDetails) {
        viewModelScope.launch(IO) {
            serviceHistoryRepository.saveServiceHistoryDetails(
                    serviceHistoryDetails
            )
        }
    }

    fun saveServiceHistoryMaintenance() {
        Timber.v("Saving Service History Maintenance:")
        Timber.v(serviceHistoryMaintenanceHash[MaintenanceTab.PREVENTIVE].toString())
        Timber.v(serviceHistoryMaintenanceHash[MaintenanceTab.EMERGENCY].toString())
        viewModelScope.launch(IO) {
            serviceHistoryMaintenanceHash[MaintenanceTab.PREVENTIVE]?.let { serviceHistoryRepository.saveServiceHistoryMaintenance(it) }
            serviceHistoryMaintenanceHash[MaintenanceTab.EMERGENCY]?.let { serviceHistoryRepository.saveServiceHistoryMaintenance(it) }
        }
    }

    fun saveStepOne(
            registeredMotorcycleId: String,
            registeredMotorcycleName: String,
            currentOdometerReading: String,
            dateOfPurchase: String,
            pmsDate: String,
            mileage: String?
    ) {
        Timber.v("saving maintenance details...")
        Timber.v("motorcycleid = $registeredMotorcycleId")
        Timber.v("motorcycleid = ${_activeServiceHistoryDetails.value.toString()}")

        val serviceHistoryDetails = ServiceHistoryDetails(
                registeredMotorcycleId,
                registeredMotorcycleName,
                currentOdometerReading,
                dateOfPurchase,
                pmsDate,
                mileage
        )

        _activeServiceHistoryDetails.value = serviceHistoryDetails
    }

    fun submit() {
        val maintenanceDetails = _activeServiceHistoryDetails.value
        val preventiveMaintenance = serviceHistoryMaintenanceHash[MaintenanceTab.PREVENTIVE]
        val emergencyMaintenance = serviceHistoryMaintenanceHash[MaintenanceTab.EMERGENCY]
        Timber.v("maintenance details ${maintenanceDetails.toString()}")
        Timber.v("preventive details ${preventiveMaintenance.toString()}")
        Timber.v("emergency details ${emergencyMaintenance.toString()}")



        if (maintenanceDetails != null && preventiveMaintenance != null && emergencyMaintenance != null) {
            val serviceHistory = ServiceHistory(
                    appSharedPref.getUserId(),
                    maintenanceDetails.registeredMotorcycleId,
                    maintenanceDetails.currentOdometerReading,
                    maintenanceDetails.nextPmsDate,
                    maintenanceDetails.mileageLeft,
                    preventiveMaintenance.changeOil,
                    preventiveMaintenance.tires,
                    preventiveMaintenance.brakes,
                    preventiveMaintenance.chainsAndSprockets,
                    preventiveMaintenance.airFilter,
                    preventiveMaintenance.sparkPlugs,
                    preventiveMaintenance.exhaust_muffler,
                    preventiveMaintenance.suspension,
                    preventiveMaintenance.chassisBoltsNuts,
                    preventiveMaintenance.notes,
                    emergencyMaintenance.changeOil,
                    emergencyMaintenance.tires,
                    emergencyMaintenance.brakes,
                    emergencyMaintenance.chainsAndSprockets,
                    emergencyMaintenance.airFilter,
                    emergencyMaintenance.sparkPlugs,
                    emergencyMaintenance.exhaust_muffler,
                    emergencyMaintenance.suspension,
                    emergencyMaintenance.chassisBoltsNuts,
                    emergencyMaintenance.notes,
                    maintenanceDetails.registeredMotorcycleName,
                    maintenanceDetails.purchasedDate
            )

            Timber.v("registered motorcycle id =  ${serviceHistory.registeredMotorcycleId}")
            Timber.v("purchased date =  ${serviceHistory.purchasedDate}")

            viewModelScope.launch(IO){
                when(serviceHistoryId){
                    "0" -> { // Add Service History
                        try{
                            _postServiceHistoryResult.postValue(Result.Loading)
                            val response = serviceHistoryRepository.postServiceHistoryMaintenance(serviceHistory)
                            _postServiceHistoryResult.postValue(Result.Success(response))
                            serviceHistoryRepository.scheduleLocalNotification(maintenanceDetails,response.data.id.toString())
                            Timber.v(response.meta.message)
                            serviceHistoryRepository.clearTables()
                        }catch (exception: Exception){
                            _postServiceHistoryResult.postValue(Result.Error(exception))
                            exception.printStackTrace()
                        }
                    }
                    else -> {
                        try{
                            _updateServiceHistoryResult.postValue(Result.Loading)
                            val response = serviceHistoryRepository.updateServiceHistoryMaintenance(serviceHistoryId,serviceHistory)
                            _updateServiceHistoryResult.postValue(Result.Success(response))
                            serviceHistoryRepository.scheduleLocalNotification(maintenanceDetails,serviceHistoryId)
                            Timber.v(response.meta.message)
                            serviceHistoryRepository.clearTables()
                        }catch (exception: Exception){
                            _updateServiceHistoryResult.postValue(Result.Error(exception))
                            exception.printStackTrace()
                        }
                    }
                }

            }

        }

    }

    fun getRegisteredMotorcycles() {
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

    fun clearResults(){
        _postServiceHistoryResult.postValue(null)
        _updateServiceHistoryResult.postValue(null)
    }

    fun getUpcomingEvents(){
        viewModelScope.launch(IO){
            try{
                _upcomingEventsResult.postValue(Result.Loading)
                val response = serviceHistoryRepository.getServiceHistoryList(
                        appSharedPref.getUserId(),
                        3
                )
                _upcomingEventsResult.postValue(Result.Success(response))
            }catch (exception: Exception){
                if(exception is HttpException){
                    val message = exception.getMessage()
                    _upcomingEventsResult.postValue(Result.Error(Exception(message)))
                }else{
                    _upcomingEventsResult.postValue(Result.Error(exception))
                }
            }
        }
    }


}