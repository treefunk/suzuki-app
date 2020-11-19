package com.myoptimind.suzuki_app.features.suzuki_diary.service_history.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data.ServiceHistoryDetails
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data.ServiceHistoryMaintenance

@Dao
interface ServiceHistoryDao {

    @Query("SELECT * FROM service_history_details LIMIT 1")
    suspend fun getServiceHistoryDetails(): ServiceHistoryDetails?

    @Query("SELECT * FROM service_history_maintenance WHERE tab_type = :type")
    suspend fun getServiceMaintenance(type: String): ServiceHistoryMaintenance?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addServiceHistoryDetails(serviceHistoryDetails: ServiceHistoryDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addServiceHistoryMaintenance(serviceHistoryMaintenance: ServiceHistoryMaintenance)


}