package oc.moneylog.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoneyLogServerApplication

fun main(args: Array<String>) {
	runApplication<MoneyLogServerApplication>(*args)
}
