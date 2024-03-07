package com.jdm.alija.presentation.ui.permission

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.jdm.alija.BuildConfig
import com.jdm.alija.domain.model.Version
import com.jdm.alija.presentation.ui.permission.PermissionActivity.Companion.ForceVersionUpdate
import com.jdm.alija.presentation.ui.permission.PermissionActivity.Companion.MoveMain
import com.jdm.alija.presentation.ui.permission.PermissionActivity.Companion.SelectVersionUpdate
import com.jdm.alija.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(): ViewModel() {
    val splashSideEffect = SingleLiveEvent<Int>()
    private val defaultVersion = mapOf("version" to """
                {
                    "minimum" : "1.0.0",
                    "latest" : "1.0.0"
                }
                """.trimIndent())
    fun setRemoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setDefaultsAsync(defaultVersion)
        remoteConfig.setConfigSettingsAsync(configSettings)

    }
    fun getAppVersion(activity: Activity) {
        viewModelScope.launch {
            delay(1000L)
            val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        val json: String = remoteConfig.getString("version")
                        val version: Version = Json.decodeFromString(json)

                        val localVersion = BuildConfig.VERSION_NAME
                        val minVersion = version.minimum
                        val latestVersion = version.latest
                        if (compareVersion(localVersion, minVersion)) {
                            splashSideEffect.value = ForceVersionUpdate
                        } else if (!compareVersion(localVersion, minVersion) && compareVersion(localVersion, latestVersion)) {
                            splashSideEffect.value = SelectVersionUpdate
                        } else {
                            splashSideEffect.value = MoveMain
                        }
                    }

                }.addOnFailureListener {

                }

        }


    }

    private fun compareVersion(currentVersion: String, otherVersion: String): Boolean {
        val currentVersionIntList = currentVersion.split(".").map { it.toInt() }
        val otherVersionIntList = otherVersion.split(".").map { it.toInt() }
        mutableListOf<Int>().apply {
            currentVersionIntList.forEachIndexed { index, i ->
                add(i - (otherVersionIntList.getOrNull(index) ?: 0))
            }
        }.forEach {
            if (it > 0) {
                return false
            } else if (it < 0) {
                return true
            }
        }
        return false
    }
}