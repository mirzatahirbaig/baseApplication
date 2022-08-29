package com.mobizion.gallary.repository.implementation

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.mobizion.gallary.enum.MediaType
import com.mobizion.gallary.enum.SortOrder
import com.mobizion.gallary.extensions.convertSecondsToHMmSs
import com.mobizion.gallary.models.GalleryMedia
import com.mobizion.gallary.repository.abstraction.GalleryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 27/08/2022
 */
class GalleryRepoImpl(private val contentResolver: ContentResolver) : GalleryRepository {

    private val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.TITLE,
        MediaStore.Video.VideoColumns.DURATION,
        MediaStore.Video.VideoColumns.DATA,
    )

    private val String.selection: String
        get() = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=$this"

    private val SortOrder.order
        get() = "${MediaStore.Files.FileColumns.DATE_ADDED} $name"

    override fun load(type: MediaType, order: SortOrder): Flow<List<GalleryMedia>> {
        return when (type) {
            MediaType.IMAGES -> {
                flow {
                    emit(
                        loadData(
                            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString().selection,
                            order
                        )
                    )
                }
            }
            MediaType.VIDEOS -> {
                flow {
                    emit(
                        loadData(
                            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString().selection,
                            order
                        )
                    )
                }
            }
            MediaType.IMAGES_AND_VIDEOS -> {
                flow {
                    emit(
                        loadData(
                            (MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString().selection
                                    + " OR "
                                    + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString().selection), order
                        )
                    )
                }
            }
        }
    }

    private suspend fun loadData(selection: String, order: SortOrder): List<GalleryMedia> {
        return withContext(Dispatchers.IO) {
            val media = mutableListOf<GalleryMedia>()
            contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                order.order
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val mediaTypeColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
                while (cursor.moveToNext()) {
                    val id = cursor.getString(idColumn)
                    val mediaType = cursor.getInt(mediaTypeColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Files.getContentUri("external"),
                        id.toLong()
                    )
                    var duration: String? = null
                    if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                        duration =
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))
                                .convertSecondsToHMmSs()
                    }
                    media.add(GalleryMedia(id, mediaType, contentUri, duration))
                }
            }
            media
        }
    }
}