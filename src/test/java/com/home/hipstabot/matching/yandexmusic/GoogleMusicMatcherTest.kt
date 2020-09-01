package com.home.hipstabot.matching.yandexmusic

import com.home.hipstabot.matching.Media
import org.junit.Test

class GoogleMusicMatcherTest {
    @Test
    fun test() {
        var media = Media()
        media.setTags(listOf("rap", "god"))
        media.type = Media.ServiceType.NO_SERVICE
        var media1 = YandexMusicMatcher().getMedia(media)
        var i =0;
    }
}
