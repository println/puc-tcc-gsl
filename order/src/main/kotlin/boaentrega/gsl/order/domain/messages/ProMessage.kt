package boaentrega.gsl.order.domain.messages

data class ProMessage(val message: String) : Message {
    constructor() : this("")
}
