package com.upsjb.movilsantarosa.feature.post.ui.post_form

import com.upsjb.movilsantarosa.core.utils.currentTimeMillis
import com.upsjb.movilsantarosa.feature.auth.domain.model.UserRole
import com.upsjb.movilsantarosa.feature.post.data.model.PostType
import com.upsjb.movilsantarosa.feature.post.data.model.Priority

data class PostFormState(
    val id: String = "",

    val type: PostType = PostType.ANNOUNCEMENT,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.NORMAL,

    val createdBy: String = "",

    val createdAt: Long = currentTimeMillis(),
    val expiredAt: Long = currentTimeMillis(),

    val latitude: String = "",
    val longitude: String = "",
    val address: String = ""
)

data class PostFormUiState(
    val postId: String = "",

    val form: PostFormState = PostFormState(),

    val actionState: PostFormActionState = PostFormActionState.Idle,

    val mode: PostFormMode = PostFormMode.CREATE,
    val role: UserRole = UserRole.PARTNER,
)

sealed interface PostFormActionState {
    data object Idle : PostFormActionState
    data object Loading : PostFormActionState
    data object Success : PostFormActionState
    data class Error(val message: String) : PostFormActionState
}

enum class PostFormMode {
    CREATE,
    EDIT,
    READ_ONLY;

    val displayName: String
        get() = when (this) {
            CREATE -> "Crear publicación"
            EDIT -> "Editar publicación"
            READ_ONLY -> "Detalle de publicación"
        }

    val displayButton: String
        get() = when (this) {
            CREATE -> "Publicar"
            EDIT -> "Actualizar"
            READ_ONLY -> ""
        }
}