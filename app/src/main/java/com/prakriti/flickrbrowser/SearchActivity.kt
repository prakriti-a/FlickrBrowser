package com.prakriti.flickrbrowser

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.preference.PreferenceManager
import com.prakriti.flickrbrowser.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity() {
// in manifest, Search activity is set (via intent-filter) as the activity to be launched upon action_search

    private val TAG = "SearchActivity"

    private var searchView: android.widget.SearchView? = null
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activateToolbar(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.i(TAG, "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.search_activity_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager // system search services
            // searchMgr requires intent filter for action.search activity in manifest
        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView // get reference to search view widget embedded in toolbar
        val searchableInfo = searchManager.getSearchableInfo(componentName) // use searchMgr to retrieve searchable info from searchable.xml
            // component name -> refs to search activity
//        Log.d(TAG, "onCreateOptionsMenu: component name is $componentName")
//        Log.d(TAG, "onCreateOptionsMenu: hint is ${searchView?.queryHint}")
//        Log.d(TAG, "onCreateOptionsMenu: searchable info is $searchableInfo")

        searchView?.setSearchableInfo(searchableInfo) // set searchable info into search view widget
        searchView?.isIconified = false // setting false - opens the activity with focus on search bar & keyboard appearing
            // if set true, search options not visible till icon is clicked

        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit called")
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext) // get default shared pref obj
                    // appContext -> bcoz diff activities will store & retrieve the data
                sharedPref.edit().putString(FLICKR_QUERY, query).apply()
                searchView?.clearFocus()

                finish() // closes this activity & will return to main activity
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return false // not handling this, so let default implementation handle text changes
            }
        })

        searchView?.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                finish()
                return false
            }
        })

        return true
    }
    // REF: https://developer.android.com/guide/topics/search


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.app_bar_search ->
//        }
//        return super.onOptionsItemSelected(item)
//    }

}