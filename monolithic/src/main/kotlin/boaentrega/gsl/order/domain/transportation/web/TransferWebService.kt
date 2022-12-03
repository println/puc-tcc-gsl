package boaentrega.gsl.order.domain.transportation.web

import boaentrega.gsl.order.domain.transportation.Transfer
import boaentrega.gsl.order.domain.transportation.TransferFilter
import boaentrega.gsl.order.domain.transportation.TransferService
import boaentrega.gsl.support.web.AbstractWebService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class TransferWebService(private val service: TransferService) : AbstractWebService<Transfer>() {
    fun findAll(filter: TransferFilter, pageable: Pageable): Page<Transfer> {
        return service.findAll(filter, pageable)
    }

    fun findById(id: UUID): Transfer {
        val nullableEntity = service.findById(id)
        return assertNotFound(nullableEntity)
    }

    fun movingToNextStorage(transferId: UUID, partnerId: UUID, storage: String): Transfer {
        val nullableEntity = service.movingToNextStorage(transferId, partnerId, storage)
        return assertBadRequest(nullableEntity)
    }

    fun receiveOnStorage(transferId: UUID, partnerId: UUID, storage: String): Transfer {
        val nullableEntity = service.receiveOnStorage(transferId, partnerId, storage)
        return assertBadRequest(nullableEntity)
    }
}