package net.husnilkamil.dicodingstory.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import net.husnilkamil.dicodingstory.R
import net.husnilkamil.dicodingstory.data.entity.Story
import net.husnilkamil.dicodingstory.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    var name: String? = null
    var photo: String? = null
    var description: String? = null
    var date: String? = null
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val storyIntent = intent
        if (storyIntent != null) {
            val story = storyIntent.getParcelableExtra<Story>("STORY_PARCELABLE")
            loadStoryDetail(story)
        }
    }

    private fun loadStoryDetail(story: Story?) {
        name = story!!.name
        photo = story.photoUrl
        description = story.description
        date = story.createdAt
        binding!!.tvDetailDescription.text = description
        binding!!.tvDetailName.text = name
        binding!!.tvDetailDate.text = date
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding!!.ivDetailPhoto)
    }
}