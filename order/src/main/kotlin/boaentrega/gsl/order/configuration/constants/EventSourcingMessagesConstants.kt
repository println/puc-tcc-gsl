package boaentrega.gsl.order.configuration.constants

object EventSourcingMessagesConstants {
    object Commands {
        const val CREATE = "create"
        const val UPDATE = "update"
        const val DELETE = "update"
    }

    object Events {
        const val CREATED = "created"
        const val UPDATED = "updated"
        const val DELETED = "deleted"
    }

    object Documents {
        const val NEW = "new"
        const val MODIFY = "modify"
        const val REMOVE = "remove"
    }
}