package com.flowerencee9.storyapp.screens.launcher

import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.ActivityMainBinding
import com.flowerencee9.storyapp.screens.auth.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sp: SoundPool
    private var soundId: Int = 0
//    private var spLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSound()
        setupView()
    }

    private fun setupSound() {
        sp = SoundPool.Builder()
            .setMaxStreams(20)
            .build()

        sp.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
//                startActivity(LoginActivity.newIntent(this))
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(LoginActivity.newIntent(this))
                }, 5000)
            } else {
                Toast.makeText(this@MainActivity, "Gagal load", Toast.LENGTH_SHORT).show()
            }
        }
        soundId = sp.load(this, R.raw.noot, 1)
    }

    private fun setupView() {
        Handler(Looper.getMainLooper()).postDelayed({
            sp.play(soundId, 1f, 1f, 0, 0, 1f)
        }, 500)
    }
}