package com.mgdev.psqlmsgq

import org.springframework.integration.annotation.Gateway
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.annotation.Transformer
import org.springframework.messaging.Message
import org.springframework.stereotype.Component

@Component
class PostgresqlMsgExample{

    @MessagingGateway
    interface MessageGateway {

        @Gateway(requestChannel = "message")
        fun publish(msg: MyMessage)
    }

    @Transformer(inputChannel = "message", outputChannel = "messageProcessor")
    fun validateMessage(message: Message<MyMessage>) = message.payload

    @ServiceActivator(inputChannel = "messageProcessor")
    fun processMessage(myMessage: MyMessage) {
        println("=========================")
        println(myMessage)
        println("=========================")
    }
}

data class MyMessage(
    val id: String,
    val msg: String
)