package hu.tothlp.tutorials.rabbitmq_tutorials.tut2

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.util.StopWatch
import java.util.concurrent.atomic.AtomicInteger


class Tut2Sender {
	@Autowired
	private lateinit var template: RabbitTemplate

	@Autowired
	private lateinit var queue: Queue

	private val dots = AtomicInteger(0)
	private val count = AtomicInteger(0)

	@Scheduled(fixedDelay = 1000, initialDelay = 500)
	fun send() {
		if (dots.incrementAndGet() == 4) {
			dots.set(1)
		}
		buildString {
			append("Hello")
			for (i in 0 until dots.get()) {
				append('.')
			}
			append(count.incrementAndGet())
		}.let { message ->
			template.convertAndSend(queue.name, message)
			println(" [x] Sent '$message'")
		}
	}
}

@RabbitListener(queues = ["hello"])
class Tut2Receiver(private val instance: Int) {

	@RabbitHandler
	fun receive(input: String) = StopWatch().run {
		start()
		println("instance $instance [x] Received '$input'")
		doWork(input)
		stop()
		println("instance $instance [x] Done in ${totalTimeSeconds}s")
	}

	private fun doWork(input: String) = input.asSequence().filter { it == '.' }.forEach { _ -> Thread.sleep(500) }

}