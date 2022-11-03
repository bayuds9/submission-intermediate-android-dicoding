package com.flowerencee9.storyapp.screens.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.ActivityHomeBinding
import com.flowerencee9.storyapp.models.response.Story
import com.flowerencee9.storyapp.screens.auth.login.LoginActivity
import com.flowerencee9.storyapp.screens.formuploader.FormUploaderActivity
import com.flowerencee9.storyapp.screens.detail.DetailActivity
import com.flowerencee9.storyapp.screens.locations.MapsActivity
import com.flowerencee9.storyapp.support.*
import com.flowerencee9.storyapp.support.supportclass.LoadingStateAdapter
import com.flowerencee9.storyapp.support.supportclass.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val mainViewModel : HomeViewModel by viewModels {
        ViewModelFactory(this)
    }

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_animation
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_animation
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_animation
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_animation
        )
    }
    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!isLogin()) startActivity(LoginActivity.newIntent(this))
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        setupItemData()
    }

    private fun setupItemData() {
        val dataAdapter = StoryListAdapter { story -> storyClicked(story) }
        binding.rvMainItem.adapter = dataAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                dataAdapter.retry()
            }
        )
        mainViewModel.stories.observe(this){
            dataAdapter.submitData(lifecycle, it)
        }
    }

    private fun setupView() {
        with(binding) {
            rvMainItem.layoutManager = LinearLayoutManager(this@HomeActivity)
            fabParent.setOnClickListener { expandBottom() }
            fabAddContent.setOnClickListener { gotoFormUploader() }
            fabLogout.setOnClickListener { logoutUser() }
            fabToMaps.setOnClickListener {
                startActivity(MapsActivity.newIntent(this@HomeActivity))
            }
        }
    }

    private fun gotoFormUploader() {
//        startActivity(FormUploaderActivity.newIntent(this@HomeActivity))
    }

    private fun logoutUser() {
        removeUserPref()
        startActivity(LoginActivity.newIntent(this))
    }

    private fun expandBottom() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        with(binding) {
            if (clicked) {
                fabLogout.toInvisible()
                fabAddContent.toInvisible()
                Handler(Looper.getMainLooper()).postDelayed({
                    fabLogout.toHide()
                    fabAddContent.toHide()
                }, 300)
            } else {
                fabLogout.toShow()
                fabAddContent.toShow()
            }
        }
    }

    private fun setAnimation(clicked: Boolean) {
        with(binding) {
            if (clicked) {
                fabLogout.startAnimation(toBottom)
                fabAddContent.startAnimation(toBottom)
                fabParent.startAnimation(rotateClose)
            } else {
                fabLogout.startAnimation(fromBottom)
                fabAddContent.startAnimation(fromBottom)
                fabParent.startAnimation(rotateOpen)
            }
        }
    }

    private fun setClickable(clicked: Boolean) {
        with(binding) {
            if (clicked) {
                fabLogout.isClickable = false
                fabAddContent.isClickable = false
            } else {
                fabLogout.isClickable = true
                fabAddContent.isClickable = true
            }
        }
    }

    private fun storyClicked(story: Story) {
        startActivity(DetailActivity.newIntent(this, story))
    }

    companion object {
        private val TAG = HomeActivity::class.java.simpleName
        fun newIntent(context: Context, clearStack: Boolean = false): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            if (clearStack) intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            return intent
        }
    }
}