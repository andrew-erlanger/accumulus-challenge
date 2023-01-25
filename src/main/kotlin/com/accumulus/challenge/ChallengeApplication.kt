package com.accumulus.challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

data class UserToppingsSubmission(val email: String, val topics: List<String>)
data class ToppingCountMap(val countMap: Map<String, Int>)

class InMemoryDataStore {
	// Within the class, I don't box types (such as the user email) for simplicity.
	private val emailToppingsMap: MutableMap<String, List<String>> = mutableMapOf()

	fun pushUserToppingsSubmission(submission: UserToppingsSubmission) {
		emailToppingsMap[submission.email] = submission.topics
	}

	// This function is used for testing.
	fun showToppingsMap(): MutableMap<String, List<String>> {
		return emailToppingsMap
	}

	// As the number of users grows, the `emailToppingsMap` grows linearly, and the time and space
	// complexity of `getToppingsCountMap` both grow linearly. If we needed to cut down on the
	// time complexity, we could persist the `ToppingsCountMap` and have the function
	// `pushUserToppingsSubmission` update the `ToppingsCountMap` on-the-fly. For simplicity's
	// sake, here I simply compute the `ToppingsCountMap` each time.
	fun getToppingsCountMap(): ToppingCountMap {
		return ToppingCountMap(
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
	val dataStore = InMemoryDataStore()
	dataStore.pushUserToppingsSubmission(UserToppingsSubmission("foo@com.com", listOf("a", "b", "c")))
	dataStore.pushUserToppingsSubmission(UserToppingsSubmission("bar@com.com", listOf("a", "c")))
	dataStore.pushUserToppingsSubmission(UserToppingsSubmission("baz@com.com", listOf("c")))
	println(dataStore.showToppingsMap())
	println(dataStore.getToppingsCountMap())
}
