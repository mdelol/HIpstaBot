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

            val media: Media = getMediaFromRequest(query) ?: return buildEmptyResponse()

            val availableServices = matchers.map { x -> Pair(x, x.getLink(media)) }.filter { (_, link) -> link != null }


            val map = availableServices.map { x ->
                val result = InlineQueryResultArticle()
                result.id = x.first.service().prettyName()
                result.title = x.first.service().prettyName()
                result.url = x.second
                val content = InputTextMessageContent()
                content.messageText = x.second
                content.enableWebPagePreview() // todo try this
                content.enableMarkdown(true)
                result.inputMessageContent = content

                result;
            }

            val answerInlineQuery = AnswerInlineQuery()

            answerInlineQuery.results = map;
            answerInlineQuery.inlineQueryId = update.inlineQuery.id
            answerInlineQuery.cacheTime = 1000

            return InlineContainer(answerInlineQuery)
        }
        return SendMessageContainer(SendMessage(update.message.chatId, "message mock"))
    }

    private fun buildEmptyResponse(): Container? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getMediaFromRequest(query: String): Media? {
        val filter = matchers.filter { x -> x.matchesUri(query) }.first()
        val media = filter.getMedia(query)

        return media
    }
}