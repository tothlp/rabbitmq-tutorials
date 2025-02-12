package hu.tothlp.tutorials.rabbitmq_tutorials.tut4

import org.springframework.amqp.core.AnonymousQueue
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("tut4", "routing")
@Configuration
class Tut4Config {

	@Bean
	fun direct() = DirectExchange("tut.direct")

	@Profile("receiver")
	class ReceiverConfig {

		@Bean
		fun autoDeleteQueue1() = AnonymousQueue()

		@Bean
		fun autoDeleteQueue2() = AnonymousQueue()

		@Bean
		fun binding1a(direct: DirectExchange, autoDeleteQueue1: Queue) = BindingBuilder.bind(autoDeleteQueue1).to(direct).with("orange")

		@Bean
		fun binding2a(direct: DirectExchange, autoDeleteQueue1: Queue) = BindingBuilder.bind(autoDeleteQueue1).to(direct).with("black")

		@Bean
		fun binding1b(direct: DirectExchange, autoDeleteQueue2: Queue) = BindingBuilder.bind(autoDeleteQueue2).to(direct).with("green")

		@Bean
		fun binding2b(direct: DirectExchange, autoDeleteQueue2: Queue) = BindingBuilder.bind(autoDeleteQueue2).to(direct).with("black")

		@Bean
		fun receiver() = Tut4Receiver()
	}

	@Profile("sender")
	@Bean
	fun sender() = Tut4Sender()
}