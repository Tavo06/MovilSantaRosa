package com.upsjb.movilsantarosa.feature.post.domain.model

import com.upsjb.movilsantarosa.core.utils.currentTimeMillis
import com.upsjb.movilsantarosa.core.utils.toDateString
import com.upsjb.movilsantarosa.feature.post.data.model.PostModel
import com.upsjb.movilsantarosa.feature.post.data.model.PostType
import com.upsjb.movilsantarosa.feature.post.data.model.Priority
import com.upsjb.movilsantarosa.feature.post.ui.post_form.PostFormState

data class Post(
    val id: String = "",
    val type: PostType = PostType.ANNOUNCEMENT,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.NORMAL,
    val createdBy: String = "",
    val createdAt: String = currentTimeMillis().toDateString(),
    val expiredAt: String = currentTimeMillis().toDateString(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String = ""
)

fun PostModel.toDomain(): Post =
    Post(
        id = id,
        type = type,
        title = title,
        description = description,
        priority = priority,
        createdBy = createdBy,
        createdAt = createdAt,
        expiredAt = expiredAt,
        latitude = latitude,
        longitude = longitude,
        address = address
    )

fun Post.toModel(): PostModel =
    PostModel(
        id = id,
        type = type,
        title = title,
        description = description,
        priority = priority,
        createdBy = createdBy,
        createdAt = createdAt,
        expiredAt = expiredAt,
        latitude = latitude,
        longitude = longitude,
        address = address
    )

fun PostFormState.toDomain(): Post =
    Post(
        id = id,
        type = type,
        title = title,
        description = description,
        priority = priority,
        createdBy = createdBy,
        createdAt = createdAt,
        expiredAt = expiredAt,
        latitude = latitude.toDoubleOrNull(),
        longitude = longitude.toDoubleOrNull(),
        address = address
    )

fun Post.toForm(): PostFormState =
    PostFormState(
        id = id,
        type = type,
        title = title,
        description = description,
        priority = priority,
        createdBy = createdBy,
        createdAt = createdAt,
        expiredAt = expiredAt,
        latitude = latitude?.toString().orEmpty(),
        longitude = longitude?.toString().orEmpty(),
        address = address
    )