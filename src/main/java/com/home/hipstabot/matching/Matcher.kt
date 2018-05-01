package com.home.hipstabot.matching

interface Matcher {
    fun getMedia(query: String) : Media
    fun getLink(media: Media) : String
    fun service() : Media.ServiceType

}