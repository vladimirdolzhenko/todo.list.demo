package com.example.todo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@Import(Beans::class)
@SpringBootApplication
class TodoApplication

fun main(args: Array<String>) {
    SpringApplication.run(TodoApplication::class.java, *args)
}
