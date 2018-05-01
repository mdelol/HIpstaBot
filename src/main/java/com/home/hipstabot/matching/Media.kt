package com.home.hipstabot.matching

class Media {

    lateinit var title : String
    lateinit var artist : String
    lateinit var album : String
    lateinit var sourceLink : String
    lateinit var type : ServiceType

    enum class ServiceType {
        APPLE_MUSIC,
        GOOGLE_MUSIC,
        YANDEX_MUSIC
    }


}
