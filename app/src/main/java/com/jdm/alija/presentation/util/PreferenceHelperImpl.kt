package com.jdm.alija.presentation.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceHelper @Inject constructor(@ApplicationContext val appContext: Context)  {
    private val TAG = this.javaClass.simpleName
    private val prefName = "ALIJA_PREFUTIL"

    private val preference: SharedPreferences = appContext.getSharedPreferences(
        "${prefName}_$TAG",
        Context.MODE_PRIVATE
    )

    /** key */
    private val keyStopGuide = "pref_key_stop_guide"

    fun setStopGuide(value: Boolean) {
        setPreference(keyStopGuide, value)
    }
    fun getStopGuide() : Boolean {
        return preference.getBoolean(keyStopGuide, false)
    }
    private fun setPreference(key: String, nValue: Int): Boolean {
        val edit = preference.edit()
        edit.putInt(key, nValue)
        return edit.commit()
    }

    private fun setPreference(key: String, value: Long): Boolean {
        val edit = preference.edit()
        edit.putLong(key, value)
        return edit.commit()
    }

    private fun setPreference(key: String, strValue: String?): Boolean {
        val edit = preference.edit()
        edit.putString(key, strValue)
        return edit.commit()
    }

    private fun setPreference(key: String, strValue: Boolean): Boolean {
        val edit = preference.edit()
        edit.putBoolean(key, strValue)
        return edit.commit()
    }

    private fun setPreference(key: String, value: Float): Boolean {
        val edit = preference.edit()
        edit.putFloat(key, value)
        return edit.commit()
    }
}
