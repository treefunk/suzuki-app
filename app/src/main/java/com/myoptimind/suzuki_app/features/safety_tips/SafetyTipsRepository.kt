package com.myoptimind.suzuki_app.features.safety_tips

import com.myoptimind.suzuki_app.features.safety_tips.api.SafetyTipsService
import javax.inject.Inject

class SafetyTipsRepository @Inject constructor(
        private val safetyTipsService: SafetyTipsService
){
    suspend fun getSafetyTips(): SafetyTipsService.SafetyTipsResponse = safetyTipsService.getSafetyTips()
}