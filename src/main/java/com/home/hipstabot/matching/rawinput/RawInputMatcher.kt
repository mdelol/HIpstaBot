package com.home.hipstabot.matching.rawinput

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class RawInputMatcher: Matcher {
    override fun getMedia(query: String): Media? {
        val parts = query.split(Pattern.compile("[^а-яА-Я\\w]"))
        val media = Media()
        media.tags = parts
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