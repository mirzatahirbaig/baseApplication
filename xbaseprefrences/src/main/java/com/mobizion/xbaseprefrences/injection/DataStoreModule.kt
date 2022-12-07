package com.mobizion.xbaseprefrences.injection

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.mobizion.xbaseprefrences.abstraction.DataStoreRepository
import com.mobizion.xbaseprefrences.implementation.DataStoreRepoImpl
import com.mobizion.xbaseprefrences.view.model.DataStoreViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 12/09/2022
 */

val datastoreModule = module {

    single<DataStoreRepository> {
        DataStoreRepoImpl(androidContext())
    }

    viewModel {
        DataStoreViewModel(get())
    }
}

