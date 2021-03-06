package com.home.hipstabot.matching.yandexmusic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class YandexMusicResponse {

}

@JsonIgnoreProperties(ignoreUnknown = true)
class YandexTrack {
    lateinit var name : String
    lateinit var inAlbum: YandexAlbum
}

@JsonIgnoreProperties(ignoreUnknown = true)
class YandexAlbum {
    lateinit var byArtist: YandexArtist
    lateinit var image : String
}

@JsonIgnoreProperties(ignoreUnknown = true)
class YandexArtist {
    lateinit var name: String
}