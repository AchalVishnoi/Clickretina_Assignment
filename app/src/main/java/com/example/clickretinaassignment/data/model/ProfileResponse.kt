package com.example.clickretinaassignment.data.model

data class ProfileResponse(
    val user: User
)

data class User(
    val username: String,
    val name: String,
    val location: Location,
    val avatar: String,
    val social: Social,
    val statistics: Statistics
)

data class Location(
    val city: String,
    val country: String
)

data class Social(
    val website: String,
    val profiles: List<SocialProfile>
)

data class SocialProfile(
    val platform: String,
    val url: String
)

data class Statistics(
    val followers: Int,
    val following: Int
)
