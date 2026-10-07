package dev.abdullah.messseat.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.abdullah.messseat.data.dto.auth.AuthResponse
import dev.abdullah.messseat.data.dto.user.UserResponse
import dev.abdullah.messseat.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class SessionManager(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
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

    @Volatile
    private var user: User? = null

    init {
        runBlocking {
            val prefs = dataStore.data.first()
            access = prefs[Keys.ACCESS]
            refresh = prefs[Keys.REFRESH]
            user = prefs[Keys.USER]?.let {
                runCatching {
                    json.decodeFromString<UserResponse>(it)
                        .toDomain()
                }.getOrNull()
            }
        }
    }

    val userFlow = dataStore.data.map { prefs ->
        prefs[Keys.USER]?.let {
            runCatching {
                json.decodeFromString<UserResponse>(it)
                    .toDomain()
            }.getOrNull()
        }
    }

    val isLoggedInFlow = dataStore.data.map { prefs ->
        prefs[Keys.ACCESS] != null
    }

    fun accessToken() = access
    fun refreshToken() = refresh

    fun user() = user

    suspend fun saveSession(auth: AuthResponse) {
        access = auth.accessToken
        refresh = auth.refreshToken
        user = auth.user.toDomain()

        dataStore.edit { prefs ->
            prefs[Keys.ACCESS] = auth.accessToken
            prefs[Keys.REFRESH] = auth.refreshToken
            prefs[Keys.USER] = json.encodeToString(auth.user)
        }
    }

    suspend fun updateToken(newAccess: String, newRefresh: String) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS] = newAccess
            prefs[Keys.REFRESH] = newRefresh
        }
    }

    suspend fun updateUser(user: UserResponse) =
        dataStore.edit { it[Keys.USER] = json.encodeToString(user) }


    suspend fun clear() = dataStore.edit {
        access = null
        refresh = null
        it.remove(Keys.ACCESS)
        it.remove(Keys.REFRESH)
        it.remove(Keys.USER)
    }
}