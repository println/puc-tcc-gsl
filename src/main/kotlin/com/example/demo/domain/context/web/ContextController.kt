package com.example.demo.domain.context.web

import com.example.demo.domain.context.ContextService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ContextController(val service: ContextService) {
    @GetMapping
    fun checkEvents(): String {
        val date = Date().toString()
        service.startFire(date)
        return date
    }
}