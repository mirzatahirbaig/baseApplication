package com.mobizion.gallary

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 11/10/2022
 */

data class DeviceMedia(
    var media_id: String,
    var type:MessageType,
    val contentUri: Uri,
    val path: String,
    val duration:String?,
    var isSelected:Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readSerializable() as? MessageType ?: MessageType.IMAGE,
        parcel.readParcelable(Uri::class.java.classLoader)?: Uri.EMPTY,
        parcel.readString()?:"",
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(media_id)
        parcel.writeSerializable(type)
        parcel.writeParcelable(contentUri, flags)
        parcel.writeString(path)
        parcel.writeString(duration)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeviceMedia> {
        override fun createFromParcel(parcel: Parcel): DeviceMedia {
            return DeviceMedia(parcel)
        }

        override fun newArray(size: Int): Array<DeviceMedia?> {
            return arrayOfNulls(size)
        }
    }
}