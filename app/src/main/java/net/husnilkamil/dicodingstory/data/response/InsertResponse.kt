package net.husnilkamil.dicodingstory.data.response

import com.google.gson.annotations.SerializedName

data class InsertResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
