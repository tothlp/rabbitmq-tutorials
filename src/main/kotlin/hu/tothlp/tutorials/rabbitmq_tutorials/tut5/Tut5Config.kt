package hu.tothlp.tutorials.rabbitmq_tutorials.tut5

import org.springframework.amqp.core.AnonymousQueue
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("tut5", "topics")
@Configuration
class Tut5Config {

	@Bean
	fun topic() = TopicExchange("tut.topic")

	@Profile("receiver")
	class ReceiverConfig {

		@Bean
		fun receiver() = Tut5Receiver()

		@Bean
		fun autoDeleteQueue1() = AnonymousQueue()

		@Bean
		fun autoDeleteQueue2() = AnonymousQueue()

		@Bean
		fun binding1a(topic: TopicExchange, autoDeleteQueue1: Queue) = BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.orange.*")

		@Bean
		fun binding1b(topic: TopicExchange, autoDeleteQueue1: Queue) = BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.*.rabbit")

		@Bean
		fun binding2a(topic: TopicExchange, autoDeleteQueue2: Queue) = BindingBuilder.bind(autoDeleteQueue2).to(topic).with("lazy.#")

	}

	@Profile("sender")
	@Bean
	fun sender() = Tut5Sender()
}