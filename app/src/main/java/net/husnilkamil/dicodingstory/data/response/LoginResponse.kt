package net.husnilkamil.dicodingstory.data.response

import com.google.gson.annotations.SerializedName
import net.husnilkamil.dicodingstory.data.entity.Login

data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResult: Login? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)


