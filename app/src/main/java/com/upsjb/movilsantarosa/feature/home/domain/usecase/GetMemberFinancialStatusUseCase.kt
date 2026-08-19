package com.upsjb.movilsantarosa.feature.home.domain.usecase

import com.upsjb.movilsantarosa.feature.auth.domain.usecase.CurrentUserUseCase
import com.upsjb.movilsantarosa.feature.fine.data.model.FineStatus
import com.upsjb.movilsantarosa.feature.fine.domain.usecase.GetFinesByEmailUseCase
import com.upsjb.movilsantarosa.feature.home.domain.model.MemberFinancialStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMemberFinancialStatusUseCase @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val getFinesByEmailUseCase: GetFinesByEmailUseCase
) {

    operator fun invoke(): Flow<MemberFinancialStatus> {

        return flow {

            val user = currentUserUseCase()
                .getOrElse {
                    throw Exception("No existe una sesión activa.")
                }

            emitAll(
                getFinesByEmailUseCase(user.email).map { fines ->
                    MemberFinancialStatus(
                        activeFineCount = fines.count { it.status == FineStatus.PENDING }
                    )
                }
            )
        }
    }
}
