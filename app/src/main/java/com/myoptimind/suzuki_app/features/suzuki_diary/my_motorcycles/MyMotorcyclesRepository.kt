package com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles

import com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.api.MyMotorcyclesService
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data.ServiceHistoryDetails
import retrofit2.http.Field
import retrofit2.http.Query
import javax.inject.Inject

class MyMotorcyclesRepository @Inject constructor(
        private val myMotorcyclesService: MyMotorcyclesService
){
    suspend fun registerMyMotorcycle(
            beastNickName: String,
            customerId: String,
            motorcycleModelId: String,
            engineNumber: String,
            frameNumber: String,
            datePurchased: String,
            purchasedIn: String
    ): MyMotorcyclesService.RegisterMotorcycleResponse {
        return myMotorcyclesService.registerMotorcycle(
                beastNickName,
                customerId,
                motorcycleModelId,
                engineNumber,
                frameNumber,
                datePurchased,
                purchasedIn
        )
    }

    suspend fun updateRegisteredMyMotorcycle(
            registeredMotorcycleId: String,
            beastNickName: String,
            customerId: String,
            motorcycleModelId: String,
            engineNumber: String,
            frameNumber: String,
            datePurchased: String,
            purchasedIn: String
    ): MyMotorcyclesService.RegisterMotorcycleResponse {
        return myMotorcyclesService.updateRegisteredMotorcycle(
                registeredMotorcycleId,
                beastNickName,
                customerId,
                motorcycleModelId,
                engineNumber,
                frameNumber,
                datePurchased,
                purchasedIn
        )
    }

    suspend fun getMotorcycleModels(
            name: String? = null,
            categoryName: String? = null,
            offset: Int? = null,
            limit: Int? = null
    ): MyMotorcyclesService.GetMotorcycleModelsListResponse {
        return myMotorcyclesService.getMotorcycleModels(
                name,
                categoryName,
                offset?.toString(),
                limit?.toString()
        )
    }

    suspend fun getRegisteredMotorcycles(customerId: String): MyMotorcyclesService.RegisteredMotorcyclesResponse = myMotorcyclesService.getRegisteredMotorcycles(customerId)



}