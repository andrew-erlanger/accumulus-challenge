package com.accumulus.challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

data class UserToppingsSubmission(val email: String, val toppings: List<String>)
data class ToppingCountMap(val toppingCountMap: Map<String, Int>)

object InMemoryDataStore {
	// Within the class, I don't box types (such as the user email) for simplicity.
	private val emailToppingsMap: MutableMap<String, List<String>> = mutableMapOf()

	fun pushUserToppingsSubmission(submission: UserToppingsSubmission) {
		emailToppingsMap[submission.email] = submission.toppings
	}

	// This function is used for testing only.
	fun showToppingsMap(): MutableMap<String, List<String>> {
		return emailToppingsMap
	}

	// This function allows for resetting the application state without restarting.
	fun clearToppingsMap() {
		emailToppingsMap.clear()
	}

	// As the number of users grows, the `emailToppingsMap` grows linearly, and the time and space
	// complexity of `getToppingsCountMap` both grow linearly. If we needed to cut down on the
	// time complexity, we could persist the `ToppingsCountMap` and have the function
	// `pushUserToppingsSubmission` update the `ToppingsCountMap` on-the-fly. For simplicity's
	// sake, here I simply compute the `ToppingsCountMap` each time.
	fun getToppingCountMap(): ToppingCountMap {
		return ToppingCountMap(
			emailToppingsMap
			.flatMap { it.value }
			.groupingBy { it }
			.eachCount())
	}
}

@RestController
class ChallengeController {
	@GetMapping("/toppingCountMap")
	fun toppingCountMap(): ToppingCountMap {
		return InMemoryDataStore.getToppingCountMap()
	}

	// We treat the ToppingCountMap as a resource, implementing a delete method.
	@DeleteMapping("/toppingCountMap")
	fun clearToppingCountMap() = InMemoryDataStore.clearToppingsMap()

	@PostMapping("/userToppingSubmission")
	fun userToppingSubmission(@RequestBody submission: UserToppingsSubmission): String {
		InMemoryDataStore.pushUserToppingsSubmission(submission)
		return "Ok"
	}

}

@SpringBootApplication
class ChallengeApplication

fun main(args: Array<String>) {
	runApplication<ChallengeApplication>(*args)
}
