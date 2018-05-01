package com.home.hipstabot.service

import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult

interface QueryResultService {
    fun getQueryResults(query: String) : List<InlineQueryResult>
}