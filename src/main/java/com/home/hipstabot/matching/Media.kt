package com.home.hipstabot.matching

class Media {

    lateinit var title : String
    lateinit var artist : String
    lateinit var album : String
    lateinit var sourceLink : String
    lateinit var type : ServiceType

    enum class ServiceType {
        APPLE_MUSIC {
            override fun prettyName(): String {
                return "Apple Music"
            }
        },
        GOOGLE_MUSIC {
            override fun prettyName(): String {
                return "Google Music"
            }
        },
        YANDEX_MUSIC {
            override fun prettyName(): String {
                return "Yandex Music"
            }
        };

        abstract fun prettyName() : String
    }


}
