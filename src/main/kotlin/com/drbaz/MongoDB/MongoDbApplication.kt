package com.drbaz.MongoDB

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MongoDbApplication

fun main(args: Array<String>) {
	runApplication<MongoDbApplication>(*args)
}
