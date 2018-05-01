package com.home.hipstabot.service

import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update

interface MessageProcessor {

    fun getResponse(update : Update?) : SendMessage?

}
