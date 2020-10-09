package com.myoptimind.suzuki_app.features.whats_new

import com.myoptimind.suzuki_app.features.whats_new.api.WhatsNewService
import javax.inject.Inject

class WhatsNewRepository @Inject constructor(
        private val whatsNewService: WhatsNewService
){
    suspend fun getWhatsNew(
            categoryId: String?,
            offset: Int?,
            limit: Int?
    ): WhatsNewService.WhatsNewResponse {
        return whatsNewService.getArticles(
                categoryId,
                offset?.toString(),
                limit?.toString()
        )
    }
}