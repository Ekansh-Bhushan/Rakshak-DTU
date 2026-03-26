package eu.ekansh.rakshakdtu.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("auth")

class TokenManager(private val context: Context) {

    private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    private val REFRESH_KEY = stringPreferencesKey("refresh_token")
    private val EMAIL_KEY = stringPreferencesKey("user_email")

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun getToken(): String? =
        context.dataStore.data.first()[TOKEN_KEY]

    suspend fun clearToken() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }

    //  Refresh token

    suspend fun saveRefreshToken(token: String) =             // ← add this
        context.dataStore.edit { it[REFRESH_KEY] = token }

    suspend fun getRefreshToken(): String? =                  // ← add this
        context.dataStore.data.first()[REFRESH_KEY]

    // ── Email ──────────────────────────────────────────────────────────────
    suspend fun saveEmail(email: String) {
        context.dataStore.edit { it[EMAIL_KEY] = email }
    }

    suspend fun getEmail(): String? =
        context.dataStore.data.first()[EMAIL_KEY]

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}