package boaentrega.gsl.order.domain.transportation.api

import org.springframework.stereotype.Service

@Service
class RouteService {

    private companion object {
        var storages = listOf("storage A", "storage B", "storage C", "storage D")
    }

    fun calculateInitialAndFinalStops(currentPosition: String, deliveryAddress: String): Pair<String, String> {
        return Pair(storages.first(), storages.last())
    }

    fun calculateNextStop(currentPosition: String, deliveryAddress: String): String {
        val index = storages.indexOf(currentPosition)
        return storages[index + 1]
    }
}