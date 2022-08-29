package com.mobizion.xbase.app

import android.app.Application
import com.mobizion.xbase.injection.dependencyModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */

class XBaseApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@XBaseApplication)
            modules(dependencyModules)
        }
    }

}