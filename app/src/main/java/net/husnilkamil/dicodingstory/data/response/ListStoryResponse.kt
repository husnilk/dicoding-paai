package net.husnilkamil.dicodingstory.data.response

import com.google.gson.annotations.SerializedName
import net.husnilkamil.dicodingstory.data.entity.Story

data class ListStoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<Story>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)


