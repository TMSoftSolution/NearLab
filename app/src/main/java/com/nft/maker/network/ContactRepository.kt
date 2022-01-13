package com.nft.maker.network

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.provider.ContactsContract
import android.text.TextUtils

import com.nft.maker.model.mobile_contact_model.MobileContactList

import java.io.IOException
import java.io.InputStream

class ContactRepository(val context: Context) {
    lateinit var contactArrayList: ArrayList<MobileContactList>
    lateinit var contactArrayListTwo: ArrayList<MobileContactList>

    @SuppressLint("Range")
    suspend fun fetchContacts(): ArrayList<MobileContactList> {
        contactArrayList = arrayListOf<MobileContactList>()
        val contentResolver: ContentResolver = context?.contentResolver!!
        var contactId: String? = null
        var displayName: String? = null
        var phoneNumber: String = ""
        var email: String = ""
        val cursor: Cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )!!
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                val hasPhoneNumber: Int =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        .toInt()
                    if (hasPhoneNumber > 0) {
                        contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        displayName =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val phoneCursor: Cursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf<String?>(contactId),
                            null
                        )!!
                        if (phoneCursor.moveToNext()) {
                            phoneNumber =
                                phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
//                        val emailCur = contentResolver.query(
//                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
//                            arrayOf<String?>(contactId),
//                            null
//                        )!!
//                        while (emailCur.moveToNext()) {
//                             email = emailCur.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//
//                        }


                        var imageUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))



                        var photo: Bitmap? = null

                        try {
                            val inputStream: InputStream? = ContactsContract.Contacts.openContactPhotoInputStream(
                                    context!!.contentResolver,
                                    ContentUris.withAppendedId(
                                        ContactsContract.Contacts.CONTENT_URI,
                                        contactId.toLong()
                                    )
                                )

                            if (inputStream != null) {
                                photo = BitmapFactory.decodeStream(inputStream)
                            }
                            if (inputStream != null)
                                inputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        phoneCursor.close()
                        contactArrayList.add(
                            MobileContactList(
                                contactId,
                                displayName,
                                phoneNumber,
                                email,
                                photo,
                                false,
                                false
                            )
                        )
                    }


                }
            }

            cursor.close()
            contactArrayListTwo = getHeaderListLatter(contactArrayList)
            return contactArrayListTwo
        }

        fun getHeaderListLatter(usersList: ArrayList<MobileContactList>): ArrayList<MobileContactList> {
            contactArrayListTwo = arrayListOf<MobileContactList>()

            usersList.sortBy { name -> name.contactName }
            var lastHeader: String? = ""
            val size = usersList.size
            for (i in 0 until size) {
                val user: MobileContactList = usersList[i]
                val header: String = user.contactName.first().uppercase()
                if (!TextUtils.equals(lastHeader, header)) {
                    lastHeader = header
                    contactArrayListTwo.add(
                        MobileContactList(
                            user.contactId,
                            header,
                            user.contactNumber,
                            user.email,
                            user.contactImage,
                            true, false
                        )
                    )
                }
                contactArrayListTwo.add(user)
            }
            return contactArrayListTwo
        }

    }