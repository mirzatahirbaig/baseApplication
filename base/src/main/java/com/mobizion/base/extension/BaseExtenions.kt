/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.extension

import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.mobizion.base.decorator.SpacesItemDecoration
import java.io.File
import java.net.URLConnection
import kotlin.reflect.full.declaredMemberProperties


/**
 * Start New Activity with animation
 *
 * @param activity the destination class name
 */
fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        it.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        this.startActivity(it)
        finish()
    }
}

/**
 * Start New Activity with animation
 * @param activity the destination class name
 * @param startAnimRes start animation Id
 * @param endAnimRes end animation Id
 */

fun <A : Activity> Activity.startNewActivityWithAnimation(activity: Class<A>,@AnimRes startAnimRes: Int, @AnimRes endAnimRes: Int) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(it)
        overridePendingTransition(startAnimRes, endAnimRes)
        this.finish()
    }
}

/**
 * Check whether to show rational dialog to the user or not.
 *
 * @param permission the permission against which the rational has to be shown
 */

fun Activity.showPermissionRational(permission: String):Boolean{
    return ActivityCompat.shouldShowRequestPermissionRationale(this,permission)
}

/**
 * common view extensions
 */

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.invisible(isInvisible: Boolean) {
    visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

fun View.enabled(enabled: Boolean) {
    isEnabled = enabled
}

fun View.alpha(alpha:Float) {
    this.alpha = alpha
}

/**
 * File extensions to check file type
 */

/**
 * Creates a Uri from the given encoded URI string.
 *
 * @see Uri.parse
 */
fun String.toUri(): Uri = Uri.parse(this)

/**
 * Creates a Uri from the given file.
 *
 * @see Uri.fromFile
 */
fun File.toUri(): Uri = Uri.fromFile(this)

/**
 * Creates a [File] from the given [Uri]. Note that this will throw an
 * [IllegalArgumentException] when invoked on a [Uri] that lacks `file` scheme.
 */
fun Uri.toFile(): File {
    require(scheme == "file") { "Uri lacks 'file' scheme: $this" }
    return File(requireNotNull(path) { "Uri path is null: $this" })
}

fun File.isImageFile(): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(path)
    return mimeType.startsWith("image")
}

fun File.isVideoFile(): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(path)
    return mimeType.startsWith("video")
}

fun File.isDocFile(): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(path)
    return mimeType.startsWith("application/pdf")
}

/**
 * Extensions for getting resources
 * @param resource id
 */

fun Context.getStringRes(@StringRes stringId: Int): String {
    return this.resources.getString(stringId)
}

fun Context.getStringArray(@ArrayRes arrayId: Int): Array<String> {
    return this.resources.getStringArray(arrayId)
}

fun Context.getImageDrawable(@DrawableRes drawableId: Int): Drawable {
    return ContextCompat.getDrawable(this,drawableId)!!
}

fun Context.getInteger(@IntegerRes integerId: Int): Int {
    return this.resources.getInteger(integerId)
}

fun Context.getColour(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this,colorId)
}

fun Context.getIntArray(@ArrayRes arrayId: Int):IntArray{
    return resources.getIntArray(arrayId)
}

/**
 * @param permission to check whether app has or not
 * @return true or false base on whether app has permission or not
 */

fun Context.appHasPermission(permission: String): Boolean {
    if (permission == android.Manifest.permission.WRITE_EXTERNAL_STORAGE){
        return ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
    }
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * @param msg to show toast
 */
fun Context.showToast(msg:String){
    Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
}

/**
 * These extensions for setting layout manager to recycler view
 */

fun RecyclerView.setLayoutManager():LinearLayoutManager {
    val manager = LinearLayoutManager(context)
    layoutManager= manager
    return manager
}

fun RecyclerView.setHorizontalLayoutManager() {
    this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
}

fun RecyclerView.setGridManager() {
    this.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    val decoration = SpacesItemDecoration(5)
    this.addItemDecoration(decoration)
}

fun RecyclerView.setGridManager(spanCount:Int) {
    this.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
}

fun RecyclerView.setStackFromEndManager() {
    val manager = LinearLayoutManager(context)
    manager.stackFromEnd = true
    this.layoutManager = manager
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
 * convert int to db
 */

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

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

fun Context.createAlertDialog(bindings:ViewBinding):Dialog{
    return Dialog(this).also { dialog ->
        dialog.setContentView(bindings.root)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}

fun View.showSimpleSnackBar(msg: String){
    Snackbar.make(
        this,
        msg,
        Snackbar.LENGTH_LONG
    ).show()
}

fun View.showSnackBar(msg: String,gravity:Int,backgroundColor:Int){
    val snackBar = Snackbar.make(
        this,
        msg,
        Snackbar.LENGTH_LONG
    )
    val snackBarView = snackBar.view
    val params:FrameLayout.LayoutParams = snackBarView.layoutParams as FrameLayout.LayoutParams
    params.gravity = gravity
    params.height = FrameLayout.LayoutParams.WRAP_CONTENT
    params.width = FrameLayout.LayoutParams.MATCH_PARENT
    snackBarView.layoutParams = params
    snackBarView.setBackgroundColor(backgroundColor)
    snackBar.show()
}

fun View.showErrorSnackBar(msg: String){
    showSnackBar(msg,Gravity.TOP,Color.RED)
}
fun View.showSuccessSnackBar(msg: String){
    showSnackBar(msg,Gravity.TOP,Color.GREEN)
}

fun EditText.isEmpty():Boolean{
    return this.text.toString().trim().isEmpty()
}

fun EditText.text():String{
    return this.text.toString().trim()
}

fun EditText.password():String{
    return this.text.toString()
}