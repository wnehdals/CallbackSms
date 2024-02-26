package com.jdm.alija.data.entity

import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group

fun MobileEntity.toContract(): Contact {
    return Contact(
        id = id,
        name = name,
        numbers = numbers
    )
}
fun GroupEntity.toGroup(): Group {
    return Group(
        id = id,
        name = name
    )
}
fun Group.toGroupEntity(): GroupEntity {
    val groupEntity = GroupEntity(name = name)
    groupEntity.id = id
    return groupEntity
}