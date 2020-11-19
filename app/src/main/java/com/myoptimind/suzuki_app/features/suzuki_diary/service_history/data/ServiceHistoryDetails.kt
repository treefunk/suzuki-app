package com.myoptimind.suzuki_app.features.suzuki_diary.service_history.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import javax.annotation.Nonnull

@Entity(tableName = "service_history_details")
data class ServiceHistoryDetails(


        @ColumnInfo(name = "registered_motorcycle_id")
        var registeredMotorcycleId: String?,

        @ColumnInfo(name = "registered_motorcycle_name")
        var registeredMotorcycleName: String?,

        @ColumnInfo(name = "current_odometer_reading")
        var currentOdometerReading: String?,

        @ColumnInfo(name = "purchased_date")
        var purchasedDate: String?,

        @ColumnInfo(name = "next_pms_date")
        var nextPmsDate: String?,

        @ColumnInfo(name ="mileage_left")
        var mileageLeft: String?,

        @PrimaryKey
        @Nonnull
        val id: Int = 1
): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(registeredMotorcycleId)
                parcel.writeString(registeredMotorcycleName)
                parcel.writeString(currentOdometerReading)
                parcel.writeString(purchasedDate)
                parcel.writeString(nextPmsDate)
                parcel.writeString(mileageLeft)
                parcel.writeInt(id)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<ServiceHistoryDetails> {
                override fun createFromParcel(parcel: Parcel): ServiceHistoryDetails {
                        return ServiceHistoryDetails(parcel)
                }

                override fun newArray(size: Int): Array<ServiceHistoryDetails?> {
                        return arrayOfNulls(size)
                }
        }
}