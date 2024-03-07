package com.jdm.alija.domain.usecase

import android.util.Log
import com.jdm.alija.data.entity.GroupEntity
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.presentation.ui.group.GroupViewModel
import java.util.Calendar
import javax.inject.Inject

class GetDefaultGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke() : Group {
        return groupRepository.getGroup(GroupEntity.DEFAULT_GROUP_ID)
    }
}
