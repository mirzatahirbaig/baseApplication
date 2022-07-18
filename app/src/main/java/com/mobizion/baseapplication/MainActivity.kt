package com.mobizion.baseapplication

import android.content.ContentResolver
import android.database.ContentObserver
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import com.mobizion.base.activity.BaseActivity
import com.mobizion.base.enums.GradientTypes
import com.mobizion.base.extension.dp
import com.mobizion.base.utils.getGradientDrawable
import com.mobizion.baseapplication.databinding.ActivityMainBinding
import kotlin.reflect.full.declaredMemberProperties


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun shouldHideKeyboard() = false

    override fun initViews() {
//        val mainDrawable = com.mobizion.base.utils.getDrawable(
//            Color.WHITE,
//            topLeft = 20,
//            topRight = 20,
//            bottomRight = 20,
//            bottomLeft = 20,
//        )
        binding.txt.background = getGradientDrawable(
            intArrayOf(
                Color.WHITE,
                Color.BLACK
            ),
            gradientType = GradientTypes.RADIAL_GRADIENT
        )
//        val radii = floatArrayOf((20).dp.toFloat(), (20).dp.toFloat(), (20).dp.toFloat(), (20).dp.toFloat(), (20).dp.toFloat(), (20).dp.toFloat(), (20).dp.toFloat(), (20).dp.toFloat())
//        val ss = Shadow(1, 100, "#FFBB86FC", GradientDrawable.RECTANGLE, radii, Shadow.Position.CENTER).getShadow()
//        val ld = LayerDrawable(arrayOf(ss, mainDrawable)) // inset the shadow so it doesn't start right at the left/top
//        binding.txt.background = ld
    }


}


fun HashMap<String,Any?>.removeNullValues(): HashMap<String, Any?> {
    val result = hashMapOf<String, Any?>()
    for ((key, value) in this) {
        if (value != null) {
            if (value is String){
                if (value.isNotEmpty()){
                    result[key] = value
                }
            }
            else{
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