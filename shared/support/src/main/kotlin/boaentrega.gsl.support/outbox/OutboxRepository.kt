package boaentrega.gsl.support.outbox


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface OutboxRepository : JpaRepository<OutboxMessage, UUID> {

    fun getTop10ByIsPublishedFalse(): List<OutboxMessage>

    fun countByIsPublishedFalse(): Long

    fun countByIsPublishedTrue(): Long

    @Modifying(clearAutomatically = true)
    @Query("update OutboxMessage m set m.isPublished = true where m.messageId in (:ids)")
    fun markAsPublished(@Param("ids") messageIds: List<UUID>)
}