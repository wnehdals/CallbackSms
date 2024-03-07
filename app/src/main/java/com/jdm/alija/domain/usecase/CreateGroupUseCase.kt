package com.jdm.alija.domain.usecase

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.repository.ContactRepository
import com.jdm.alija.domain.repository.GroupRepository
import com.jdm.alija.presentation.ui.group.GroupViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Calendar
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke(groupName: String) : Long {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1
        val day = Calendar.getInstance().get(Calendar.DATE)
        Log.e(GroupViewModel.TAG, "${year}-$month-$day")
        return groupRepository.insert(Group(name = groupName))
    }
}
