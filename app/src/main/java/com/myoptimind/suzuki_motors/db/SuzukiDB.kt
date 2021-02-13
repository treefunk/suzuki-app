package com.myoptimind.suzuki_motors.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.dao.ServiceHistoryDao
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.data.ServiceHistoryDetails
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.data.ServiceHistoryMaintenance

@Database(entities = [
    ServiceHistoryDetails::class,
    ServiceHistoryMaintenance::class
], version = 1)
abstract class SuzukiDB: RoomDatabase() {
    abstract fun serviceHistoryDao(): ServiceHistoryDao
}