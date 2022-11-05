package boaentrega.gsl.order

import boaentrega.gsl.order.configuration.config.ConnectorsFactoryTestConfig
import org.junit.jupiter.api.BeforeEach
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder


@ActiveProfiles("test", "integration-test")
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@Import(value = [ConnectorsFactoryTestConfig::class])
@ContextConfiguration(classes = [ConnectorsFactoryTestConfig::class])
abstract class AbstractIntegrationTest {

    protected lateinit var restMockMvc: MockMvc

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        this.restMockMvc = this.getMvcBuilder(this.createResource()).build()
    }

    abstract fun createResource(): Any

    private fun getMvcBuilder(resource: Any): StandaloneMockMvcBuilder {
        return MockMvcBuilders.standaloneSetup(resource)
    }
}