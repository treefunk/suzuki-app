package com.myoptimind.suzuki_app.features.suzuki_diary.service_history

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import com.myoptimind.suzuki_app.db.SuzukiDB
import com.myoptimind.suzuki_app.features.shared.AppSharedPref
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.ServiceHistoryBroadcastReceiver.Companion.EXTRA_BEAST_NICKNAME
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.ServiceHistoryBroadcastReceiver.Companion.EXTRA_SERVICE_HISTORY_NOTIF_TYPE
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.ServiceHistoryBroadcastReceiver.Companion.TYPE_DAY_BEFORE
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.ServiceHistoryBroadcastReceiver.Companion.TYPE_DAY_ITSELF
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.api.ServiceHistoryService
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.dao.ServiceHistoryDao
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data.ServiceHistory
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data.ServiceHistoryDetails
import com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data.ServiceHistoryMaintenance
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ServiceHistoryRepository @Inject constructor(
        private val context: Context,
        private val serviceHistoryService: ServiceHistoryService,
        private val serviceHistoryDao: ServiceHistoryDao,
        private val appSharedPref: AppSharedPref,
        private val suzukiDB: SuzukiDB
) {

    // API operations

    suspend fun getServiceHistoryList(
            customerId: String,
            maxDayLimitFromNow: Int? = null
    ): ServiceHistoryService.ServiceHistoryListResponse {
        return serviceHistoryService.getServiceHistoryList(customerId,maxDayLimitFromNow?.toString())
    }

    suspend fun postServiceHistoryMaintenance(
            serviceHistory: ServiceHistory
    ): ServiceHistoryService.AddServiceHistoryResponse {
        return serviceHistoryService.addServiceHistory(
                serviceHistory.customerId!!,
                serviceHistory.registeredMotorcycleId!!,
                serviceHistory.currentOdometerReading!!,
                serviceHistory.nextPmsDate!!,
                serviceHistory.mileageLeft!!,
                serviceHistory.changeOil,
                serviceHistory.tires,
                serviceHistory.brakes,
                serviceHistory.chainsAndSprockets,
                serviceHistory.airFilter,
                serviceHistory.sparkPlugs,
                serviceHistory.exhaust_muffler,
                serviceHistory.suspension,
                serviceHistory.chassisBoltsNuts,
                serviceHistory.notes,
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
    }

    suspend fun updateServiceHistoryMaintenance(
            serviceHistoryId: String,
            serviceHistory: ServiceHistory
    ): ServiceHistoryService.UpdateServiceHistoryResponse {
        return serviceHistoryService.updateServiceHistory(
                serviceHistoryId,
                serviceHistory.customerId!!,
                serviceHistory.registeredMotorcycleId!!,
                serviceHistory.currentOdometerReading!!,
                serviceHistory.nextPmsDate!!,
                serviceHistory.mileageLeft!!,
                serviceHistory.changeOil,
                serviceHistory.tires,
                serviceHistory.brakes,
                serviceHistory.chainsAndSprockets,
                serviceHistory.airFilter,
                serviceHistory.sparkPlugs,
                serviceHistory.exhaust_muffler,
                serviceHistory.suspension,
                serviceHistory.chassisBoltsNuts,
                serviceHistory.notes,
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
    }


    // DB operations / Room (local storage)

    suspend fun saveServiceHistoryMaintenance(
            serviceHistoryMaintenance: ServiceHistoryMaintenance
    ){
        serviceHistoryDao.addServiceHistoryMaintenance(serviceHistoryMaintenance)
    }

    suspend fun getServiceHistoryDetails(): ServiceHistoryDetails? {
        return serviceHistoryDao.getServiceHistoryDetails()
    }

    suspend fun getServiceHistoryMaintenance(tabtype: AddServiceHistoryMaintenanceFragment.MaintenanceTab): ServiceHistoryMaintenance? {
        return serviceHistoryDao.getServiceMaintenance(tabtype.title)
    }


    suspend fun saveServiceHistoryDetails(
            serviceHistoryDetails: ServiceHistoryDetails
    ){
        serviceHistoryDao.addServiceHistoryDetails(serviceHistoryDetails)
    }


    fun clearTables(){
        suzukiDB.clearAllTables()
    }

    fun scheduleLocalNotification(maintenanceDetails: ServiceHistoryDetails, serviceHistoryId: String) {

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        val parser = DateTimeFormat.forPattern("MMMM dd, yyyy")

        val nextPmsDt = parser.parseDateTime(maintenanceDetails.nextPmsDate)
//        val nextPmsDt = parser.parseDateTime("October 29, 2020")
        val todayDt = DateTime().withTime(0,0,0,0)
        val tommorowDt = todayDt.plusDays(1)

        val currentTime = System.currentTimeMillis()
        val seconds = TimeUnit.SECONDS.toMillis(10)


        Timber.v("seconds before alarm ${seconds}" )
        Timber.v("maintenance details ${maintenanceDetails}")
        val intent = Intent(context,ServiceHistoryBroadcastReceiver::class.java)
        intent.putExtra(EXTRA_BEAST_NICKNAME,maintenanceDetails.registeredMotorcycleName)

        if(nextPmsDt == tommorowDt){
            Timber.v("preparing notif for tommorow..(day itself)")
            // do single notif tomm
            intent.putExtra(EXTRA_SERVICE_HISTORY_NOTIF_TYPE, TYPE_DAY_ITSELF)
            val pendingIntent = PendingIntent.getBroadcast(context,0,intent,0)
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    nextPmsDt.millis,
                    pendingIntent
            )
            Timber.v("successfully prepared notification for tommorow")
        }else if(nextPmsDt != todayDt){
            // do two notifs day before and day itself
            Timber.v("preparing two notifs (day before and day itself)")

            intent.putExtra(EXTRA_SERVICE_HISTORY_NOTIF_TYPE, TYPE_DAY_ITSELF)
            intent.putExtra(EXTRA_BEAST_NICKNAME,maintenanceDetails.registeredMotorcycleName)
            val pendingIntent = PendingIntent.getBroadcast(context,serviceHistoryId.toInt(),intent,0)
            val currentDay = nextPmsDt
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    currentDay.millis,
                    pendingIntent
            )

            intent.removeExtra(EXTRA_SERVICE_HISTORY_NOTIF_TYPE) // refresh type
            intent.putExtra(EXTRA_SERVICE_HISTORY_NOTIF_TYPE, TYPE_DAY_BEFORE)
            val pendingIntentBefore = PendingIntent.getBroadcast(context,serviceHistoryId.toInt() + 1,intent,0)
            val dayBefore = nextPmsDt.minusDays(1)
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    dayBefore.millis,
                    pendingIntentBefore
            )
            val pattern = "MMM dd, yyyy HH:mm:ss"
            Timber.v("successfully prepared notification for ${dayBefore.toString(pattern)} and ${currentDay.toString(pattern)}")
        }else{
            Timber.v("no notification")
        }






    }



}