package com.home.hipstabot.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update

@Service
class MessageProcessorImpl : MessageProcessor {
    override fun getResponse(update: Update?) : SendMessage? {
        if (update != null) {
            return SendMessage(update.message.chatId, "message mock")
        }
        return null
    }
}