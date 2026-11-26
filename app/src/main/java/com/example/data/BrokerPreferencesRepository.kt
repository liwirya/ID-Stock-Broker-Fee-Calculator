package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.BrokerCatalog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.brokerDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "fee_saham_bei_prefs"
)

interface BrokerPreferencesStore {
    val defaultBrokerIdFlow: Flow<String>
    suspend fun saveDefaultBrokerId(brokerId: String)
}

class BrokerPreferencesRepository(
    private val context: Context
) : BrokerPreferencesStore {

    private val defaultBrokerKey = stringPreferencesKey("default_broker_id")

    override val defaultBrokerIdFlow: Flow<String> = context.brokerDataStore.data.map { prefs ->
        prefs[defaultBrokerKey] ?: BrokerCatalog.DEFAULT_BROKER_ID
    }

    override suspend fun saveDefaultBrokerId(brokerId: String) {
        context.brokerDataStore.edit { prefs ->
            prefs[defaultBrokerKey] = brokerId
        }
    }
}
