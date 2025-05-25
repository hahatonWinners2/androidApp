package com.quo.hackaton.data.api

import com.quo.hackaton.data.model.CommentRequest
import com.quo.hackaton.data.model.CompanyDTO
import com.quo.hackaton.data.model.SuspiciousClientRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ClientsApi {
    @GET("/clients/")
    suspend fun getClients(
//        @Query("checked") checked: Boolean = false
    ): List<CompanyDTO>

    @POST("/suspicious_clients/")
    suspend fun markSuspicious(@Body request: SuspiciousClientRequest)

    @DELETE("/suspicious_clients/{client_id}/")
    suspend fun unmarkSuspicious(@Path("client_id") id: String)

    @PATCH("/suspicious_clients/{client_id}/comment")
    suspend fun addComment(
        @Path("client_id") id: String,
        @Body request: CommentRequest
    )
}