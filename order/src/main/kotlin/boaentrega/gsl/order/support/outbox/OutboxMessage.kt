package boaentrega.gsl.order.support.outbox

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class OutboxMessage {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = [
                Parameter(
                        name = "uuid_gen_strategy_class",
                        value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                )
            ]
    )
    @Column(name = "id", updatable = false, nullable = false)
    var messageId: UUID? = null

    val destination: String

    @Column(length=512, updatable = false, nullable = false)
    val payload: String

    val createdAt: OffsetDateTime

    var isPublished = false

    constructor(destination: String, payload: String) {
        this.destination = destination
        this.payload = payload
        createdAt = OffsetDateTime.now()
    }
}
