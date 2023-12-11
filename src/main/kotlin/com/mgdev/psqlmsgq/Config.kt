package com.mgdev.psqlmsgq

import com.fasterxml.jackson.databind.ObjectMapper
import org.postgresql.jdbc.PgConnection
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.jdbc.channel.PostgresChannelMessageTableSubscriber
import org.springframework.integration.jdbc.channel.PostgresSubscribableChannel
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore
import org.springframework.integration.jdbc.store.channel.PostgresChannelMessageStoreQueryProvider
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHeaders

@Configuration
class Config {

    @Bean
    fun singleConnectionDataSource(
        @Value("\${spring.datasource.url}") url: String,
        @Value("\${spring.datasource.username}") username: String,
        @Value("\${spring.datasource.password}") password: String
    ) = SingleConnectionDataSource(url, username, password, true)

    @Bean
    fun jdbcChannelMessageStore(singleConnectionDataSource: SingleConnectionDataSource, objectMapper: ObjectMapper) =
        JdbcChannelMessageStore(singleConnectionDataSource).also {
            it.setChannelMessageStoreQueryProvider(PostgresChannelMessageStoreQueryProvider())
            it.setSerializer { ob, outputStream ->
                objectMapper.writeValue(outputStream, ob)
            }
            it.setDeserializer { inputStream ->
                objectMapper.readValue(inputStream, JsonMessage::class.java)
            }
        }

    @Bean
    fun subscribableChannel(
        singleConnectionDataSource: SingleConnectionDataSource,
        jdbcChannelMessageStore: JdbcChannelMessageStore
    ): PostgresSubscribableChannel {

        val messageTableSubscriber =
            PostgresChannelMessageTableSubscriber { singleConnectionDataSource.connection.unwrap(PgConnection::class.java) }
        return PostgresSubscribableChannel(
            jdbcChannelMessageStore,
            "message",
            messageTableSubscriber
        )
    }

    @Bean
    fun message(subscribableChannel: PostgresSubscribableChannel): MessageChannel {
        return subscribableChannel
    }
}

class JsonMessage(
    private val payload: MyMessage,
    private val headers: Map<String, Any>
) : Message<MyMessage> {

    override fun getPayload(): MyMessage = payload

    override fun getHeaders(): MessageHeaders = MessageHeaders(headers)
}