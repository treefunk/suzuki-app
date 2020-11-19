package com.myoptimind.suzuki_app.features.dealer_locator

import com.myoptimind.suzuki_app.features.dealer_locator.api.DealerLocatorService
import javax.inject.Inject

class DealerLocatorsRepository @Inject constructor(
        private val dealerLocatorService: DealerLocatorService
) {
    suspend fun getDealerLocators(
            name: String?,
            location: String?,
            offset: Int?,
            limit: Int?
    ): DealerLocatorService.DealerLocatorsResponse {
        return dealerLocatorService.getDealerLocators(
                name,
                location,
                offset?.toString(),
                limit?.toString()
        )
    }

    suspend fun getSingleDealerLocator(
            id: String
    ): DealerLocatorService.SingleDealerLocatorResponse {
        return dealerLocatorService.getSingleDealerLocator(id)
    }
}