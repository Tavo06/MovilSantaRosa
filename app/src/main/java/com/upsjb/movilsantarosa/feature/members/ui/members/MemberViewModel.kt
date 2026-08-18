package com.upsjb.movilsantarosa.feature.members.ui.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsjb.movilsantarosa.feature.members.domain.usecase.GetActiveMembersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    getActiveMembersUseCase: GetActiveMembersUseCase
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val membersFlow = getActiveMembersUseCase()

    val uiState = combine(
        membersFlow,
        query
    ) { members, query ->

        MemberUiState.Success(
            members = members,
            query = query
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MemberUiState.Loading
        )

    fun updateQuery(query: String) {
        this.query.value = query
    }
}