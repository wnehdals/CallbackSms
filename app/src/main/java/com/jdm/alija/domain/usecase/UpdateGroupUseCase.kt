package com.jdm.alija.domain.usecase

import android.util.Log
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.presentation.ui.group.GroupViewModel
import java.util.Calendar
import javax.inject.Inject

class UpdateGroupUseCase  @Inject constructor(
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke(id: Int,
                                name: String,
                                contactList: List<Contact> = listOf(),
                                isIncallActive: Boolean = false,
                                isOutcallActivie: Boolean = false,
                                isReleaseCallActive: Boolean = false,
                                dupicateIdx: Int = 0,
                                incallText: String = "",
                                incallImg: String = "",
                                outcallText: String = "",
                                outcallImg: String = "",
                                releaseCallText: String = "",
                                releaseCallImg: String = "",
                                mon: Boolean = false,
                                tue: Boolean = false,
                                wed: Boolean = false,
                                thu: Boolean = false,
                                fri: Boolean = false,
                                sat: Boolean = false,
                                sun: Boolean = false,
                                isBeforeCheck: Boolean = true
    ) : Int {
        return groupRepository.update(
            Group(
                id, name, contactList, isIncallActive, isOutcallActivie, isReleaseCallActive, dupicateIdx, incallText, incallImg, outcallText, outcallImg, releaseCallText, releaseCallImg, mon, tue, wed, thu, fri, sat, sun, isBeforeCheck
            )
        )
    }
    suspend fun execute(group: Group) : Int{
        return groupRepository.update(group)
    }
}
