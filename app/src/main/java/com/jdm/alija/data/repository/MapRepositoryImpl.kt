package com.jdm.alija.data.repository

import com.jdm.alija.data.di.AUTH
import com.jdm.alija.data.remote.MapService
import com.jdm.alija.domain.model.Location
import com.jdm.alija.domain.repository.MapRepository
import com.skydoves.sandwich.onSuccess
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    @AUTH val mapService: MapService
): MapRepository {
    override suspend fun getLocation(query: String, onSuccess: (Location) -> Unit) {
        mapService.getLocation(query).onSuccess {
            if (!this.data.documents.isEmpty()) {
                onSuccess(Location(this.data.documents[0].y, this.data.documents[0].x))
            }
        }
    }
}