package com.drbaz.MongoDB

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MongoDbApplication
/* remember to run
*systemctl start mongodb
* *in Terminal
*/

fun main(args: Array<String>) {
	runApplication<MongoDbApplication>(*args)
}
