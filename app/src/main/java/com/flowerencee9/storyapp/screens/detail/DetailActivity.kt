package com.flowerencee9.storyapp.screens.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.flowerencee9.storyapp.databinding.ActivityDetailBinding
import com.flowerencee9.storyapp.models.response.Story

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        val story = intent.getParcelableExtra<Story>(EXTRA_STORY) as Story
        Glide.with(this).load(story.photoUrl).into(binding.imageView)
        binding.tvDetailName.text = story.name
        binding.tvDetailDesc.text = story.description
    }

    companion object {
        fun newIntent(context: Context, item: Story) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_STORY, item)
            }

        private const val EXTRA_STORY = "STORY"
    }
}