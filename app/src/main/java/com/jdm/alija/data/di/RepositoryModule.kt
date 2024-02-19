package com.jdm.alija.data.di

import com.jdm.alija.data.repository.ContractRepositoryImpl
import com.jdm.alija.data.repository.GalleryRepositoryImpl
import com.jdm.alija.data.repository.SmsRepositoryImpl
import com.jdm.alija.domain.repository.ContractRepository
import com.jdm.alija.domain.repository.GalleryRepository
import com.jdm.alija.domain.repository.SmsRepository
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
    abstract fun bindAlarmRepository(smsRepository: SmsRepositoryImpl): SmsRepository
    @Singleton
    @Binds
    abstract fun bindContractRepository(contractRepository: ContractRepositoryImpl) : ContractRepository
    @Singleton
    @Binds
    abstract fun bindGalleryRepository(galleryRepository: GalleryRepositoryImpl) : GalleryRepository

}
