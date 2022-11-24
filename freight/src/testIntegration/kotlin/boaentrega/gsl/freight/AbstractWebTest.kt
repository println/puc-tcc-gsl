package boaentrega.gsl.freight

import boaentrega.gsl.freight.support.jpa.AuditableModel
import org.hamcrest.Matchers
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*
import kotlin.streams.toList

abstract class AbstractWebTest<T : AuditableModel<T>> : AbstractEventSourcingTest() {
    val easyRandom = EasyRandom()
    var entities = listOf<AuditableModel<T>>()

    abstract fun getRepository(): JpaRepository<T, *>
    abstract fun getEntityType(): Class<T>
    abstract fun preProcessing(data: T): Unit
    abstract fun getResource(): String

    @BeforeEach
    fun setupWeb() {
        reloadData { preProcessing(it) }
    }

    fun reloadData(adjustment: (T) -> Unit) {
        val data = easyRandom.objects(getEntityType(), 200).toList()
        data.forEach { adjustment(it) }
        getRepository().deleteAll()
        entities = getRepository().saveAllAndFlush(data)
    }

    @ParameterizedTest
    @CsvSource(
            "'', true, 0, false",
            "?page=1, true, 0, false",
            "?page=2, false, 1, false",
            "?page=10, false, 9, true"
    )
    fun getAll(page: String, first: Boolean, index: Int, last: Boolean) {
        restMockMvc.perform(MockMvcRequestBuilders.get("${getResource()}$page")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.content", Matchers.hasSize<String>(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.first").value(first))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.number").value(index))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.last").value(last))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.totalPages").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.numberOfElements").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.size").value(20))
    }

    @Test
    fun getById() {
        val id = entities.first().id
        restMockMvc.perform(MockMvcRequestBuilders.get("${getResource()}/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("\$").isNotEmpty)
    }

    @Test
    fun getByWrongIdNotFound() {
        val id = UUID.randomUUID()
        restMockMvc.perform(MockMvcRequestBuilders.get("${getResource()}/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}