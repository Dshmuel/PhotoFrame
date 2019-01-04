package com.dimovsoft.photoframe

import android.content.Context
import android.provider.MediaStore

object DataManager
{
    init {
        println("Singleton class invoked.")
    }

    fun getImages(context: Context, listener: (ArrayList<String>) -> Unit) {
        val list = ArrayList<String>()
        val uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        while (cursor.moveToNext()) {
            val absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
            list.add(absolutePathOfImage)
        }

        listener(list)

    }

}