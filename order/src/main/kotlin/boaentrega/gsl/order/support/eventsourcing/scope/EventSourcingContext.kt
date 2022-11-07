package boaentrega.gsl.order.support.eventsourcing.scope

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.util.*

@Component
@RequestScope
class EventSourcingContext {
    var trackId: UUID? = null
}