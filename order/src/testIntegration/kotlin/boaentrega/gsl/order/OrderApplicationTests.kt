package boaentrega.gsl.order

import boaentrega.gsl.order.configuration.config.ConnectorsFactoryTestConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@Import(value = [ConnectorsFactoryTestConfig::class])
@ContextConfiguration(classes = [ConnectorsFactoryTestConfig::class])
class OrderApplicationTests {

	@Test
	fun contextLoads() {
	}

}
