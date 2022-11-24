package net.husnilkamil.dicodingstory.ui.main

import net.husnilkamil.dicodingstory.data.entity.Story

object DummyData {

    fun genereteStoryEntity() : List<Story> {
        val storyList: MutableList<Story> = arrayListOf()
        for (i in 0..10){
            val story = Story(
                "$i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                "name $i",
                "description ke-$i",
                null,
                null,
            )
            storyList.add(story)
        }
        return storyList
    }
}