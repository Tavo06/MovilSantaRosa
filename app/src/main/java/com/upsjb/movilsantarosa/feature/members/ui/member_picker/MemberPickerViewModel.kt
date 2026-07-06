package com.upsjb.movilsantarosa.feature.members.ui.member_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetAllMembersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MemberPickerViewModel @Inject constructor(
    getAllMembersUseCase: GetAllMembersUseCase
) : ViewModel() {

    private val query = MutableStateFlow("")

    private val membersFlow = getAllMembersUseCase()

    val uiState = combine(
        membersFlow,
        query
    ) { members, query ->

        MemberPickerUiState.Success(
            members = members,
            query = query
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MemberPickerUiState.Loading
        )

    fun updateQuery(newQuery: String) {
        query.value = newQuery
    }
}