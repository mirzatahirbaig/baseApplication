package com.mobizion.hiddlegallery.module

import com.mobizion.gallary.repository.abstraction.GalleryRepository
import com.mobizion.gallary.repository.implementation.GalleryRepoImpl
import com.mobizion.hiddlegallery.view.model.BitmapViewModel
import com.mobizion.hiddlegallery.view.model.GalleryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

val galleryModule = module {
    single<GalleryRepository> {
        GalleryRepoImpl(androidContext().contentResolver)
    }

    viewModel {
        GalleryViewModel(get())
    }
    viewModel{
        BitmapViewModel(get())
    }
}