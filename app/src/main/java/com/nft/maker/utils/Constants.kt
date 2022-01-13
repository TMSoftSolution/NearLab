package com.nft.maker.utils

import java.util.regex.Pattern

object Constants {
    // LOCAL DB NAME
    const val DB_NAME = "db_name"

    // BASE URL
    const val BASE_URL_DASHBOARD = "https://nftmaker.algorepublic.com/api/v1/"
    const val BASE_URL = "https://nftmaker.algorepublic.com/" // "https://63ce-162-12-210-90.ngrok.io/"//

//    const val BASE_URL_DASHBOARD = "https://b147-162-12-210-90.ngrok.io/api/v1/"
//    const val BASE_URL = "https://b147-162-12-210-90.ngrok.io/"

    const val NO_INTERNET_CONNECTION = "No Internet Connection"

    const val DISTANCE_IN_METERS = 500

    const val SHORT_ANIMATION_DURATION = 350L


    // PERMISSION
    const val CAMERA_AND_READ_AND_WRITE_PERMISSION = 1111
    const val FINE_AND_COURSE_LOCATION_PERMISSION = 2222

    // FOR ALERT DIALOG LISTENER
    const val CLICK_POSITIVE = 1
    const val CLICK_NEGATIVE = 2
    public fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }


}