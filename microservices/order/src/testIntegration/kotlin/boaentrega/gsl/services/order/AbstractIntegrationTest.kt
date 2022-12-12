package boaentrega.gsl.services.order

import boaentrega.gsl.configuration.config.DummyConnectorFactoryTestConfig
import org.junit.jupiter.api.BeforeEach
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder


@ActiveProfiles("test", "integration-test")
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@Import(value = [DummyConnectorFactoryTestConfig::class])
@ContextConfiguration(classes = [DummyConnectorFactoryTestConfig::class])
abstract class AbstractIntegrationTest {

    @Autowired
    lateinit var pageable: PageableHandlerMethodArgumentResolver

    protected lateinit var restMockMvc: MockMvc

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)
        this.restMockMvc = this.getMvcBuilder(this.createResource()).build()
    }

    abstract fun createResource(): Any

    private fun getMvcBuilder(resource: Any): StandaloneMockMvcBuilder {
        return MockMvcBuilders
                .standaloneSetup(resource)
                .setCustomArgumentResolvers(pageable)

    }
}