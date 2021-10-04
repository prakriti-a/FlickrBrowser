package com.prakriti.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class GetFlickrJsonData(private val listener: OnDataAvailableListener) : AsyncTask<String, Void, ArrayList<Photo>>() { // separate thread
    // string input, photo output

    private val TAG = "GetFlickrJsonData"

    interface OnDataAvailableListener {
        fun onDataAvailable(data: List<Photo>)
        fun onDataError(exception: Exception) // json parser throws errors when something goes wrong, its passed to caller
        // cant catch exceptions thrown by code running on a separate thread
    }

    override fun doInBackground(vararg params: String): ArrayList<Photo> {
        Log.i(TAG, "doInBackground called")
        val photoList = ArrayList<Photo>()

        // using org.json package to parse data
        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")
            // loop and extract data required
            for(i in 0 until itemsArray.length()) {
                val jsonPhoto = itemsArray.getJSONObject(i)
                // get properties from each object inside array
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")

                val jsonMedia = jsonPhoto.getJSONObject("media")
                val photoUrl = jsonMedia.getString("m")
                val link = photoUrl.replaceFirst("_m.jpg", "_b.jpg") // displayed photo -> better resolution & larger

                val photoObject = Photo(title, author, authorId, link, tags, photoUrl)
                photoList.add(photoObject)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e(TAG, "doInBackground: Error processing JSON data: ${e.message}")
            cancel(true) // in case of error, cancel task so onPostExecute() is not called after this & empty data is not returned
                // cancel() lets main thread cancel a long running task, async task can also cancel itself
            listener.onDataError(e) // runs on separate thread, so caller cant catch exception
        }
        return photoList
        // onPostExecute() called after this fn
    }

    override fun onPostExecute(result: ArrayList<Photo>) { // doInBg wont return null, change fn signature
        Log.i(TAG, "onPostExecute called")
        listener.onDataAvailable(result) // onDataAvailable() expects a non null param
    }
}