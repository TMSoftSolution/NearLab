package com.nft.maker.utils

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import androidx.annotation.RequiresPermission
import com.nft.maker.model.mobile_contact_model.MobileContactList


@RequiresPermission(Manifest.permission.READ_CONTACTS)
fun Context.isContactExists(
    phoneNumber: String
): Boolean {
    val lookupUri = Uri.withAppendedPath(
        ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
        Uri.encode(phoneNumber)
    )
    val projection = arrayOf(
        ContactsContract.PhoneLookup._ID,
        ContactsContract.PhoneLookup.NUMBER,
        ContactsContract.PhoneLookup.DISPLAY_NAME
    )
    contentResolver.query(lookupUri, projection, null, null, null).use {
        return (it?.moveToFirst() == true)
    }
}


@JvmOverloads
fun Context.retrieveAllContacts(
    searchPattern: String = "",
    retrieveAvatar: Boolean = true,
    limit: Int = 30,
    offset: Int = 0
): List<MobileContactList> { //List<ContactData>
    val result: MutableList<MobileContactList> = mutableListOf()
//    val result: MutableList<ContactData> = mutableListOf()
    contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        CONTACT_PROJECTION,
        if (searchPattern.isNotBlank()) "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?" else null,
        if (searchPattern.isNotBlank()) arrayOf("%$searchPattern%") else null,
        if (limit > 0 && offset > -1) "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC LIMIT $limit OFFSET $offset"
        else ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
    )?.use {
        if (it.moveToFirst()) {
            do {
                val contactId = it.getLong(it.getColumnIndex(CONTACT_PROJECTION[0]))
                val name = it.getString(it.getColumnIndex(CONTACT_PROJECTION[2])) ?: ""
                val hasPhoneNumber = it.getString(it.getColumnIndex(CONTACT_PROJECTION[3])).toInt()
//                val email = it.getString(it.getColumnIndex(CONTACT_PROJECTION[4])) ?: ""
                val phoneNumber: List<String> = if (hasPhoneNumber > 0) {
                    retrievePhoneNumber(contactId)
                } else mutableListOf()

                val avatar = if (retrieveAvatar) retrieveAvatar(contactId) else null

                var imageBase64 : Bitmap? = null
                avatar?.let { uri ->
                    val imageStream = contentResolver.openInputStream(uri);
                    imageBase64 = BitmapFactory.decodeStream(imageStream);

//                    imageBase64 = UtilsFunction.compressBitmap(selectedImage)
                }
                val email = retrieveEmail(contactId)

                val phoneNum = if(phoneNumber.isNotEmpty()) phoneNumber[0] else ""

                result.add(MobileContactList(contactId.toString(), name, phoneNum, email ?: "" ,imageBase64 , false , false ))
            } while (it.moveToNext())
        }
    }
    return result
}



@JvmOverloads
fun Context.searchContactByName(
    searchPattern: String = "",
    retrieveAvatar: Boolean = true,
    limit: Int = 50,
    offset: Int = 0
): List<MobileContactList> {
    val result: MutableList<MobileContactList> = mutableListOf()
    contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        CONTACT_PROJECTION,
        if (searchPattern.isNotBlank()) "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?" else null,
        if (searchPattern.isNotBlank()) arrayOf("%$searchPattern%") else null,
        if (limit > 0 && offset > -1) "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC LIMIT $limit OFFSET $offset"
        else ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
    )?.use {
        if (it.moveToFirst()) {
            do {
                val contactId = it.getLong(it.getColumnIndex(CONTACT_PROJECTION[0]))
                val name = it.getString(it.getColumnIndex(CONTACT_PROJECTION[2])) ?: ""
                val hasPhoneNumber = it.getString(it.getColumnIndex(CONTACT_PROJECTION[3])).toInt()
//                val email = it.getString(it.getColumnIndex(CONTACT_PROJECTION[4])) ?: ""
                val phoneNumber: List<String> = if (hasPhoneNumber > 0) {
                    retrievePhoneNumber(contactId)
                } else mutableListOf()

                val avatar = if (retrieveAvatar) retrieveAvatar(contactId) else null

                var imageBase64 : Bitmap? = null
                avatar?.let { uri ->
                    val imageStream = contentResolver.openInputStream(uri);
                    imageBase64 = BitmapFactory.decodeStream(imageStream);

//                    imageBase64 = UtilsFunction.compressBitmap(selectedImage)
                }
                val email = retrieveEmail(contactId)

                val phoneNum = if(phoneNumber.isNotEmpty()) phoneNumber[0] else ""

                result.add(MobileContactList(contactId.toString(), name, phoneNum, email ?: "" ,imageBase64 , false , false ))
            } while (it.moveToNext())
        }
    }
    return result
}

private fun Context.retrievePhoneNumber(contactId: Long): List<String> {
    val result: MutableList<String> = mutableListOf()
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} =?",
        arrayOf(contactId.toString()),
        null
    )?.use {
        if (it.moveToFirst()) {
            do {
                result.add(it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
            } while (it.moveToNext())
        }
    }
    return result
}

private fun Context.retrieveAvatar(contactId: Long): Uri? {
    return contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        null,
        "${ContactsContract.Data.CONTACT_ID} =? AND ${ContactsContract.Data.MIMETYPE} = '${ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE}'",
        arrayOf(contactId.toString()),
        null
    )?.use {
        if (it.moveToFirst()) {
            val contactUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI,
                contactId
            )
            Uri.withAppendedPath(
                contactUri,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
            )
        } else null
    }
}


private fun Context.retrieveEmail(contactId: Long): String? {

    return contentResolver.query(
        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
        ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
        arrayOf(contactId.toString()), null)?.use {
        if (it.moveToFirst()) {
            it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA) )
        } else null
    }
}

private val CONTACT_PROJECTION = arrayOf(
    ContactsContract.Contacts._ID,
    ContactsContract.Contacts.LOOKUP_KEY,
    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
    ContactsContract.Contacts.HAS_PHONE_NUMBER
)

data class ContactData(
    val contactId: Long,
    val name: String,
    val phone_no: List<String>,
    val pic: String?,
    val email : String?
)