package com.myoptimind.suzuki_app.features.dealer_locator.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class DealerLocatorsListItem(

        val id: String? = null,

        val name: String? = null,

        @SerializedName("email_address")
        val emailAddress: String? = null,

        @SerializedName("full_address")
        val fullAddress: String? = null,

        val city: String? = null,

        @SerializedName("contact_number")
        val contactNumber: String? = null,

        @SerializedName("mobile_number")
        val mobileNumber: String? = null,

        @SerializedName("google_map")
        val googleMap: String? = null,

        val latitude: String? = null,

        val longitude: String? = null,


        @SerializedName("is_active")
        val isActive: String? = null,

        @SerializedName("tagged_motorcycle")
        val taggedMotorcycle: String? = null,

        @SerializedName("tagged_spare_part")
        val taggedSparePart: String? = null

): Parcelable {
        constructor(parcel: Parcel) : this(
                id = parcel.readString(),
                name = parcel.readString(),
                emailAddress = parcel.readString(),
                fullAddress = parcel.readString(),
                city = parcel.readString(),
                contactNumber = parcel.readString(),
                mobileNumber = parcel.readString(),
                googleMap = parcel.readString(),
                isActive = parcel.readString(),
                taggedMotorcycle = parcel.readString(),
                taggedSparePart = parcel.readString()) {
        }

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel?, flags: Int) {
                if(dest != null){
                        dest.writeString(id)
                        dest.writeString(name)
                        dest.writeString(emailAddress)
                        dest.writeString(fullAddress)
                        dest.writeString(city)
                        dest.writeString(contactNumber)
                        dest.writeString(mobileNumber)
                        dest.writeString(googleMap)
                        dest.writeString(isActive)
                        dest.writeString(taggedMotorcycle)
                        dest.writeString(taggedSparePart)
                }

        }

        companion object CREATOR : Parcelable.Creator<DealerLocatorsListItem> {
                override fun createFromParcel(parcel: Parcel): DealerLocatorsListItem {
                        return DealerLocatorsListItem(parcel)
                }

                override fun newArray(size: Int): Array<DealerLocatorsListItem?> {
                        return arrayOfNulls(size)

                }
        }


}
