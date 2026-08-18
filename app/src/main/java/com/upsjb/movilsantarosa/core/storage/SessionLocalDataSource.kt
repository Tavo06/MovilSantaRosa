package com.upsjb.movilsantarosa.core.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionLocalDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val preferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getSessionId(): String? =
        preferences.getString(KEY_ACTIVE_SESSION_ID, null)

    fun saveSessionId(sessionId: String) {
        preferences.edit()
            .putString(KEY_ACTIVE_SESSION_ID, sessionId)
            .apply()
    }

    fun clearSessionId() {
        preferences.edit()
            .remove(KEY_ACTIVE_SESSION_ID)
            .apply()
    }

    private companion object {
        const val PREFS_NAME = "auth_session_prefs"
        const val KEY_ACTIVE_SESSION_ID = "active_session_id"
    }
}
