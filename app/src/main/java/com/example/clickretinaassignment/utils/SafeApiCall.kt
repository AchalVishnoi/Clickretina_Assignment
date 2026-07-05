package com.example.clickretinaassignment.utils

import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

suspend inline fun <DTO, DOMAIN> safeApiCall(
    crossinline apiCall: suspend () -> Response<DTO>,
    crossinline mapper: (DTO) -> DOMAIN
): Result<DOMAIN> {

    return try {

        val response = apiCall()

        if (response.isSuccessful) {

            val body = response.body()

            if (body != null) {
                Result.success(mapper(body))
            } else {
                Result.failure(Exception("Response body is null"))
            }

        } else {

            Result.failure(
                Exception("HTTP ${response.code()}: ${response.message()}")
            )

        }

    } catch (e: SocketTimeoutException) {

        Result.failure(Exception("Request timed out", e))

    } catch (e: IOException) {

        Result.failure(Exception("No internet connection", e))

    } catch (e: Exception) {

        Result.failure(e)

    }
}