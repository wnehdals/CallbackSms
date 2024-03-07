package com.jdm.alija.domain.usecase

import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.GroupRepository
import javax.inject.Inject

class GetGroupByIdUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke(id: Int) : Group {
        return groupRepository.getGroup(id)
    }
}
