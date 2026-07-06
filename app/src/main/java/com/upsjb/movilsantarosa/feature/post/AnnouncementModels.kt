// domain/models/AnnouncementModels.kt
package com.upsjb.movilsantarosa.feature.post

data class Announcement(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val isImportant: Boolean = false
)

enum class AnnouncementTab {
    HOME,
    MEMBERS,
    PAYMENTS,
    FINES,
    ANNOUNCEMENTS
}