package com.example.clickretinaassignment.data.network.api

import com.example.clickretinaassignment.data.network.dto.Main_dto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("notes/refs/heads/main/data.json")
    suspend fun getAllDetails(): Response<Main_dto>
}