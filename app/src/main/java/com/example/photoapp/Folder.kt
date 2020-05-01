package com.example.photoapp

import android.os.Parcel
import android.os.Parcelable

data class Folder(var name : String? ,var  path : String? ,var firstImage : String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(path)
        parcel.writeString(firstImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Folder> {
        override fun createFromParcel(parcel: Parcel): Folder {
            return Folder(parcel)
        }

        override fun newArray(size: Int): Array<Folder?> {
            return arrayOfNulls(size)
        }
    }


}