package boaentrega.gsl.delivery.support.jpa

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class Auditable<T : Auditable<T>> : Serializable {

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    var createdAt: Date? = null

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    var lastModified: Date? = null

}
