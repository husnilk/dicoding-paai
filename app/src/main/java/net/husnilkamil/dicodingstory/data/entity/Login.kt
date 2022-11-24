package net.husnilkamil.dicodingstory.data.entity

import com.google.gson.annotations.SerializedName

data class Login(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)