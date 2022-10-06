package com.example.storyapp.UI

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.Model.LoginRequest
import com.example.storyapp.Model.RequestRegister
import com.example.storyapp.Preference.StoryUserPreference
import com.example.storyapp.R
import com.example.storyapp.UI.ViewModel.LoginViewModel
import com.example.storyapp.UI.ViewModel.RegisterViewModel
import com.example.storyapp.UI.ViewModel.StoryUserViewModel
import com.example.storyapp.UI.ViewModel.ViewModelFactory
import com.example.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    lateinit var name: String
    private lateinit var email: String
    private lateinit var pass: String
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "register")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        setupActionBar()
        setRegisterNow()
        animationIsActive()
        observe()
        preferenceObserve()
        showPassword()
        setThemeView()
    }

    private fun setThemeView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private  fun preferenceObserve(){

        val preferences = StoryUserPreference.getUserPreference(dataStore)
        val storyUserViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[StoryUserViewModel::class.java]

        storyUserViewModel.getUserLoginAuth().observe(this) { model ->
            if (model) {
                val register = Intent(this@RegisterActivity, ListStoryActivity::class.java)
                register.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(register)
                finish()
            } else {
                storyUserViewModel.saveTokenAuth("")
                storyUserViewModel.saveUserName("")
            }
        }

        loginViewModel.userLoginAuth.observe(this) {
            storyUserViewModel.saveUserLoginAuth(true)
            storyUserViewModel.saveTokenAuth(it.loginResult.token)
            storyUserViewModel.saveUserName(it.loginResult.name)

        }
    }


    private  fun observe(){
        registerViewModel.messages.observe(this) {
            checkUserResponseRegisterAuth(it, registerViewModel.isErrorMessage)
        }

        registerViewModel.isLoadingActive.observe(this) {
            loadingIsActive(it)
        }


        loginViewModel.isLoadingActive.observe(this) {
            loadingIsActive(it)
        }

    }

    private fun setRegisterNow() {
        registerBinding.btnRegister.setOnClickListener {
            registerBinding.apply {
                edEmailRegister.clearFocus()
                edNameRegister.clearFocus()
                edPasswordRegister.clearFocus()
            }
            checkUserData()

        }
    }

    private  fun checkUserData(){
        if (dataIsValid()) {
            name = registerBinding.edNameRegister.text.toString().trim()
            email = registerBinding.edEmailRegister.text.toString().trim()
            pass = registerBinding.edPasswordRegister.text.toString().trim()
            val register = RequestRegister(
                name,
                email,
                pass
            )
            registerViewModel.getUserResponseRegisterAuth(register)
        }
    }

    private  fun showPassword(){
        registerBinding.cbSeePassword.setOnClickListener {
            if (registerBinding.cbSeePassword.isChecked) {
                registerBinding.edPasswordRegister.transformationMethod = HideReturnsTransformationMethod.getInstance()

            } else {
                registerBinding.edPasswordRegister.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }




    private fun checkUserResponseRegisterAuth(message: String, registerIsError: Boolean) {
        if (!registerIsError) {
            userWasCreated()
            val userLogin = LoginRequest(
                email,
                pass
            )
            loginViewModel.getUserResponseLoginAuth(userLogin)

        } else {
            when (message) {
                "Bad Request" -> {
                    showBadRequest()
                }
                "timeout" -> {
                    showTimeOut()
                }
                else -> {
                   showErrorMessage()
                }
            }
        }
    }

    private  fun userWasCreated(){
        Toast.makeText(this, getString(R.string.the_user_created), Toast.LENGTH_LONG).show()
    }
    private fun showBadRequest(){

        Toast.makeText(this, getString(R.string.valid_email_address), Toast.LENGTH_LONG).show()
        registerBinding.edEmailRegister.apply {
            setText("")
            requestFocus()
        }
    }

    private  fun showErrorMessage(){
        Toast.makeText(this, getString(R.string.timeout_time), Toast.LENGTH_LONG)
            .show()
    }



    private fun showTimeOut(){
        Toast.makeText(this, getString(R.string.timeout_time), Toast.LENGTH_LONG).show()
    }

    private fun dataIsValid(): Boolean {
        return registerBinding.edNameRegister.userNameIsValid && registerBinding.edEmailRegister.userEmailIsValid &&
                registerBinding.edPasswordRegister.userPasswordIsValid
    }


    private fun loadingIsActive(isLoading: Boolean) {
        registerBinding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    @SuppressLint("RestrictedApi")
    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun animationIsActive() {
        val titleRegister = ObjectAnimator.ofFloat(registerBinding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val nameRegister = ObjectAnimator.ofFloat(registerBinding.tvName, View.ALPHA, 1f).setDuration(500)
        val nameTextViewRegister = ObjectAnimator.ofFloat(registerBinding.tilName, View.ALPHA, 1f).setDuration(500)
        val emailTextViewRegister = ObjectAnimator.ofFloat(registerBinding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayoutRegister = ObjectAnimator.ofFloat(registerBinding.tilEmail, View.ALPHA, 1f).setDuration(500)
        val passwordTextViewRegister = ObjectAnimator.ofFloat(registerBinding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayoutRegister = ObjectAnimator.ofFloat(registerBinding.tilPassword, View.ALPHA, 1f).setDuration(500)
        val seePassword= ObjectAnimator.ofFloat(registerBinding.cbSeePassword, View.ALPHA, 1f).setDuration(500)
        val buttonRegister = ObjectAnimator.ofFloat(registerBinding.btnRegister, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                titleRegister,
                nameRegister,
                nameTextViewRegister,
                emailTextViewRegister,
                emailEditTextLayoutRegister,
                passwordTextViewRegister,
                passwordEditTextLayoutRegister,
                seePassword,
                buttonRegister,
            )
            startDelay = 500
        }.start()
    }



}