package com.example.myapplication.data.login

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login_prefs")

class LoginData(private val context: Context) {

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    // 토큰 저장
    suspend fun saveToken(token: String?) {

        //Preferences는 KEY-VALUE 구조를 가진다.
        context.dataStore.edit { data ->
            if(token != null){
                data[ACCESS_TOKEN] = token
                data[IS_LOGGED_IN] = true
            }

        }
    }

    //Flow ?: DataStore가 Flow를 반환한다
    val accessToken: Flow<String?> = context.dataStore.data.map { data ->
        data[ACCESS_TOKEN]
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { data ->
        data[IS_LOGGED_IN] ?: false
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(IS_LOGGED_IN)
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { data ->
            data.clear()
        }
    }
}