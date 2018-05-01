package com.home.hipstabot.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.generics.BotOptions
import org.telegram.telegrambots.generics.LongPollingBot

@Service
class PollingBotService : LongPollingBot {

    @Autowired
    private lateinit var messageProcessor : MessageProcessor

    @Value("\${telegram.api.key}")
    private lateinit var tokenValue: String


    override fun getBotToken(): String {
        return tokenValue.orEmpty()
    }

    override fun onUpdateReceived(update: Update?) {
        messageProcessor.process(update)
    }

    override fun getBotUsername(): String {
        return "hipstaBot"
    }

    override fun getOptions(): BotOptions {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearWebhook() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}