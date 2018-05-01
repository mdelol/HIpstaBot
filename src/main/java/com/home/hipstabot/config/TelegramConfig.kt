package com.home.hipstabot.config

import com.home.hipstabot.service.PollingBotService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.TelegramBotsApi

@Configuration
open class TelegramConfig {

    @Bean
    open fun botsApi(botService: PollingBotService): TelegramBotsApi {
        val telegramBotsApi = TelegramBotsApi()
        telegramBotsApi.registerBot(botService)
        return telegramBotsApi
    }

}