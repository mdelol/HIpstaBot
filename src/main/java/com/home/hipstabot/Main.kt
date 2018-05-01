package com.home.hipstabot

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment


@SpringBootApplication
@PropertySource("classpath:application.properties")
open class Application {

    @Autowired
    private val env: Environment? = null


    @Bean
    open fun init() = CommandLineRunner {
        var property = env?.getProperty("telegram.api.key")
        print(property)
    }
}

fun main(args : Array<String>) {
    var run = SpringApplication.run(Application::class.java, *args)
}