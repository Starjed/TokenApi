package auth.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@EnableMongoRepositories
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class AuthApiApplication

fun main(args: Array<String>) {
	runApplication<AuthApiApplication>(*args)
}
