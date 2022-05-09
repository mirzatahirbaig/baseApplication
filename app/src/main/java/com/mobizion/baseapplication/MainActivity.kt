package com.mobizion.baseapplication

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobizion.base.activity.BaseActivity
import com.mobizion.baseapplication.databinding.ActivityMainBinding
import kotlin.reflect.full.declaredMemberProperties

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun initViews() {
    }

    override fun shouldHideKeyboard(): Boolean {
        TODO("Not yet implemented")
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