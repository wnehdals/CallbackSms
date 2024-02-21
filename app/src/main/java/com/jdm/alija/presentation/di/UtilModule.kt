package com.jdm.alija.presentation.di

import android.content.Context
import com.jdm.alija.presentation.util.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    @Singleton
    fun providePrefHelper(
        @ApplicationContext appContext: Context
    ): PreferenceHelper {
        return PreferenceHelper(appContext)
    }

}
