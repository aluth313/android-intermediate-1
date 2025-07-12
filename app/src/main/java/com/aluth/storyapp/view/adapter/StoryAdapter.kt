package com.aluth.storyapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aluth.storyapp.databinding.ItemStoryBinding
import com.aluth.storyapp.model.data.Story
import com.aluth.storyapp.utils.AppConst
import com.aluth.storyapp.view.DetailStoryActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson

class StoryAdapter : ListAdapter<Story, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class MyViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story){
            binding.tvItemName.text = story.name
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            binding.clItemStory.setOnClickListener {

                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(AppConst.STORY, Gson().toJson(story))
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.ivItemPhoto, AppConst.PHOTO),
                    Pair(binding.tvItemName, AppConst.NAME),
                )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}