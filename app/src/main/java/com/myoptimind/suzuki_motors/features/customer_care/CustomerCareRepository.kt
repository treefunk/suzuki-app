package com.myoptimind.suzuki_motors.features.customer_care

import com.myoptimind.suzuki_motors.features.customer_care.api.CustomerCareService
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