package dev.abdullah.messseat.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.JsonAdapter
import dev.abdullah.messseat.data.dto.auth.AuthResponse
import dev.abdullah.messseat.data.dto.user.UserResponse
import dev.abdullah.messseat.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class SessionManager(
    private val dataStore: DataStore<Preferences>,
    private val userAdapter: JsonAdapter<UserResponse>
) {

    private object Keys {
        val ACCESS = stringPreferencesKey("access_token")
        val REFRESH = stringPreferencesKey("refresh_token")
        val USER = stringPreferencesKey("user_json")
    }


    @Volatile
    private var access: String? = null

    @Volatile
    private var refresh: String? = null

    init {
        runBlocking {
            val prefs = dataStore.data.first()
            access = prefs[Keys.ACCESS]
            refresh = prefs[Keys.REFRESH]
        }
    }

    val userFlow = dataStore.data.map { prefs ->
        prefs[Keys.USER]?.let {
            runCatching {
                userAdapter.fromJson(it)?.toDomain()
            }.getOrNull()
        }
    }

    val isLoggedInFlow = dataStore.data.map { prefs ->
        prefs[Keys.ACCESS] != null
    }

    fun accessToken() = access
    fun refreshToken() = refresh

    suspend fun saveSession(auth: AuthResponse) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS] = auth.accessToken
            prefs[Keys.REFRESH] = auth.refreshToken
            prefs[Keys.USER] = userAdapter.toJson(auth.user)
        }
    }

    suspend fun updateToken(newAccess: String, newRefresh: String) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS] = newAccess
            prefs[Keys.REFRESH] = newRefresh
        }
    }

    suspend fun updateUser(user: UserResponse) =
        dataStore.edit { it[Keys.USER] = userAdapter.toJson(user) }


    suspend fun clear() = dataStore.edit {
        access = null
        refresh = null
        it.remove(Keys.ACCESS)
        it.remove(Keys.REFRESH)
        it.remove(Keys.USER)
    }
}