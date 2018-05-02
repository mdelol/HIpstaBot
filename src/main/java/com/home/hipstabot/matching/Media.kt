package com.home.hipstabot.matching

class Media {

    lateinit var title : String
    lateinit var artist : String
    lateinit var album : String
    lateinit var link : String
    lateinit var thumbnailUri : String
    lateinit var type : ServiceType
    var tags : List<String> = ArrayList()

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
        },
        NO_SERVICE {
            override fun prettyName(): String {
                return "No service"
            }
        };

        abstract fun prettyName() : String
    }


}
