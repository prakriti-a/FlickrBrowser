package com.prakriti.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import android.view.View
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

// hold download statuses for callbacks
enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

class GetRawData(private val listener: OnDownloadCompleteListener): AsyncTask<String, Void, String>() { // passing a listener object thru constructor
    // pass String url as input
// perform download on separate thread

    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

//    private var listener: MainActivity? = null

    // use interfaces -> a binding contract
    interface OnDownloadCompleteListener {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }

    override fun doInBackground(vararg params: String?): String { // nullable type String array -> array will be non-null, items may be null
        Log.i(TAG, "doInBackground called")
        if(params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED // empty url
            return "No URL Specified"
        }
        try {
            downloadStatus = DownloadStatus.OK // change when exception is raised
            return URL(params[0]).readText()
        } catch (e: Exception) {
            val errorMessage = when(e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground: Invalid URL: ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IOException reading data: ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: Security Exception: ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "doInBackground: Unknown Error: ${e.message}"
                }
            }
            Log.e(TAG, "doInBackground: $errorMessage")
            return errorMessage
        }
    }

    // setting listener for callbacks
//    fun setDownloadCompleteLister(callbackObject: MainActivity) { // this needs to be called before starting the async task
//        listener = callbackObject
//    }

    override fun onPostExecute(result: String) { // dont process the result here, to avoid tight coupling
        Log.d(TAG, "onPostExecute called") //: parameter is $result")
        // callback listener when download is complete
        listener.onDownloadComplete(result, downloadStatus) // since doInBg() does not return null String as result, change fn signature
            // also, listener can not be null, as its passed when this class is instantiated
    }
}