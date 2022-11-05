package com.flowerencee9.storyapp.screens.auth.register

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.flowerencee9.storyapp.R
import com.flowerencee9.storyapp.databinding.ActivityAuthFormBinding
import com.flowerencee9.storyapp.models.request.RegisterRequest
import com.flowerencee9.storyapp.screens.auth.AuthViewModel
import com.flowerencee9.storyapp.screens.auth.login.LoginActivity
import com.flowerencee9.storyapp.support.*
import com.flowerencee9.storyapp.support.customs.CustomInput
import com.flowerencee9.storyapp.support.customs.CustomInput.TYPE.EMAIL
import com.flowerencee9.storyapp.support.customs.CustomInput.TYPE.TEXT

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthFormBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthFormBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {

        val nameWatcher = object : CustomInput.InputListener {
            override fun afterTextChanged(input: String) {
                binding.edtName.setCleartext(input.isNotEmpty())
                setupButtonStates()
            }

        }

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
                gotoLogin()
            }

        }

        ObjectAnimator.ofFloat(binding.containerLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        with(binding) {
            edtName.apply {
                toShow()
                setInpuType(TEXT)
                setListener(nameWatcher)
                setHint(getString(R.string.hint_name))
            }
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
                    getString(R.string.asking_login),
                    getString(R.string.click_login),
                    spanListener
                )
                movementMethod = LinkMovementMethod.getInstance()
            }
            btnLogin.apply {
                text = getString(R.string.button_register)
                setOnClickListener { registerUser() }
            }

            viewModel.loadingStates.observe(this@RegisterActivity){
                loading.loadingContainer.showView(it)
                Log.d(TAG, "loading $it")
            }
        }
        setupButtonStates()
    }

    private fun gotoLogin() {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    private fun registerUser() {
        with(binding) {
            val request = RegisterRequest(
                edtEmail.getText(),
                edtName.getText(),
                edtPassword.getText()
            )
            viewModel.registerUser(request)
            viewModel.basicResponse.observe(this@RegisterActivity) {
                Log.d(TAG, "response $it")
                binding.root.snackbar(it.message)
                if (!it.error) Handler(Looper.getMainLooper()).postDelayed({ gotoLogin() }, 3000)
            }
        }
    }

    private fun setupButtonStates() {
        with(binding) {
            btnLogin.isEnabled = edtName.isValid() && edtEmail.isValid() && edtPassword.isValid()
        }
    }

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, RegisterActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            return intent
        }
    }
}