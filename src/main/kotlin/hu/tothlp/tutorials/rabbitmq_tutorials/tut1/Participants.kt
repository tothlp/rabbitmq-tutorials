package hu.tothlp.tutorials.rabbitmq_tutorials.tut1

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled


class Tut1Sender {
	@Autowired
	private lateinit var template: RabbitTemplate

	@Autowired
	private lateinit var queue: Queue

	@Scheduled(fixedDelay = 1000, initialDelay = 500)
	fun send() = "Hello World!".let {
		template.convertAndSend(queue.name, it)
		println(" [x] Sent '$it'")
	}
}

@RabbitListener(queues = ["hello"])
class Tut1Receiver {

	@RabbitHandler
	fun receive(`in`: String) = println(" [x] Received '$`in`'")
}