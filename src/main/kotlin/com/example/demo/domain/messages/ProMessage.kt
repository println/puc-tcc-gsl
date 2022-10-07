package com.example.demo.domain.messages

data class ProMessage(val message: String) : Message {
    constructor() : this("")
}
