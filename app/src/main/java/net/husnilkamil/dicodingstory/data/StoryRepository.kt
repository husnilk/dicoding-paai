package net.husnilkamil.dicodingstory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import net.husnilkamil.dicodingstory.data.database.StoryDatabase
import net.husnilkamil.dicodingstory.data.entity.Story
import net.husnilkamil.dicodingstory.data.network.ApiService
import net.husnilkamil.dicodingstory.data.request.StoryRequest
import net.husnilkamil.dicodingstory.data.response.InsertResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class StoryRepository(private val token: String, private val database: StoryDatabase, private val apiService: ApiService) {

    fun insert(storyRequest: StoryRequest): LiveData<Result<InsertResponse>> = liveData{

        emit(Result.Loading)

        val desc = storyRequest.description.toRequestBody("text/plain".toMediaType())
        val img = storyRequest.file!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultiPart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", storyRequest.file.name, img)

        val response = apiService.addStories(
            storyRequest.token,
            imageMultiPart,
            desc
        )

        try {
            emit(Result.Success(response))
        }catch (t: Throwable){
            emit(Result.Error(t.message.toString()))
        }
    }

    //Paging
    fun getStories(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, database, apiService),
            pagingSourceFactory = {
                database.storyDao().getStories()
            }
        ).liveData
    }
}