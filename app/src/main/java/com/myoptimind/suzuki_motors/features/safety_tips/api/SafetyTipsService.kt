package com.myoptimind.suzuki_motors.features.safety_tips.api

import com.myoptimind.suzuki_motors.features.safety_tips.data.SafetyTip
import com.myoptimind.suzuki_motors.features.shared.api.MetaResponse
import retrofit2.http.GET


interface SafetyTipsService {

    @GET("contents/safety_tips_list?per_rows=0")
    suspend fun getSafetyTips(): SafetyTipsResponse

    class SafetyTipsResponse(
            val data: List<SafetyTip>,
            val meta: MetaResponse
    )

}