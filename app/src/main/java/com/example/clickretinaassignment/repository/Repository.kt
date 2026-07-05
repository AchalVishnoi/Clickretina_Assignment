package com.example.clickretinaassignment.domain.repository

import com.example.clickretinaassignment.data.network.dto.Main_dto
import okhttp3.Response

interface Repository {
    suspend fun getAllDetails(): Result<Main_dto>
}