package com.example.storyapp.UI

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyaipp.ui.ViewModel.ListStoryViewModel
import com.example.storyapp.Adapter.ListStoryAdapter
import com.example.storyapp.Model.ListStory
import com.example.storyapp.Preference.StoryUserPreference
import com.example.storyapp.R
import com.example.storyapp.UI.ViewModel.StoryUserViewModel
import com.example.storyapp.UI.ViewModel.ViewModelFactory
import com.example.storyapp.databinding.ActivityListStoryBinding
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import kotlinx.android.synthetic.main.activity_login.*

class ListStoryActivity : AppCompatActivity() {
    private  lateinit var  listStoryBinding: ActivityListStoryBinding
    private var isEnding = false
    private  lateinit var token: String
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "list")
    private val  listAddStoryViewModel: ListStoryViewModel by viewModels()
    private lateinit var rvSkeleton: Skeleton

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listStoryBinding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(listStoryBinding.root)
        setOnClick()
        observe()
        layoutManager()
        preference()
    }

    private  fun preference(){
        val preferences = StoryUserPreference.getUserPreference(dataStore)
        val userStoriesViewModel =  ViewModelProvider(this,
            ViewModelFactory(preferences))[StoryUserViewModel::class.java]

        userStoriesViewModel.getTokenAuth().observe(this){
            token = it
            listAddStoryViewModel.getStories(token)
        }
    }

    private  fun layoutManager(){
        val layoutStoriesUser = LinearLayoutManager(this)
        listStoryBinding.rvListStories.layoutManager = layoutStoriesUser
        val itemListUser = DividerItemDecoration(this, layoutStoriesUser.orientation)
        listStoryBinding.rvListStories.addItemDecoration(itemListUser)
    }

    private  fun observe(){
        listAddStoryViewModel.isLoading.observe(this){ 
            loadingIsActive(it)
        }

        listAddStoryViewModel.message.observe(this){
            setUserData(listAddStoryViewModel.listStoriesUser)
            checkStories(it)
        }
    }

    override fun onCreateOptionsMenu(item: Menu): Boolean {
        val menu = menuInflater
        menu.inflate(R.menu.option_menu_item, item)
        return super.onCreateOptionsMenu(item)
    }

    private  fun  setOnClick(){
        listStoryBinding.fabStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }


        listStoryBinding.pullRefresh.setOnRefreshListener {
            listAddStoryViewModel.getStories(token)
            listStoryBinding.pullRefresh.isRefreshing = false
        }

        listStoryBinding.btnRefresh.setOnClickListener {
            listAddStoryViewModel.getStories(token)
        }

        rvSkeleton = listStoryBinding.rvListStories.applySkeleton(R.layout.item_list_story)
    }

    private fun noHaveData(noHaveData: Boolean){
        listStoryBinding.tvNoData.visibility = if (noHaveData) View.VISIBLE else View.GONE
    }

    private fun checkStories(message: String) {
        if (listAddStoryViewModel.isError && !isEnding) {
            Toast.makeText(
                this,
                "${getString(R.string.error_loading)} $message",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private  fun setUserData(listStories: List<ListStory>){
        if (listStories.isEmpty()){
            noHaveData(true)
        } else {
            noHaveData(false)
            val listStoryAdapter = ListStoryAdapter(listStories)
            listStoryBinding.rvListStories.adapter = listStoryAdapter

            listStoryAdapter.setOnClickStoriesItemCallback(object: ListStoryAdapter.OnItemClickCallback{
                override fun onItemClicked(stories: ListStory) {
                    detailStories(stories)
                }

            })
        }
    }

    private fun detailStories(listStories: ListStory) {
        val intent = Intent(this@ListStoryActivity, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.LIST_EXTRA_STORIES, listStories)
        startActivity(intent)
    }


    private  fun loadingIsActive(loadingIsActive: Boolean){
        listStoryBinding.loadingProgressBar.visibility = if (loadingIsActive) View.VISIBLE else View.GONE
        if (loadingIsActive) rvSkeleton.showSkeleton() else rvSkeleton.showOriginal()
    }



    override fun onOptionsItemSelected(subjectItem: MenuItem): Boolean {
        return  when(subjectItem.itemId){
            R.id.language_menu -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }
            R.id.logout_menu -> {
                logOutAlertDialog()
                true
            }

            else -> true
        }
    }

    private  fun logOutAlertDialog(){
        val maker = AlertDialog.Builder(this)
        val dialog= maker.create()
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
        isEnding= true
        val logOut = Intent(this@ListStoryActivity, LoginActivity::class.java)
        startActivity(logOut)
        finish()
    }

}