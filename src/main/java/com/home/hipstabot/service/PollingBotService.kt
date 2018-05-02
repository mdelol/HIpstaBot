package com.home.hipstabot.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot

@Service
class PollingBotService(botOptions: DefaultBotOptions) : TelegramLongPollingBot(botOptions) {

    @Autowired
    private lateinit var messageProcessor : MessageProcessor

    @Value("\${telegram.api.key}")
    private lateinit var tokenValue: String


    override fun getBotToken(): String {
        return tokenValue
    }

    override fun onUpdateReceived(update: Update?) {
        var millis = System.currentTimeMillis()
        var response = messageProcessor.getResponse(update) ?: return

        when (response) {
            is InlineContainer -> {
                sendApiMethod(response.aIC)
            }
            is SendMessageContainer -> {
                sendApiMethod(response.sM)
            }
        }
        println("spent - ${System.currentTimeMillis() - millis}")
    }

    override fun getBotUsername(): String {
        return "hipstabot"
    }

    override fun clearWebhook() {

    }
}