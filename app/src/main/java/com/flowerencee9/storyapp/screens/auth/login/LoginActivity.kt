package com.flowerencee9.storyapp.screens.auth.login

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.ActivityAuthFormBinding
import com.flowerencee9.storyapp.models.request.LoginRequest
import com.flowerencee9.storyapp.screens.auth.AuthViewModel
import com.flowerencee9.storyapp.screens.auth.register.RegisterActivity
import com.flowerencee9.storyapp.screens.main.MainActivity
import com.flowerencee9.storyapp.support.*
import com.flowerencee9.storyapp.support.customs.CustomInput
import com.flowerencee9.storyapp.support.customs.CustomInput.TYPE.EMAIL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthFormBinding
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isLogin()) startActivity(MainActivity.newIntent(this))
        binding = ActivityAuthFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        val emailWatcher = object : CustomInput.InputListener {
            override fun afterTextChanged(input: String) {
                binding.edtEmail.setCleartext(input.isNotEmpty())
                setupButtonStates()
            }

        }

        val passwordWatcher = object : CustomInput.InputListener {
            override fun afterTextChanged(input: String) {
                setupButtonStates()
            }

        }

        val spanListener = object : SpannableListener {
            override fun onClick() {
                gotoRegister()
            }

        }

        ObjectAnimator.ofFloat(binding.containerLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        with(binding) {
            edtName.toHide()
            edtEmail.apply {
                setInpuType(EMAIL)
                setListener(emailWatcher)
                setHint(getString(R.string.hint_email))
            }
            edtPassword.apply {
                setListener(passwordWatcher)
                setHint(getString(R.string.hint_password))
                setVisiblePassword()
                setMinLength(6)
            }
            tvRegister.apply {
                text = spanText(
                    getString(R.string.asking_register),
                    getString(R.string.click_register),
                    spanListener
                )
                movementMethod = LinkMovementMethod.getInstance()
            }
            btnLogin.setOnClickListener {
                loginUser()
            }
        }

        setupButtonStates()
        viewModel.loadingStates.observe(this) {
            binding.loading.loadingContainer.showView(it)
            Log.d(TAG, "loading $it")
        }
    }

    private fun gotoRegister() {
        startActivity(RegisterActivity.newIntent(this))
    }

    private fun setupButtonStates() {
        with(binding) {
            btnLogin.isEnabled = edtEmail.isValid() && edtPassword.isValid()
        }
    }

    private fun loginUser() {
        with(binding) {
            val request = LoginRequest(
                edtEmail.getText(),
                edtPassword.getText()
            )
            viewModel.loginUser(request)
            viewModel.basicResponse.observe(this@LoginActivity) {
                Log.d(TAG, "login response $it")
                if (!it.error) startActivity(MainActivity.newIntent(this@LoginActivity))
                else binding.root.snackbar(it.message)
            }
        }
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            return intent
        }
    }
}