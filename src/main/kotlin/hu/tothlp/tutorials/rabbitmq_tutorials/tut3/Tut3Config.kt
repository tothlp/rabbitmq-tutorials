package hu.tothlp.tutorials.rabbitmq_tutorials.tut3

import org.springframework.amqp.core.AnonymousQueue
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("tut3", "pub-sub", "publish-subscribe")
@Configuration
class Tut3Config {

	@Bean
	fun fanout() = FanoutExchange("tut.fanout")

	@Profile("receiver")
	class ReceiverConfig {

		@Bean
		fun autoDeleteQueue1() = AnonymousQueue()

		@Bean
		fun autoDeleteQueue2() = AnonymousQueue()

		@Bean
		fun binding1(fanout: FanoutExchange, autoDeleteQueue1: Queue) = BindingBuilder.bind(autoDeleteQueue1).to(fanout)

		@Bean
		fun binding2(fanout: FanoutExchange, autoDeleteQueue2: Queue) = BindingBuilder.bind(autoDeleteQueue2).to(fanout)

		@Bean
		fun receiver() = Tut3Receiver()
	}

	@Profile("sender")
	@Bean
	fun sender() = Tut3Sender()
}