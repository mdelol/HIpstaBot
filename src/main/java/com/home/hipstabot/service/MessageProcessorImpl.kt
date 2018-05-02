package com.home.hipstabot.service

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.AnswerInlineQuery
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle

@Service
class MessageProcessorImpl : MessageProcessor {

    @Autowired
    private lateinit var matchers: List<Matcher>

    override fun getResponse(update: Update?): Container? {
        if (update == null) return null

        if (update.hasInlineQuery()) {
            val query = update.inlineQuery.query

            val media: Media = getMediaFromRequest(query) ?: return buildEmptyResponse(update)

            val availableServices = matchers.map { x -> Pair(x, x.getLink(media)) }.filter { (_, link) -> link != null }

            if(availableServices.isEmpty()) return buildEmptyResponse(update)

            val articles = availableServices.map { x -> buildArticleFromResult(x) }

            return buildInlineContainer(articles, update)
        }

        return SendMessageContainer(SendMessage(update.message.chatId, "message mock"))
    }

    private fun buildInlineContainer(articles: List<InlineQueryResultArticle>, update: Update): InlineContainer {
        val answerInlineQuery = AnswerInlineQuery()

        answerInlineQuery.results = articles
        answerInlineQuery.inlineQueryId = update.inlineQuery.id
        answerInlineQuery.cacheTime = 1000

        return InlineContainer(answerInlineQuery)
    }

    private fun buildArticleFromResult(x: Pair<Matcher, String?>): InlineQueryResultArticle {
        val result = InlineQueryResultArticle()

        result.id = x.first.service().prettyName()
        result.title = x.first.service().prettyName()
        result.url = x.second
        result.thumbUrl = x.second

        val content = InputTextMessageContent()
        content.messageText = x.second
        content.enableWebPagePreview()
        content.enableMarkdown(true)

        result.inputMessageContent = content

        return result
    }

    private fun buildEmptyResponse(update: Update): Container? {
        val answerInlineQuery = AnswerInlineQuery()

        answerInlineQuery.results = ArrayList()
        answerInlineQuery.inlineQueryId = update.inlineQuery.id
        answerInlineQuery.cacheTime = 1000

        return InlineContainer(answerInlineQuery)
    }

    private fun getMediaFromRequest(query: String): Media? {
        val filter = matchers.filter { x -> x.matchesUri(query) }.firstOrNull()?: return null
        val media = filter.getMedia(query)

        return media
    }
}