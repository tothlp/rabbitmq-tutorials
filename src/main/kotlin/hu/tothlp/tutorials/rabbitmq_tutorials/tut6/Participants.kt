package hu.tothlp.tutorials.rabbitmq_tutorials.tut6

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled


class Tut6Client {
	@Autowired
	private lateinit var template: RabbitTemplate

	@Autowired
	private lateinit var exchange: DirectExchange

	private var start = 0

	@Scheduled(fixedDelay = 1000, initialDelay = 500)
	fun send() {
		println(" [x] Requesting fib($start)")
		val resp: Int = template.convertSendAndReceive(exchange.name, "rpc", start++) as Int
		println(" [.] Got '$resp'")
	}
}

class Tut6Server {

	@RabbitListener(queues = ["tut.rpc.requests"])
	fun fibonacci(n: Int): Int {
		println(" [x] Received request for $n")
		val result = fib(n)
		println(" [.] Returned $result")
		return result
	}

	private fun fib(n: Int): Int = n.takeIf { n in 0..1 } ?: fib(n - 1) + fib(n - 2)

}