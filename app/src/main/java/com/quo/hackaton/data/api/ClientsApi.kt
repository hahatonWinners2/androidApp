package com.quo.hackaton.data.api

import com.quo.hackaton.data.model.CompanyDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ClientsApi {
    @GET("/clients/")
    suspend fun getClients(
        @Query("checked") checked: Boolean = false
    ): List<CompanyDTO>

    @PATCH("/suspicious_clients/{client_id}/comment")
    suspend fun addComment(
        @Path("client_id") id: String,
        @Body comment: String
    )
}