package com.example.clickretinaassignment.data.repository

import com.example.clickretinaassignment.data.network.api.ApiService
import com.example.clickretinaassignment.data.network.api.RetrofitClient
import com.example.clickretinaassignment.data.network.dto.Main_dto
import com.example.clickretinaassignment.domain.repository.Repository
import com.example.clickretinaassignment.utils.safeApiCall

class RepositoryImpl(): Repository {
    override suspend fun getAllDetails(): Result<Main_dto> {
        return safeApiCall(
            apiCall = { RetrofitClient.api.getAllDetails() },
            mapper = { it }
        )
    }
}