package com.chestbox.app

import android.content.Context
import androidx.core.content.edit

class PrefHelper(private val context: Context) {


    fun getSavedLink(): String? {
        val sharedPreferences =
            context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("url", null)
    }

    fun saveLink(url: String) {
        val sharedPreferences =
            context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit { putString("url", url) }
    }
}