package com.accumulus.challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

// data class PersonToppingsChoice(val name: String, val topics: List<String>)

data class Greeting(val id: Long, val content: String)

@RestController
class GreetingController {
	private val counter = AtomicLong()
	@GetMapping("/greeting")
	fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String?): Greeting {
		return Greeting(counter.incrementAndGet(), String.format(template, name))
	}

	companion object {
		private const val template = "Hello, %s!"
	}
}

@SpringBootApplication
class ChallengeApplication

fun main(args: Array<String>) {
	runApplication<ChallengeApplication>(*args)
	//print(PersonToppingsChoice("Bob", listOf("cheese", "pepperoni")))
}
