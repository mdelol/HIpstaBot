package com.home.hipstabot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.generics.BotSession
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Configuration
open class TelegramConfig {

    @Bean
    open fun botSession(): BotSession {
        return DefaultBotSession()
    }


}