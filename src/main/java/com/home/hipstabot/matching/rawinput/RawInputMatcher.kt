package com.home.hipstabot.matching.rawinput

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import com.home.hipstabot.util.TagUtil
import org.springframework.stereotype.Service

@Service
class RawInputMatcher: Matcher {
    override fun getMedia(query: String): Media? {
        if(query.isBlank()) return null
        val parts = TagUtil.splitTags(query)
        val media = Media()
        media.setTags(parts)
        media.type = Media.ServiceType.NO_SERVICE
        return media
    }

    override fun getMedia(media: Media): Media? {
        return null
    }

    override fun service(): Media.ServiceType {
        return Media.ServiceType.NO_SERVICE
    }

    override fun matchesUri(uri: String): Boolean {
        return !(uri.contains("itunes.apple.com") ||
                uri.contains("music.google.com") ||
                uri.contains("music.yandex.ru"))
    }
}