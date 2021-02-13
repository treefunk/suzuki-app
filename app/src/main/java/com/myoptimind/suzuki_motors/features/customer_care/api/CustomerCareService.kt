package com.myoptimind.suzuki_motors.features.customer_care.api

import com.myoptimind.suzuki_motors.features.shared.api.MetaResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CustomerCareService {

    @POST("customers/inquiry")
    @FormUrlEncoded
    suspend fun submitInquiry(
            @Field("subject") subject: String,
            @Field("fullname") fullname: String,
            @Field("landline") landline: String,
            @Field("phone_number") phoneNumber: String,
            @Field("email_address") emailAddress: String,
            @Field("location") location: String,
            @Field("message") message: String
    ): SubmitInquiryResponse

    class SubmitInquiryResponse(
            val meta: MetaResponse
    )
}