package com.myoptimind.suzuki_app.features.customer_care

import com.myoptimind.suzuki_app.features.customer_care.api.CustomerCareService
import retrofit2.http.Field
import javax.inject.Inject

class CustomerCareRepository @Inject constructor(
        private val customerCareService: CustomerCareService
) {
    suspend fun submitInquiry(
            subject: String,
            fullname: String,
            landline: String,
            phoneNumber: String,
            emailAddress: String,
            location: String,
            message: String
    ): CustomerCareService.SubmitInquiryResponse {
        return customerCareService.submitInquiry(
                subject,
                fullname,
                landline,
                phoneNumber,
                emailAddress,
                location,
                message
        )
    }
}