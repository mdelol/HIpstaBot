package com.home.hipstabot.service

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.AnswerInlineQuery
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle
import java.util.stream.Collectors

@Service
class MessageProcessorImpl : MessageProcessor {

    @Autowired
    private lateinit var matchers: List<Matcher>

    @Value("\${telegram.api.cache.time}")
    private var cacheTime: Int = 1000

    override fun getResponse(update: Update?): Container? {
        if (update == null) return null

        if (update.hasInlineQuery()) {
            val query = update.inlineQuery.query

            val media: Media = getMediaFromRequest(query) ?: return buildEmptyResponse(update)

            val availableServices: List<Media> = matchers
                    .parallelStream()
                    .map { x -> x.getMedia(media) }
                    .filter { x -> x != null }.collect(Collectors.toList()).requireNoNulls()

            if (availableServices.isEmpty()) return buildEmptyResponse(update)

            val articles = availableServices
                    .filter { entry -> entry.type != Media.ServiceType.NO_SERVICE }
                    .map { x -> buildArticleFromResult(x) }

            return buildInlineContainer(articles, update)
        }

        return SendMessageContainer(SendMessage(update.message.chatId, "message mock"))
    }

    private fun buildInlineContainer(articles: List<InlineQueryResultArticle>, update: Update): InlineContainer {
        val answerInlineQuery = AnswerInlineQuery()

        answerInlineQuery.results = articles
        answerInlineQuery.inlineQueryId = update.inlineQuery.id
        answerInlineQuery.cacheTime = cacheTime

        return InlineContainer(answerInlineQuery)
    }

    private fun buildArticleFromResult(x: Media): InlineQueryResultArticle {
        val result = InlineQueryResultArticle()

        result.id = x.type.prettyName()
        result.title = x.type.prettyName()
        result.url = x.link
        result.description = x.getDisplayName()
        result.thumbUrl = x.thumbnailUri
        result.thumbHeight = 60
        result.thumbWidth = 60

        val content = InputTextMessageContent()
        content.messageText = x.link
        content.enableWebPagePreview()
        content.enableMarkdown(true)

        result.inputMessageContent = content

        return result
    }

    private fun buildEmptyResponse(update: Update): Container? {
        val answerInlineQuery = AnswerInlineQuery()

        answerInlineQuery.results = ArrayList()
        answerInlineQuery.inlineQueryId = update.inlineQuery.id
        answerInlineQuery.cacheTime = cacheTime

        return InlineContainer(answerInlineQuery)
    }

    private fun getMediaFromRequest(query: String): Media? {
        val filter = matchers.firstOrNull { x -> x.matchesUri(query) } ?: return null

        return filter.getMedia(query)
    }
}