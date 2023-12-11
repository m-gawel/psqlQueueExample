package com.mgdev.psqlmsgq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.jdbc.channel.PostgresSubscribableChannel
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel

@EnableIntegration
@IntegrationComponentScan
@SpringBootApplication
class PsqlMsgQApplication

fun main(args: Array<String>) {
    runApplication<PsqlMsgQApplication>(*args)
}
