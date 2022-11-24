package net.husnilkamil.dicodingstory.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.husnilkamil.dicodingstory.data.entity.Story
import net.husnilkamil.dicodingstory.databinding.ItemStoryBinding

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var listener: StoryClickListener

    fun setListener(listener: StoryClickListener) {
        this.listener = listener
    }

    interface StoryClickListener {
        fun storyClickListener(story: Story)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener {
                listener.storyClickListener(data);
            }
        }
    }

    class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.tvItemName.text = story.name
            Glide.with(binding.root).load(story.photoUrl).into(binding.ivItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}