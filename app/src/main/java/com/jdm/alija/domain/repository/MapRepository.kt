package com.jdm.alija.domain.repository

import com.jdm.alija.domain.model.Location

interface MapRepository {
    suspend fun getLocation(query: String, onSuccess: (Location) -> Unit)
}