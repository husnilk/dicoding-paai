package net.husnilkamil.dicodingstory.utils

import android.content.Context
import net.husnilkamil.dicodingstory.data.StoryRepository
import net.husnilkamil.dicodingstory.data.database.StoryDatabase
import net.husnilkamil.dicodingstory.data.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        val token = getToken(context)
        return StoryRepository(token, database, apiService)
    }
}