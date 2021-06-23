package com.home.hipstabot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.telegram.telegrambots.ApiContextInitializer
import java.lang.RuntimeException


@SpringBootApplication
@PropertySource("classpath:application.properties")
open class Application {

    @Autowired
    private val env: Environment? = null

    @Bean
    open fun init() = CommandLineRunner {
        val apiKey = env?.getProperty("telegram.api.key")
        val clientId = env?.getProperty("spotify.clientId")
        val clientSecret = env?.getProperty("spotify.clientSecret")

        if (apiKey.isNullOrEmpty()) throw RuntimeException("no telegram api key")
        if (clientId.isNullOrEmpty()) throw RuntimeException("no spotify client id")
        if (clientSecret.isNullOrEmpty()) throw RuntimeException("no spotify client secret")
    }
}

fun main(args : Array<String>) {
    ApiContextInitializer.init()
    SpringApplication.run(Application::class.java, *args)
}
