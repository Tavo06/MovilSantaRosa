package com.upsjb.movilsantarosa.feature.post.ui.post_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.post.domain.model.toDomain
import com.upsjb.movilsantarosa.feature.post.domain.model.toForm
import com.upsjb.movilsantarosa.feature.post.domain.usecase.GetPostByIdUseCase
import com.upsjb.movilsantarosa.feature.post.domain.usecase.RegisterPostUseCase
import com.upsjb.movilsantarosa.feature.post.domain.usecase.UpdatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostFormViewModel @Inject constructor(
    private val registerPostUseCase: RegisterPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostFormUiState())
    val uiState = _uiState.asStateFlow()

    fun updateForm(transform: PostFormState.() -> PostFormState) {
        _uiState.update {
            it.copy(form = it.form.transform())
        }
    }

    fun setMode(mode: PostFormMode) {
        _uiState.update {
            it.copy(mode = mode)
        }
    }

    fun loadPost(id: String) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    mode = PostFormMode.READ_ONLY,
                    actionState = PostFormActionState.Loading
                )
            }

            getPostByIdUseCase(id)
                .onSuccess { post ->

                    _uiState.update {
                        it.copy(
                            form = post.toForm(),
                            postId = post.id,
                            actionState = PostFormActionState.Idle
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            actionState = PostFormActionState.Error(
                                "No se pudo cargar la publicación"
                            )
                        )
                    }
                }
        }
    }

    fun savePost() {
        viewModelScope.launch {

            val state = _uiState.value
            val form = state.form

            validateForm(form)?.let { message ->
                _uiState.update {
                    it.copy(actionState = PostFormActionState.Error(message))
                }
                return@launch
            }

            _uiState.update {
                it.copy(actionState = PostFormActionState.Loading)
            }

            val currentUserResult = currentUserUseCase()

            val currentUserName = currentUserResult
                .getOrNull()
                ?.firstname
                .orEmpty()

            val post = form
                .copy(createdBy = currentUserName)
                .toDomain()

            val result = when (state.mode) {

                PostFormMode.CREATE -> registerPostUseCase(post)

                PostFormMode.EDIT -> updatePostUseCase(post)

                PostFormMode.READ_ONLY -> return@launch
            }

            result
                .onSuccess {
                    _uiState.update {
                        it.copy(actionState = PostFormActionState.Success)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            actionState = PostFormActionState.Error(
                                error.message ?: "Error desconocido"
                            )
                        )
                    }
                }
        }
    }

    fun resetAction() {
        _uiState.update {
            it.copy(actionState = PostFormActionState.Idle)
        }
    }

    fun clearLocation() {
        _uiState.update {
            it.copy(
                form = it.form.copy(
                    latitude = "",
                    longitude = "",
                    address = ""
                )
            )
        }
    }

    private fun validateForm(form: PostFormState): String? {
        return when {

            form.title.isBlank() ->
                "Ingrese un título."

            form.description.isBlank() ->
                "Ingrese una descripción."

            form.type.name.isBlank() ->
                "Seleccione un tipo de publicación."

            form.priority.name.isBlank() ->
                "Seleccione una prioridad."

            else -> null
        }
    }
}