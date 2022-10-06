package com.example.storyapp.UI

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
import com.example.storyapp.Preference.StoryUserPreference
import com.example.storyapp.R
import com.example.storyapp.UI.ViewModel.LoginViewModel
import com.example.storyapp.UI.ViewModel.StoryUserViewModel
import com.example.storyapp.UI.ViewModel.ViewModelFactory
import com.example.storyapp.databinding.ActivityLoginBinding
import com.faltenreich.skeletonlayout.Skeleton

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            loginBinding= ActivityLoginBinding.inflate(layoutInflater)
            setContentView(loginBinding.root)
            setLoginNow()
            animationIsActive()
            setThemeView()
            preferenceObserve()
            showPassword()
            toRegister()

        }
        private  fun preferenceObserve(){
            val preferences = StoryUserPreference.getUserPreference(dataStore)
            val storyUserViewModel =
                ViewModelProvider(this, ViewModelFactory(preferences))[StoryUserViewModel::class.java]
            storyUserViewModel.getUserLoginAuth().observe(this) { state ->
                if (state) {
                    val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            loginViewModel.messages.observe(this) {
                val user = loginViewModel.userLoginAuth.value
                checkingUserResponseLoginAuth(it, user?.loginResult?.token, loginViewModel.isErrorMesssage, storyUserViewModel)
            }

            loginViewModel.isLoadingActive.observe(this) {
                loadingIsActive(it)
            }

        }
        private fun setLoginNow() {
            loginBinding.btnLogin.setOnClickListener {
                loginBinding.edEmailLogin.clearFocus()
                loginBinding.edPasswordLogin.clearFocus()
                checkUserData()
            }
        }


       private fun checkUserData(){
           if (dataIsValid()) {
               val user = LoginRequest(
                   loginBinding.edEmailLogin.text.toString().trim(),
                   loginBinding.edPasswordLogin.text.toString().trim()
               )
               loginViewModel.getUserResponseLoginAuth(user)
           }
       }

       private fun toRegister(){
           loginBinding.tvRegister.setOnClickListener {
               val intent = Intent(this, RegisterActivity::class.java)
               startActivity(intent)
           }
       }

       private  fun showPassword(){
           loginBinding.cbSeePassword.setOnClickListener {
               if (loginBinding.cbSeePassword.isChecked) {
                   loginBinding.edPasswordLogin.transformationMethod = HideReturnsTransformationMethod.getInstance()
               } else {
                   loginBinding.edPasswordLogin.transformationMethod = PasswordTransformationMethod.getInstance()
               }
           }
       }

        private fun dataIsValid(): Boolean {
            return loginBinding.edEmailLogin.userEmailIsValid &&  loginBinding.edPasswordLogin.userPasswordIsValid
        }

        private fun checkingUserResponseLoginAuth(
            message: String,
            token: String?,
            isError: Boolean,
            viewModel: StoryUserViewModel
        ) {
            if (!isError) {
                successLogin(message)
                viewModel.saveUserLoginAuth(true)
                if (token != null) {
                    viewModel.saveTokenAuth(token)
                }
                viewModel.saveUserName(loginViewModel.userLoginAuth.value?.loginResult?.name.toString())
            } else {
                when (message) {
                    "Unauthorized" -> {
                        showUnauthorized()
                    }
                    "timeout" -> {
                       showTimeOut()
                    }
                    else -> {
                        errorMessage(message)
                    }
                }
            }
        }
       private  fun successLogin(message: String){
           Toast.makeText(
               this,
               "${getString(R.string.login_successfully)} $message",
               Toast.LENGTH_LONG
           ).show()
       }
       private fun errorMessage(message: String){
           Toast.makeText(
               this,
               "${getString(R.string.error_message)} $message",
               Toast.LENGTH_SHORT
           )
               .show()
       }

       private  fun showUnauthorized(){
           Toast.makeText(this, getString(R.string.this_account_unauthorized), Toast.LENGTH_SHORT)
               .show()

           loginBinding.edEmailLogin.apply {
               setText("")
               requestFocus()
           }
           loginBinding.edPasswordLogin.setText("")
       }

        private fun showTimeOut(){
            Toast.makeText(this, getString(R.string.timeout_time), Toast.LENGTH_SHORT)
                .show()
        }

        private fun loadingIsActive(loadingIsActive: Boolean) {
            loginBinding.loadingProgressBar.visibility = if (loadingIsActive) View.VISIBLE else View.GONE
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

    private fun animationIsActive() {
        val titleLogin = ObjectAnimator.ofFloat(loginBinding.tvTitleLogin, View.ALPHA, 1f).setDuration(500)
        val emailTextViewLogin = ObjectAnimator.ofFloat(loginBinding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayoutLogin = ObjectAnimator.ofFloat(loginBinding.tilEmail, View.ALPHA, 1f).setDuration(500)
        val passwordTextViewLogin = ObjectAnimator.ofFloat(loginBinding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayoutLogin = ObjectAnimator.ofFloat(loginBinding.tilPassword, View.ALPHA, 1f).setDuration(500)
        val seePassword= ObjectAnimator.ofFloat(loginBinding.cbSeePassword, View.ALPHA, 1f).setDuration(500)
        val buttonLogin = ObjectAnimator.ofFloat(loginBinding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val donTHaveAnAccount= ObjectAnimator.ofFloat(loginBinding.tvDonTHaveAnAccount, View.ALPHA, 1f).setDuration(500)
        val textViewRegister= ObjectAnimator.ofFloat(loginBinding.tvRegister, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                titleLogin,
                emailTextViewLogin,
                emailEditTextLayoutLogin,
                passwordTextViewLogin,
                passwordEditTextLayoutLogin,
                seePassword,
                buttonLogin,
                donTHaveAnAccount,
                textViewRegister
            )
            startDelay = 500
        }.start()
    }

}