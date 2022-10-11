package boaentrega.gsl.support.jpa

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditableModel<T : AuditableModel<T>?> : Serializable {
    @CreatedDate
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    @LastModifiedDate
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    var lastModified: Date? = null

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
    val id: UUID? = null


    fun createdAt(createdAt: Date?): T {
        this.createdAt = createdAt
        return this as T
    }

    fun lastModified(lastModified: Date?): T {
        this.lastModified = lastModified
        return this as T
    }
}
