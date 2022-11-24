package boaentrega.gsl.transportation.support.eventsourcing.controller

import boaentrega.gsl.transportation.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.transportation.support.functions.Functions

object Constants {
    val HANDLE_ID_ANY = Functions.Message.extractIdentifier(Any::class)
    val HANDLE_ID_COMMAND_MESSAGE = Functions.Message.extractIdentifier(CommandMessage::class)
    const val HANDLE_ID_C = "C"
    const val HANDLE_ID_ERROR = "ERROR"

    const val EMPTY_MESSAGE = "{}"
    const val WRONG_MESSAGE = "{TEST}"
}