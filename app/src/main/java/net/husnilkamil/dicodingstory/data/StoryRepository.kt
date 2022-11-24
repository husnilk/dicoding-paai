package net.husnilkamil.dicodingstory.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.*
import net.husnilkamil.dicodingstory.data.database.StoryDatabase
import net.husnilkamil.dicodingstory.data.entity.Story
import net.husnilkamil.dicodingstory.data.network.ApiService
import net.husnilkamil.dicodingstory.data.request.StoryRequest
import net.husnilkamil.dicodingstory.data.response.InsertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val token: String, private val database: StoryDatabase, private val apiService: ApiService) {

    private val resultAddStory = MediatorLiveData<InsertResponse>()
    fun insert(storyRequest: StoryRequest): LiveData<InsertResponse> {
        val response = apiService.addStories(
            storyRequest.token,
            storyRequest.file,
            storyRequest.description
        )
        response.enqueue(object : Callback<InsertResponse?> {

            override fun onResponse(call: Call<InsertResponse?>, response: Response<InsertResponse?>) {
                val objectResponse: InsertResponse? = response.body()
                if (objectResponse != null && !objectResponse.error!!) {
                    if (!objectResponse.error) {
                        resultAddStory.value = objectResponse!!
                    }
                }else{
                    resultAddStory.value = InsertResponse(true, "Oops")
                }
            }

            override fun onFailure(call: Call<InsertResponse?>, t: Throwable) {
                resultAddStory.value = InsertResponse(true, t.message.toString());
            }
        })
        return resultAddStory
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