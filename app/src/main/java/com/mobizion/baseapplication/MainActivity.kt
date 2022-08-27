package com.mobizion.baseapplication

import android.content.ContentResolver
import android.content.Intent
import android.database.ContentObserver
import android.graphics.Color
import android.net.Uri
import com.mobizion.base.activity.BaseActivity
import com.mobizion.base.enums.GradientTypes
import com.mobizion.base.extension.appHasPermission
import com.mobizion.base.utils.getGradientDrawable
import com.mobizion.base.view.model.PermissionsViewModel
import com.mobizion.baseapplication.databinding.ActivityMainBinding
import com.mobizion.camera.XCameraActivity
import com.mobizion.gallary.enum.MediaType
import com.mobizion.gallary.enum.SortOrder
import com.mobizion.gallary.view.model.GalleryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.full.declaredMemberProperties


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    val vm:GalleryViewModel by viewModel()
    val permissionVm:PermissionsViewModel by viewModel()

    override fun shouldHideKeyboard() = false

    override fun initViews() {
        launchStoragePermission()
        launchCameraPermission()
        permissionVm.storagePermissionStatus.observe {
            if (it){
                vm.load(MediaType.IMAGES).observe {
                    val ss = it
                }
            }
        }
        binding.txt.background = getGradientDrawable(
            intArrayOf(
                Color.WHITE,
                Color.BLACK
            ),
            gradientType = GradientTypes.RADIAL_GRADIENT
        )
        binding.openCamera.setOnClickListener {
            permissionVm.cameraPermissionStatus.observe {
                if (it){
                    startActivity(Intent(this@MainActivity, XCameraActivity::class.java))
                }
            }

        }
    }

    /**
     * we can load data of (images,videos,images and videos) with sort by date (is ascending or descending order)
     */

    private fun loadMediaData(){

        vm.load(MediaType.IMAGES,SortOrder.DESC)
    }

    override fun shouldHideStatusBar() = false

}


fun HashMap<String, Any?>.removeNullValues(): HashMap<String, Any?> {
    val result = hashMapOf<String, Any?>()
    for ((key, value) in this) {
        if (value != null) {
            if (value is String) {
                if (value.isNotEmpty()) {
                    result[key] = value
                }
            } else {
                result[key] = value
            }
        }
    }
    return result
}

/**
 *
 * Convert any object to map
 *
 */

inline fun <reified T : Any> T.asMap(): HashMap<String, Any?> {
    val props = T::class.declaredMemberProperties.associateBy { it.name }
    return props.keys.associateWith { props[it]?.get(this) } as HashMap<String, Any?>
}


/**
 * Convenience extension method to register a [ContentObserver] given a lambda.
 */
fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}