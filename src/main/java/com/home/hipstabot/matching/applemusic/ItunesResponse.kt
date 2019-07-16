package com.home.hipstabot.matching.applemusic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class ItunesResponse {
    var resultCount : Int = 0
    lateinit var results : List<ItunesEntity>
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ItunesEntity {
    lateinit var trackViewUrl : String
    lateinit var artworkUrl60: String
    lateinit var trackName: String
    lateinit var artistName: String
}
