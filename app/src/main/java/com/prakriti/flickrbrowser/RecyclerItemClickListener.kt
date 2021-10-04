package com.prakriti.flickrbrowser

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val listener: OnRecyclerClickListener)
        : RecyclerView.SimpleOnItemTouchListener() {
// callback is used in listeners

    private val TAG = "RCItemClickListener"

    interface OnRecyclerClickListener { // implemented by listener
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    // gesture detector
    private val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean { // or use onSingleTapConfirmed() for only single not double taps
            // listener is notified when single tap occurs
            Log.i(TAG, "onSingleTapUp called")
            val childView = recyclerView.findChildViewUnder(e.x, e.y) // find child under motion coordinates
            // MotionEvent is nullable, but this fn is only called on tap event
            Log.d(TAG, "onSingleTapUp calling listener.onItemClick")
            if (childView != null) {
                listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
            // childView is nullable, and listeners implemented fn takes non null param, so use null check or assert !!
            }
            return true //super.onSingleTapUp(e)
        }

        override fun onLongPress(e: MotionEvent) {
            Log.i(TAG, "onLongPress called")
            val childView = recyclerView.findChildViewUnder(e.x, e.y) // returned value is nullable here
            Log.d(TAG, "onLongPress calling listener.onItemLongClick")
            if (childView != null) {
                listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
            }
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, "onInterceptTouchEvent called: ${e.action}")
        // pass event ot GestureDetector
        val result = gestureDetector.onTouchEvent(e)
        // whatever GestureDetector handles returns true, if not handled, returns false
        Log.d(TAG, "onInterceptTouchEvent returning: $result")
        return result
        // returning true -> we have intercepted & handled each event, so android won't try to handle it
            // return false if not handled, or return super() call for other listeners to respond
    }
}