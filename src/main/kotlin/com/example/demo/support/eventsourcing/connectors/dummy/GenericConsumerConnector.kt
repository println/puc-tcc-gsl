package com.example.demo.support.eventsourcing.connectors.dummy

import com.example.demo.support.eventsourcing.connectors.AbstractConsumerConnector
import com.example.demo.support.eventsourcing.messages.Message
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

class GenericConsumerConnector(val bean: String) : AbstractConsumerConnector() {

    override fun afterStart() {
        val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        Thread.sleep(1000)
        consume(mapper.readValue("""{"type":"EVENT", "contentId":"promessage", "contentJson":"\"{\"message\":\"$bean\"}\""}""", Message::class.java))
        Thread.sleep(1000)
        consume(mapper.readValue("""{"type":"EVENT", "contentId":"promessage", "contentJson":"\"{\"message\":\"$bean\"}\""}""", Message::class.java))
        Thread.sleep(1000)
        consume(mapper.readValue("""{"type":"EVENT", "contentId":"promessages", "contentJson":"\"{\"message\":\"$bean\"}\""}""", Message::class.java))
        Thread.sleep(1000)
        consume(mapper.readValue("""{"type":"EVENT", "contentId":"xptomessage", "contentJson":"\"{\"message\":\"$bean\"}\""}""", Message::class.java))

        Thread.sleep(5000)
        consume(mapper.readValue("""{"type":"EVENT", "contentId":"supermessage", "contentJson":"\"{\"message\":\"$bean\"}\""}""", Message::class.java))
    }

    override fun getId(): String {
        return "teste"
    }

    override fun startConsumer() {
        println("lauched!")
    }

    override fun stopConsumer() {
        println("shutdown!")
    }
}