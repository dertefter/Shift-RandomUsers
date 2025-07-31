package com.dertefter.shiftrandomusers.data.repository


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dertefter.shiftrandomusers.data.model.User
import com.dertefter.shiftrandomusers.data.network.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStore<Preferences>
) {

    private val gson = Gson()
    private val USERS_KEY = stringPreferencesKey("users_json")

    suspend fun updateUsers(count: Int): Result<List<User>> {
        return try {
            val response = apiService.fetchUsers(count)
            if (response.isSuccessful) {
                val users = response.body()?.results ?: emptyList()
                saveUsersToDataStore(users)
                Result.success(users)
            } else {
                Result.failure(Exception("Ошибка сервера: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("UsersRepository", "Ошибка загрузки пользователей:\n" + e.stackTraceToString())
            Result.failure(e)
        }
    }

    fun getUsersFlow(): Flow<List<User>> {
        return dataStore.data.map { preferences ->
            val json = preferences[USERS_KEY]
            if (!json.isNullOrBlank()) {
                try {
                    val type = object : TypeToken<List<User>>() {}.type
                    gson.fromJson<List<User>>(json, type)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }

    private suspend fun saveUsersToDataStore(users: List<User>) {
        val json = gson.toJson(users)
        dataStore.edit { preferences ->
            preferences[USERS_KEY] = json
        }
    }
}

