package com.home.hipstabot.matching

interface Matcher {
    fun getMedia(query: String) : Media?
    fun getMedia(media: Media) : Media?
    fun service() : Media.ServiceType
    fun matchesUri(uri:String) : Boolean
}