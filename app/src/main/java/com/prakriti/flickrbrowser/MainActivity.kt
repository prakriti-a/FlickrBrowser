package com.prakriti.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : BaseActivity(), GetRawData.OnDownloadCompleteListener, GetFlickrJsonData.OnDataAvailableListener,
        RecyclerItemClickListener.OnRecyclerClickListener { // make it a listener

    private val TAG = "MainActivity" // since companion object didn't do much to help in terms of memory
    private val flickrBaseURL = "https://www.flickr.com/services/feeds/photos_public.gne"

    private var flickrRVAdapter = FlickrRVAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        supportActionBar?.setDisplayHomeAsUpEnabled(false) -> gave ERROR
//        activateToolbar(false) // this is Home page

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerView, this))
        recyclerView.adapter = flickrRVAdapter

        // moved to onResume()
//        val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne", "sunsets", "en-us", true)
//            // ?tags=android&format=json&nojsoncallback=1"
//        val getRawData = GetRawData(this) // so multiple classes can use this class
//
////        getRawData.setDownloadCompleteLister(this) // register as listener, async task class then calls onDownloadComplete() in onPostExecute()
//            // this -> telling getRawData.class to call onDlComplete() in "this" class instance, i.e. in MainActivity
//        getRawData.execute(url)

        Log.i(TAG, "onCreate ended")
    }

    private fun createUri(baseUrl: String, searchCriteria: String, lang: String, matchAll: Boolean): String {
        Log.i(TAG, "createUri called")
        var uri = Uri.parse(baseUrl)
            .buildUpon()
            .appendQueryParameter("tags", searchCriteria) // append...() method returns a builder with valid uri
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build()
        return uri.toString()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.i(TAG, "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i(TAG, "onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.menu_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    // event driven programming
    override fun onDownloadComplete(data: String, status: DownloadStatus) { // use override keyword for compiler checks
        // parse json data here
        if(status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: Success") // with data: $data")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            Log.d(TAG, "onDownloadComplete: Failed with status $status\nError: $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailable called") //, data is:\n$data")
//        flickrRVAdapter = FlickrRVAdapter(data)
        flickrRVAdapter.loadNewData(data)
    }

    override fun onDataError(exception: Exception) {
        Log.e(TAG, "onDataError called, Exception: ${exception.message}")
    }


    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, "onItemClick called")
        Toast.makeText(this, "Tap at $position", Toast.LENGTH_SHORT).show()

    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, "onItemLongClick called")
        //Toast.makeText(this, "Long tap at $position", Toast.LENGTH_SHORT).show()
        val photo = flickrRVAdapter.getPhotoItem(position) // returns nullable Photo?
        if(photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java) // class literal to pass class as param
            intent.putExtra(PHOTO_TRANSFER, photo) // since Photo class is serializable, can be passed
            // Serializable -> object can be stored and retrieved by converting it to a byte stream and back
            startActivity(intent)
        }
    }


    override fun onResume() { // called after onCreate
        Log.i(TAG, "onResume called")
        super.onResume()
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY, "") // "" is default value
        if(queryResult != null && queryResult.isNotEmpty()) {
            val url = createUri(flickrBaseURL, queryResult, resources.getString(R.string.query_lang), true)
            val getRawData = GetRawData(this) // so multiple classes can use this class
            getRawData.execute(url)
        }
        Log.i(TAG, "onResume ends")
    }


    // companion obj is similar to static var, but not entirely
//    companion object {
//        private const val TAG = "MainActivity" // using const makes the var 'inline' -> the string is replaced on each line of usage
//    }

}