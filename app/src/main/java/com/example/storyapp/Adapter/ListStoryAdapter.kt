package com.example.storyapp.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.Model.ListStory
import com.example.storyapp.UI.DetailStoryActivity
import com.example.storyapp.databinding.ItemListStoryBinding


class ListStoryAdapter(private val listStoriesModelUser: List<ListStory>) :
    RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickStoriesCallback: OnItemClickCallback

    fun setOnClickStoriesItemCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickStoriesCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, listPosition: Int) {
        holder.bind(listStoriesModelUser[listPosition])
        holder.itemView.setOnClickListener {
           val adapter = Intent(it.context, DetailStoryActivity::class.java)
            adapter.putExtra(DetailStoryActivity.LIST_EXTRA_STORIES, listStoriesModelUser[listPosition])
            it.context.startActivity(adapter,
                ActivityOptionsCompat
                    .makeSceneTransitionAnimation(it.context as Activity)
                    .toBundle()
            )
        }
    }

    class ListViewHolder(private var binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storiesData: ListStory) {
            binding.tvName.text = storiesData.name
            Glide.with(itemView.context)
                .load(storiesData.photoUrl)
                .into(binding.ivItemPhoto)
        }
    }

    override fun getItemCount(): Int = listStoriesModelUser.size

    interface OnItemClickCallback {
        fun onItemClicked(stories: ListStory)
    }



}