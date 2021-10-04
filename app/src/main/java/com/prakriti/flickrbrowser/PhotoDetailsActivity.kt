package com.prakriti.flickrbrowser

import android.os.Bundle
import android.util.Log
import com.prakriti.flickrbrowser.databinding.ActivityPhotoDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_details.*

class PhotoDetailsActivity : BaseActivity() {

    private val TAG = "PhotoDetailsActivity"
//    private lateinit var binding: ActivityPhotoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
//        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContentView(R.layout.activity_photo_details)

        activateToolbar(true)

//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        val photo = intent.extras?.getParcelable<Photo>(PHOTO_TRANSFER) as Photo

        // display UI, using string placeholders (%s)
        txtAuthor.text = resources.getString(R.string.photo_details_author, photo.getAuthor())
        txtTitle.text = resources.getString(R.string.photo_details_title, photo.getTitle())
        txtTags.text = resources.getString(R.string.photo_details_tags, photo.getTags())

        Picasso.get().load(photo.getLink()) // full image url
            .error(R.drawable.baseline_image_black_48)
            .placeholder(R.drawable.baseline_image_black_48)
            .into(imgPhoto)


    }


}