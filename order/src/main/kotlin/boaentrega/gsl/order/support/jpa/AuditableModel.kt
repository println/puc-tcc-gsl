package boaentrega.gsl.order.support.jpa

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*

@MappedSuperclass

abstract class AuditableModel<T : AuditableModel<T>> : Auditable<T>() {

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
    var id: UUID? = null

}
