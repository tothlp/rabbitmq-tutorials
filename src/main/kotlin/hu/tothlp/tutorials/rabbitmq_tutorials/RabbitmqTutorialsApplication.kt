package hu.tothlp.tutorials.rabbitmq_tutorials

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
class RabbitmqTutorialsApplication {

	@Profile("usage_message")
	@Bean
	fun usage(): CommandLineRunner = CommandLineRunner {
			println("This app uses Spring Profiles to control its behavior.")
			println("Sample usage: java -jar rabbit-tutorials.jar --spring.profiles.active=hello-world,sender")
	}

	@Profile("!usage_message")
	@Bean
	fun tutorial(): RabbitAmqpTutorialsRunner = RabbitAmqpTutorialsRunner()

}

class RabbitAmqpTutorialsRunner : CommandLineRunner {
	@Value("\${tutorial.client.duration:0}")
	private val duration = 0

	@Autowired
	private val ctx: ConfigurableApplicationContext? = null

	@Throws(Exception::class)
	override fun run(vararg arg0: String) {
		println("Ready ... running for " + duration + "ms")
		Thread.sleep(duration.toLong())
		ctx!!.close()
	}
}

fun main(args: Array<String>) {
	runApplication<RabbitmqTutorialsApplication>(*args)
}


