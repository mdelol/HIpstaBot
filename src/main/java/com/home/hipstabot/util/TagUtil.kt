package com.home.hipstabot.util

class TagUtil {
    companion object {
        fun splitTags(query: String): List<String> {
            return query.split("[^а-яА-Я\\w]".toRegex()).filter(String::isNotBlank)
        }
    }
}