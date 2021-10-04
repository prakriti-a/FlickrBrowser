package com.prakriti.flickrbrowser

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.io.IOException
import java.io.ObjectStreamException
//import java.io.Serializable

// to implement serializable correctly
// add static serial version UID to class

class Photo(private var title: String, private var author: String, private var authorId: String, private var link: String,
            private var tags: String, private var image: String) : Parcelable {

    private val TAG = "PhotoModelClass"

    // auto generated implementation for Parcelable

    constructor(parcel: Parcel) : this( // error: add non null asserted call
        parcel.readString()!!, parcel.readString()!!, parcel.readString()!!, parcel.readString()!!, parcel.readString()!!, parcel.readString()!! ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(authorId)
        parcel.writeString(link)
        parcel.writeString(tags)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }

    // ############################################################################################################### //

    override fun toString(): String {
        return "Photo(title='$title', author='$author', authorId='$authorId', link='$link', tags='$tags', image='$image')"
    }

    fun getTitle(): String {
        return title
    }

    fun getAuthor(): String {
        return author
    }

    fun getAuthorId(): String {
        return authorId
    }

    fun getLink(): String {
        return link
    }

    fun getTags(): String {
        return tags
    }

    fun getImage(): String {
        return image
    }

/* CODE FOR SERIALIZABLE

    companion object {
        private const val serialVersionUID =
            1L // to check that data retrieved is the same version as the data stored
        // auto generated by java, but diff machines might generate different UIDs
    }

    // to handle serializn & de-serializn, implement:
    // if implemented like this, Serializable is fast
    @Throws(IOException::class)
    private fun writeObject(out: java.io.ObjectOutputStream) {
        Log.d(TAG, "writeObject called")
        out.writeUTF(title) // write to stream
        out.writeUTF(author)
        out.writeUTF(authorId)
        out.writeUTF(link)
        out.writeUTF(tags)
        out.writeUTF(image)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(instream : java.io.ObjectInputStream) { // to read data offstream & put values to class properties
        Log.d(TAG, "readObject called")
        title = instream.readUTF() // allowed for vars, not vals
        author = instream.readUTF()
        authorId = instream.readUTF()
        link = instream.readUTF()
        tags = instream.readUTF()
        image = instream.readUTF()
    }

    @Throws(ObjectStreamException::class)
    private fun readObjectNoData() { // for subclasses of serialized classes
        Log.d(TAG, "readObjectNoData called")
    }
*/

}