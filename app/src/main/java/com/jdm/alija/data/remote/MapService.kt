package com.jdm.alija.data.remote

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {
    @GET("v2/local/search/address.json")
    suspend fun getLocation(@Query("query") query: String): ApiResponse<LocationResp>
}