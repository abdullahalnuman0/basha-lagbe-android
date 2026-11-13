package dev.example.messseat.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val FAVORITE_POST_IDS =
            stringSetPreferencesKey("favorite_post_ids")
    }

    val favoritePostIds: Flow<Set<String>> =
        dataStore.data.map { preferences ->
            preferences[Keys.FAVORITE_POST_IDS] ?: emptySet()
        }

    suspend fun addFavorite(postId: Long) {
        dataStore.edit { preferences ->
            val current = preferences[Keys.FAVORITE_POST_IDS] ?: emptySet()

            preferences[Keys.FAVORITE_POST_IDS] =
                current + postId.toString()
        }
    }

    suspend fun removeFavorite(postId: Long) {
        dataStore.edit { preferences ->
            val current = preferences[Keys.FAVORITE_POST_IDS] ?: emptySet()

            preferences[Keys.FAVORITE_POST_IDS] =
                current - postId.toString()
        }
    }

    suspend fun isFavorite(postId: Long): Boolean =
        dataStore.data.first()[Keys.FAVORITE_POST_IDS]
            ?.contains(postId.toString()) == true

}