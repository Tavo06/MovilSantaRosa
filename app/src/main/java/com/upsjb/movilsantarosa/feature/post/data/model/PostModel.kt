package com.upsjb.movilsantarosa.feature.post.data.model


import kotlinx.serialization.Serializable

@Serializable
data class PostModel(

    val id: String = "",

    val type: PostType = PostType.ANNOUNCEMENT,

    val title: String = "",

    val description: String = "",

    val priority: Priority = Priority.NORMAL,

    val createdBy: String = "",

    val createdByName: String = "",

    val createdAt: Long = 0L,

    val expiredAt: Long = 0L,

    val imageUrl: String = "",

    val attachmentUrl: String = "",

    val latitude: Double? = null,

    val longitude: Double? = null,

    val address: String = ""

)
@Serializable
enum class PostType {

    ANNOUNCEMENT,

    ALERT;

    val displayName: String
        get() = when (this) {

            ANNOUNCEMENT -> "Anuncio"

            ALERT -> "Alerta"

        }

}
@Serializable
enum class Priority {

    LOW,

    NORMAL,

    IMPORTANT,

    URGENT;

    val displayName: String
        get() = when (this) {

            LOW -> "Baja"

            NORMAL -> "Normal"

            IMPORTANT -> "Importante"

            URGENT -> "Urgente"

        }

}
