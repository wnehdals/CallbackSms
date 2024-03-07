package com.jdm.alija.data.entity

import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group

fun MobileEntity.toContract(mobile: String): Contact {
    return Contact(
        id = id,
        name = name,
        mobile = mobile
    )
}

fun ContactEntity.toContact(): Contact {
    return Contact(
        id = id,
        mobile = mobile,
        groupName = groupName
    )
}

fun GroupEntity.toGroup(): Group {
    val contactEntities = contactList.map { it.toContact() }
    return Group(
        id = id,
        name = name,
        contactList = contactEntities,
        isIncallActive = isIncallActive,
        isOutcallActivie = isOutcallActivie,
        isReleaseCallActive = isReleaseCallActive,
        dupicateIdx = dupicateIdx,
        incallText = incallText,
        incallImg = incallImg,
        outcallText = outcallText,
        outcallImg = outcallImg,
        releaseCallText = releaseCallText,
        releaseCallImg = releaseCallImg,
        mon = mon,
        tue = tue,
        wed = wed,
        thu = thu,
        fri = fri,
        sat = sat,
        sun = sun,
        isBeforeCheck = isBeforeCheck
    )
}

fun Group.toGroupEntity(isInsert: Boolean = false): GroupEntity {
    val contactEntities = contactList.map { it.toContactEntity() }
    val groupEntity = GroupEntity(
        name = name,
        contactList = contactEntities,
        isIncallActive = isIncallActive,
        isOutcallActivie = isOutcallActivie,
        isReleaseCallActive = isReleaseCallActive,
        dupicateIdx = dupicateIdx,
        incallText = incallText,
        incallImg = incallImg,
        outcallText = outcallText,
        outcallImg = outcallImg,
        releaseCallText = releaseCallText,
        releaseCallImg = releaseCallImg,
        mon, tue, wed, thu, fri, sat, sun, isBeforeCheck
    )
    if (!isInsert) {
        groupEntity.id = id
    }
    return groupEntity
}

fun Contact.toContactEntity(): ContactEntity {
    val contactEntity = ContactEntity(
        id = id,
        mobile = mobile,
        groupName = groupName,
    )
    return contactEntity
}
fun BlackEntity.toBlackContact(): BlackContact {
    return BlackContact(
        id = id,
        mobile = mobile,
        name = name,
        isSelected = true
    )
}
fun BlackContact.toBlackEntity(isInsert: Boolean = false): BlackEntity {
    val entity = BlackEntity(
        mobile = mobile,
        name = name
    )
    if (!isInsert) {
        entity.id = id
    }
    return entity
}