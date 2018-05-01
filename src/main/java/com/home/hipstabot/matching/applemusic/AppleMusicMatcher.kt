package com.home.hipstabot.matching.applemusic

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media

class AppleMusicMatcher : Matcher {
    override fun getMedia(query: String): Media {
        var media = Media()

        return media
    }

    override fun getLink(media: Media): String {
        if (media.type == service()) return media.sourceLink
        return ""
    }

    override fun service(): Media.ServiceType {
        return Media.ServiceType.APPLE_MUSIC
    }
}