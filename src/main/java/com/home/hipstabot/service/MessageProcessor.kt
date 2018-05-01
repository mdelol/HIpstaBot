package com.home.hipstabot.service

import org.telegram.telegrambots.api.objects.Update

interface MessageProcessor {

    fun process(update : Update?)

}
