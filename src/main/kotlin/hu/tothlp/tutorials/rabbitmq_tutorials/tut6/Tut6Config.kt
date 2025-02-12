package hu.tothlp.tutorials.rabbitmq_tutorials.tut6

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("tut6", "rpc")
@Configuration
class Tut6Config {


	@Profile("client")
	class ClientConfig {

		@Bean
		fun exchange() = DirectExchange("tut.rpc")

		@Bean
		fun client() = Tut6Client()


	}

	@Profile("server")
	class ServerConfig {

		@Bean
		fun server() = Tut6Server()

		@Bean
		fun queue() = Queue("tut.rpc.requests")

		@Bean
		fun exchange() = DirectExchange("tut.rpc")

		@Bean
		fun binding(exchange: DirectExchange, queue: Queue) =
			BindingBuilder.bind(queue).to(exchange).with("rpc")

	}
}