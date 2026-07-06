package com.upsjb.movilsantarosa.feature.post.domain.model

import com.upsjb.movilsantarosa.feature.post.data.model.PostModel
import com.upsjb.movilsantarosa.feature.post.data.model.PostType
import com.upsjb.movilsantarosa.feature.post.data.model.Priority

data class Post(
    val id: String = "",
    val type: PostType = PostType.ANNOUNCEMENT,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.NORMAL,
    val createdBy: String = "",
    val createdAt: Long = 0L,
    val expireAt: Long = 0L,
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
        expireAt = expiredAt,
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
        expiredAt = expireAt,
        latitude = latitude,
        longitude = longitude,
        address = address
    )