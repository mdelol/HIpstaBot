package com.home.hipstabot.matching

import com.home.hipstabot.util.TagUtil

class Media {

    lateinit var title: String
    lateinit var artist: String
    private var displayName: String? = null
    lateinit var link: String
    lateinit var thumbnailUri: String
    lateinit var type: ServiceType
    private var tags: List<String> = ArrayList()

    fun getTags(): List<String> {
        if (tags.isEmpty()) return TagUtil.splitTags(getDisplayName())
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
        SPOTIFY_MUSIC {
            override fun prettyName(): String {
                return "Spotify Music"
            }
        },
        NO_SERVICE {
            override fun prettyName(): String {
                return "No service"
            }
        };

        abstract fun prettyName(): String
    }

    override fun toString(): String {
        return getTags().joinToString("_")
    }
}
