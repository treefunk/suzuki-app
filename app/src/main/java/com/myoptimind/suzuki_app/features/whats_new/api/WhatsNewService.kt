package com.myoptimind.suzuki_app.features.whats_new.api

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.features.whats_new.data.Article
import com.myoptimind.suzuki_app.features.shared.api.MetaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WhatsNewService {

    @GET("contents/articles")
    suspend fun getArticles(
            @Query("category_id") categoryId: String? = null,
            @Query("offset") offset: String? = null,
            @Query("per_rows") limit: String? = null
    ): WhatsNewResponse

    class WhatsNewResponse (
            val data: Data,
            val meta: MetaResponse
    ){

        inner class Filter(
                val id: String,
                val name: String
        )

        inner class Data(
                val result: List<Article>,
                @SerializedName("filter_by")
                val filters: List<Filter>
        )

    }


}