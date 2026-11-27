package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.BrokerCatalog
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.brokerDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "fee_saham_bei_prefs",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() }
)

interface BrokerPreferencesStore {
    val defaultBrokerIdFlow: Flow<String>
    suspend fun saveDefaultBrokerId(brokerId: String)
}

class BrokerPreferencesRepository(
    private val context: Context
) : BrokerPreferencesStore {

    private val defaultBrokerKey = stringPreferencesKey("default_broker_id")

    override val defaultBrokerIdFlow: Flow<String> = context.brokerDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val storedId = prefs[defaultBrokerKey] ?: BrokerCatalog.DEFAULT_BROKER_ID
            BrokerCatalog.findById(storedId).id
        }

    override suspend fun saveDefaultBrokerId(brokerId: String) {
        context.brokerDataStore.edit { prefs ->
            prefs[defaultBrokerKey] = brokerId
        }
    }
}
