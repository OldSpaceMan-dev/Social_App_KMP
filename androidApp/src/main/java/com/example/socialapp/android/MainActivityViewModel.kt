package com.example.socialapp.android

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import com.example.socialapp.android.common.datastore.UserSettings
import com.example.socialapp.android.common.datastore.toAuthResultData
import kotlinx.coroutines.flow.map

class MainActivityViewModel(
    dataStore: DataStore<UserSettings>
): ViewModel() {
    //return flow string which will be our user token
    val authState = dataStore.data.map { it.toAuthResultData().token }

}