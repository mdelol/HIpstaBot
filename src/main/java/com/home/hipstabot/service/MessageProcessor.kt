package com.home.hipstabot.service

import org.telegram.telegrambots.api.methods.AnswerInlineQuery
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update

interface MessageProcessor {

    fun getResponse(update : Update?) : Container?

}

abstract class Container

data class InlineContainer(var aIC: AnswerInlineQuery) : Container()

data class SendMessageContainer(var sM: SendMessage): Container()
