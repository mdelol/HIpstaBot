package com.home.hipstabot.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.AnswerInlineQuery
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle

@Service
class MessageProcessorImpl : MessageProcessor {
    override fun getResponse(update: Update?) : Container? {
        if (update == null ) return null

        if (update.hasInlineQuery()) {
            val query = update.inlineQuery.query
            val content = InputTextMessageContent()
            content.messageText = query
            content.disableWebPagePreview()
            content.enableMarkdown(true)

            val answerInlineQuery = AnswerInlineQuery()
            val gmres = InlineQueryResultArticle()
            gmres.id = "gm"
            gmres.title = "gm"
            gmres.url = "https://music.google.com"
            gmres.inputMessageContent = content
            val amres = InlineQueryResultArticle()
            amres.id = "am"
            amres.title = "am"
            amres.url = "https://www.apple.com/ru/music/"
            amres.inputMessageContent = content
            val ymres = InlineQueryResultArticle()
            ymres.id = "ym"
            ymres.title = "ym"
            ymres.url = "https://music.yandex.ru"
            ymres.inputMessageContent = content
            answerInlineQuery.results = ArrayList()
            answerInlineQuery.results.add(gmres)
            answerInlineQuery.results.add(amres)
            answerInlineQuery.results.add(ymres)
            answerInlineQuery.inlineQueryId = update.inlineQuery.id
            answerInlineQuery.cacheTime = 86400
            return InlineContainer(answerInlineQuery)
        }
        return SendMessageContainer(SendMessage(update.message.chatId, "message mock"))
    }
}