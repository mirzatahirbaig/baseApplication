package com.mobizion.baseapplication

import android.content.ContentResolver
import android.content.Intent.getIntent
import android.database.ContentObserver
import android.net.Uri
import android.view.View.inflate
import com.mobizion.baseapplication.databinding.ActivityMainBinding
import com.mobizion.camera.XCameraActivity
import com.mobizion.gallary.enum.MediaType
import com.mobizion.gallary.enum.SortOrder
import com.mobizion.gallary.view.model.GalleryViewModel
import com.mobizion.xbase.activity.XBaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


class MainActivity : XBaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    val vm: GalleryViewModel by viewModel()

    override fun shouldHideKeyboard() = false

    override fun initViews() {
        loadKoinModules(listOf(repos, vms))
        binding.openCamera.setOnClickListener {
            launchActivityForResult.launch(getIntent(XCameraActivity::class.java, 1))
        }

        permissionViewModel.activityResult.observe {
            it.data?.let { intent ->
                if (intent.getIntExtra("REQUEST_CODE", 0) == 1) {
                    // Means that the results are from camera
                    val ss = it.data?.data
                }
            }
        }
    }

    /**
     * we can load data of (images,videos,images and videos) with sort by date (is ascending or descending order)
     */

    private fun loadMediaData() {

        vm.load(MediaType.IMAGES, SortOrder.DESC)
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

//inline fun <reified T : Any> T.asMap(): HashMap<String, Any?> {
//    val props = T::class.declaredMemberProperties.associateBy { it.name }
//    return props.keys.associateWith { props[it]?.get(this) } as HashMap<String, Any?>
//}


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