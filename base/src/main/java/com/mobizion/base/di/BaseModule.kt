package com.mobizion.base.di

import com.mobizion.base.view.model.PermissionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {
    viewModel {
        PermissionsViewModel()
    }

}