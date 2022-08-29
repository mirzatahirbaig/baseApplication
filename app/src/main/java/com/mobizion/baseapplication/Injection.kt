package com.mobizion.baseapplication

import com.mobizion.camera.CameraViewModel
import com.mobizion.gallary.repository.abstraction.GalleryRepository
import com.mobizion.gallary.repository.implementation.GalleryRepoImpl
import com.mobizion.gallary.view.model.GalleryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 27/08/2022
 */

val repos = module {
    single<GalleryRepository> {
        GalleryRepoImpl(androidApplication().contentResolver)
    }
}

val vms = module {
    viewModel {
        GalleryViewModel(get())
        CameraViewModel()
    }
}

