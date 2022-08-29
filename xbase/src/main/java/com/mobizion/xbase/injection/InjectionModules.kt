package com.mobizion.xbase.injection

import com.mobizion.xbase.view.model.PermissionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

val permissionModule = module {
    viewModel {
        PermissionViewModel()
    }
}

val dependencyModules = mutableListOf(
    permissionModule
)