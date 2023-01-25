package com.accumulus.challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

data class UserToppingsSubmission(val email: String, val topics: List<String>)
data class ToppingFrequencyMap(val frequencyMap: Map<String, Int>)

class InMemoryDataStore {
	// Within the class, I don't box types (such as the user email) for simplicity.
	private val emailToppingsMap: MutableMap<String, List<String>> = mutableMapOf()

	fun pushUserToppingsSubmission(submission: UserToppingsSubmission) {
		emailToppingsMap[submission.email] = submission.topics
	}

	fun showToppingsMap(): MutableMap<String, List<String>> {
		return emailToppingsMap
	}

	fun getToppingsCountMap(): ToppingFrequencyMap {
		return ToppingFrequencyMap(
			emailToppingsMap
			.flatMap { it.value }
			.groupingBy { it }
			.eachCount())
	}
}

////////////////////////////////////////////////
// demo code below this line
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
	//runApplication<ChallengeApplication>(*args)
	//print(PersonToppingsChoice("Bob", listOf("cheese", "pepperoni")))
	val dataStore = InMemoryDataStore()
	dataStore.pushUserToppingsSubmission(UserToppingsSubmission("foo@com.com", listOf("a", "b", "c")))
	dataStore.pushUserToppingsSubmission(UserToppingsSubmission("bar@com.com", listOf("a", "c")))
	dataStore.pushUserToppingsSubmission(UserToppingsSubmission("baz@com.com", listOf("c")))
	println(dataStore.showToppingsMap())
	println(dataStore.getToppingsCountMap())
}
