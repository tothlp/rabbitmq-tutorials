package hu.tothlp.tutorials.rabbitmq_tutorials.tut2

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("tut2", "work-queues")
@Configuration
class Tut2Config {
	@Bean
	fun hello(): Queue = Queue("hello")

	@Profile("receiver")
	class ReceiverConfig {
		@Bean
		fun receiver1(): Tut2Receiver {
			return Tut2Receiver(1)
		}

		@Bean
		fun receiver2(): Tut2Receiver {
			return Tut2Receiver(2)
		}
	}

	@Profile("sender")
	@Bean
	fun sender(): Tut2Sender = Tut2Sender()
}