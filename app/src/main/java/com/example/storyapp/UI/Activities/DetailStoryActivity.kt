package com.example.storyapp.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.Model.ListStory

import com.example.storyapp.Preference.StoryUserPreference
import com.example.storyapp.R
import com.example.storyapp.UI.ViewModel.StoryUserViewModel
import com.example.storyapp.UI.ViewModel.ViewModelFactory
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import java.text.SimpleDateFormat

class DetailStoryActivity : AppCompatActivity() {
    companion object {
        const val LIST_EXTRA_STORIES = "extra_story"
    }
    private var isEnding = false
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")
    private  lateinit var  detailStoryBinding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_story)
        detailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(detailStoryBinding.root)
        showDetailStory()

    }



    private  fun showDetailStory(){
        val detailStory = intent.getParcelableExtra<ListStory>(LIST_EXTRA_STORIES)!!
        showStoryDetailInformationUser(detailStory)
    }

    private  fun showStoryDetailInformationUser(detailStory: ListStory){
        val pastDateDetailFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'").parse(detailStory.createdAt)
        val presentDateDetailFormat = SimpleDateFormat("dd/MM/yyyy").format(pastDateDetailFormat!!)

        detailStoryBinding.apply {
            tvDetailName.text = detailStory.name
            tvDetailDescription.text = detailStory.description
            tvDetailDate.text = presentDateDetailFormat
        }

        Glide.with(this)
            .load(detailStory.photoUrl)
            .into(detailStoryBinding.ivDetailPhoto)
    }


    override fun onCreateOptionsMenu(itemMenu: Menu): Boolean {
        val menu = menuInflater
        menu.inflate(R.menu.option_menu_item, itemMenu)
        return super.onCreateOptionsMenu(itemMenu)
    }

    override fun onOptionsItemSelected(subjectItem: MenuItem): Boolean {
        return  when(subjectItem.itemId){
            R.id.language_menu -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }
            R.id.logout_menu -> {
                logOutDialogActive()
                true
            }

            else -> true
        }
    }

    private  fun logOutDialogActive(){
        val maker= AlertDialog.Builder(this)
        val dialog = maker.create()
        maker
            .setTitle(getString(R.string.check_logOut))
            .setMessage(getString(R.string.are_you_sure))
            .setPositiveButton(getString(R.string.check_no)){_, _ ->
                dialog.cancel()
            }
            .setNegativeButton(getString(R.string.check_yes)){_, _ ->
                userLogOutNow()
            }
            .show()
    }

    private  fun userLogOutNow(){
        val preferences = StoryUserPreference.getUserPreference(dataStore)
        val storyUserViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[StoryUserViewModel::class.java]
        storyUserViewModel.apply {
            saveUserLoginAuth(false)
            saveTokenAuth("")
            saveUserName("")
        }
        isEnding = true
        val intent = Intent(this@DetailStoryActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}