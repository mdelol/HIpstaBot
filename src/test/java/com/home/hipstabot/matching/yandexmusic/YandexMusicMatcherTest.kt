package com.home.hipstabot.matching.yandexmusic

import org.junit.Test

class YandexMusicMatcherTest {
    @Test
    open fun test() {
        val media = YandexMusicMatcher().getMedia("https://music.yandex.ru/album/81430/track/732397")
        println(media.toString())
    }
}