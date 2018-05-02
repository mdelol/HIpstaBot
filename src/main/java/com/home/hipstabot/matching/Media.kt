package com.home.hipstabot.matching

class Media {

    lateinit var title: String
    lateinit var artist: String
    private var displayName: String? = null
    lateinit var link: String
    lateinit var thumbnailUri: String
    lateinit var type: ServiceType
    private var tags: List<String> = ArrayList()

    fun getTags(): List<String> {
        if (tags.isEmpty()) return listOf(title, artist).flatMap { x -> x.split("[^а-яА-Я\\w]".toRegex()) }
        return tags
    }

    fun setTags(tags: List<String>) {
        this.tags = tags
    }

    fun getDisplayName(): String {
        return displayName ?: "${artist} - ${title}"
    }

    fun setDisplayName(displayName: String) {
        this.displayName = displayName
    }

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

        abstract fun prettyName(): String
    }


}
