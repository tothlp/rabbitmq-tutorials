package hu.tothlp.tutorials.rabbitmq_tutorials.tut5

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.util.StopWatch
import java.util.concurrent.atomic.AtomicInteger


class Tut5Sender {
	@Autowired
	private lateinit var template: RabbitTemplate

	@Autowired
	private lateinit var topic: TopicExchange

	private val index = AtomicInteger(0)
	private val count = AtomicInteger(0)

	private val keys = arrayOf("quick.orange.rabbit", "lazy.orange.elephant", "quick.orange.fox",
		"lazy.brown.fox", "lazy.pink.rabbit", "quick.brown.fox")

	@Scheduled(fixedDelay = 1000, initialDelay = 500)
	fun send() {
		if (this.index.incrementAndGet() == 3) this.index.set(0);
		val key = keys[index.get()]

		buildString {
			append("Hello to ")
			append("$key ${count.get()}")
		}.let { message ->
			template.convertAndSend(topic.name, key, message)
			println(" [x] Sent '$message'")
		}
	}
}

class Tut5Receiver {

	@RabbitListener(queues = ["#{autoDeleteQueue1.name}"])
	fun receive1(input: String) = receive(input, 1)

	@RabbitListener(queues = ["#{autoDeleteQueue2.name}"])
	fun receive2(input: String) = receive(input, 2)

	private fun receive(input: String, receiver: Int) = StopWatch().run {
		start()
		println("instance $receiver [x] Received '$input'")
		doWork(input)
		stop()
		println("instance $receiver [x] Done in ${totalTimeSeconds}s")
	}

	private fun doWork(input: String) = input.asSequence().filter { it == '.' }.forEach { _ -> Thread.sleep(500) }

}