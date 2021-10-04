package com.prakriti.flickrbrowser

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrViewHolder(view: View) : RecyclerView.ViewHolder(view) { // since inner class should be static (java), for kotlin, put classes up here

    private var photo: ImageView = view.findViewById(R.id.photo)
    private var title: TextView = view.findViewById(R.id.title)

    fun getPhoto(): ImageView {
        return photo
    }

    fun getTitle(): TextView {
        return title
    }
}

class FlickrRVAdapter(private var photoList: List<Photo>) : RecyclerView.Adapter<FlickrViewHolder>() {

    private val TAG = "FlickrRVAdapter" // 23 char max for log TAG

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrViewHolder {
        // called by Layout Manager when it needs a new view
        Log.i(TAG, "onCreateViewHolder called: new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
            // ^passing root as null to inflate() may cause problems with themes, etc
        return FlickrViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlickrViewHolder, position: Int) { // will always provide a view holder, non null
        // called by layout mgr when data needs to be stored in vh to be displayed
        Log.i(TAG, "onBindViewHolder called ")
        // if returned result list is empty, notify user
        if(photoList.isEmpty()) {
            holder.getPhoto().setImageResource(R.drawable.baseline_image_black_48)
            holder.getTitle().setText(R.string.no_search_results)
        } else {
            // Picasso is Singleton, downloads image on bg thread, also caches images
            Picasso.get()
                .load(photoList[position].getImage())
                .error(R.drawable.baseline_image_black_48)
                .placeholder(R.drawable.baseline_image_black_48)
                .into(holder.getPhoto())
            holder.getTitle().text = photoList[position].getTitle()
            // all view exist in a context, and have getContext()
        }
    }

    override fun getItemCount(): Int {
        return if (photoList.isNotEmpty()) photoList.size else 1 // to show one holder item in case no results match the query
    }

    fun loadNewData(newPhotoList: List<Photo>) {
        Log.i(TAG, "loadNewData called")
        photoList = newPhotoList
        notifyDataSetChanged() // registered observers (rv) are informed, refresh display
    }

    fun getPhotoItem(position: Int): Photo? {
        return if(photoList.isNotEmpty()) photoList[position] else null
    }
}