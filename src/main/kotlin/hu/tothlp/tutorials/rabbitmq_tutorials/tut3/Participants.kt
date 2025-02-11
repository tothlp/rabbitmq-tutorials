package hu.tothlp.tutorials.rabbitmq_tutorials.tut3

import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.util.StopWatch
import java.util.concurrent.atomic.AtomicInteger


class Tut3Sender {
	@Autowired
	private lateinit var template: RabbitTemplate

	@Autowired
	private lateinit var fanout: FanoutExchange

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
			template.convertAndSend(fanout.name, "", message)
			println(" [x] Sent '$message'")
		}
	}
}

class Tut3Receiver {

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