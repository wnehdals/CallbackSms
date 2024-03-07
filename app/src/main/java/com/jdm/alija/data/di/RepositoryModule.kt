package com.jdm.alija.data.di

import com.jdm.alija.data.repository.BlackRepositoryImpl
import com.jdm.alija.data.repository.MobileRepositoryImpl
import com.jdm.alija.data.repository.GalleryRepositoryImpl
import com.jdm.alija.data.repository.ContactRepositoryImpl
import com.jdm.alija.data.repository.GroupRepositoryImpl
import com.jdm.alija.domain.repository.BlackRepository
import com.jdm.alija.domain.repository.MobileRepository
import com.jdm.alija.domain.repository.GalleryRepository
import com.jdm.alija.domain.repository.ContactRepository
import com.jdm.alija.domain.repository.GroupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Singleton
    @Binds
    abstract fun bindAlarmRepository(smsRepository: ContactRepositoryImpl): ContactRepository
    @Singleton
    @Binds
    abstract fun bindContractRepository(contractRepository: MobileRepositoryImpl) : MobileRepository
    @Singleton
    @Binds
    abstract fun bindGalleryRepository(galleryRepository: GalleryRepositoryImpl) : GalleryRepository
    @Singleton
    @Binds
    abstract fun bindGroupRepository(groupRepository: GroupRepositoryImpl) : GroupRepository
    @Singleton
    @Binds
    abstract fun bindBlackRepository(blackRepository: BlackRepositoryImpl) : BlackRepository

}
