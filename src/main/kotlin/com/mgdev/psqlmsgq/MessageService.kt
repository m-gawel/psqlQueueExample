package com.mgdev.psqlmsgq

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class MessageService{

    @Autowired
    lateinit var messageGateway: PostgresqlMsgExample.MessageGateway

    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {
        messageGateway.publish(
            MyMessage("1", "hello world, I have just started up")
        )
        println("hello world, I have just started up")
    }

}