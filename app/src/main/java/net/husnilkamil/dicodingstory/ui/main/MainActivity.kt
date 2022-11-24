package net.husnilkamil.dicodingstory.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import net.husnilkamil.dicodingstory.R
import net.husnilkamil.dicodingstory.data.entity.Story
import net.husnilkamil.dicodingstory.databinding.ActivityMainBinding
import net.husnilkamil.dicodingstory.ui.ViewModelFactory
import net.husnilkamil.dicodingstory.ui.add.AddStoryActivity
import net.husnilkamil.dicodingstory.ui.detail.DetailActivity
import net.husnilkamil.dicodingstory.ui.login.LoginActivity
import net.husnilkamil.dicodingstory.ui.maps.MapActivity
import net.husnilkamil.dicodingstory.utils.Constant

class MainActivity : AppCompatActivity(), StoryAdapter.StoryClickListener {

    private var isLoggedIn = false
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLoggedInCheck()

        binding.fab.setOnClickListener {
            val addIntent = Intent(this, AddStoryActivity::class.java)
            startActivity(addIntent)
        }

        binding.rvListStory.layoutManager = GridLayoutManager(this, 2)

        getData()
    }

    private fun getData() {
        val adapter = StoryAdapter()
        adapter.setListener(this)
        binding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter {
                adapter.retry()
            }
        )
        mainViewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun isLoggedInCheck(): Unit {
        val preferences = getSharedPreferences(Constant.PREF_KEY_FILE_NAME, MODE_PRIVATE)
        val token = preferences.getString(Constant.PREF_KEY_TOKEN, null)
        isLoggedIn = token != null
        if (!isLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        }
    }

    override fun storyClickListener(story: Story) {
        val storyIntent = Intent(this, DetailActivity::class.java)
        storyIntent.putExtra("STORY_PARCELABLE", story)
        startActivity(storyIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> logout()
            R.id.action_map -> loadmap()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadmap() {
        val mapIntent = Intent(this, MapActivity::class.java)
        startActivity(mapIntent)
    }

    private fun logout() {
        val preferences = getSharedPreferences(Constant.PREF_KEY_FILE_NAME, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}