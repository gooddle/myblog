package com.teamsparta.myblog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MyblogApplication

fun main(args: Array<String>) {
    runApplication<MyblogApplication>(*args)
}
