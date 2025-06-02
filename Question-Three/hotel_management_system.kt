
import java.time.LocalDateTime
data class Table(
    val id: Int,
    val area: String,
    val seats: Int,
    var occupied: Boolean = false,
    var currentReservation: Reservation? = null
)
data class Guest(
    val name: String,
    val partySize: Int,
    val isKeyGuest: Boolean = false,
    val preference: Map<String, Any>? = null
)
data class Reservation(
    val guest: Guest,
    val type: String,
    val time: LocalDateTime,
    val source: String,
    var status: String = "pending",
    var assignedTable: Table? = null
)
class ReservationSystem(val tables: List<Table>) {
    private val reservations = mutableListOf<Reservation>()
    private val waitlist = mutableListOf<Reservation>()
    fun addReservation(guest: Guest, time: LocalDateTime, source: String = "website", type: String = "online"): Reservation {
        val reservation = Reservation(guest, type, time, source)
        reservations.add(reservation)
        assignTable(reservation)
        return reservation
    }
    fun addWalkIn(guest: Guest, time: LocalDateTime = LocalDateTime.now()): Reservation {
        return addReservation(guest, time, "walk-in", "walk-in")
    }
    private fun assignTable(reservation: Reservation) {
        var suitableTables = tables.filter { !it.occupied && it.seats >= reservation.guest.partySize }
        reservation.guest.preference?.let { pref ->
            when {
                pref.containsKey("tableId") -> {
                    val tableId = pref["tableId"] as Int
                    suitableTables = suitableTables.filter { it.id == tableId }
                }
                pref.containsKey("area") -> {
                    val area = pref["area"] as String
                    suitableTables = suitableTables.filter { it.area == area }
                }
            }
        }
        if (reservation.guest.isKeyGuest) {
            suitableTables = suitableTables.sortedBy { it.id }
        }
        if (suitableTables.isNotEmpty()) {
            val table = suitableTables.first()
            table.occupied = true
            table.currentReservation = reservation
            reservation.assignedTable = table
            reservation.status = "seated"
        } else {
            waitlist.add(reservation)
            reservation.status = "waiting"
        }
    }
    fun releaseTable(tableId: Int) {
        val table = tables.find { it.id == tableId }
        if (table != null && table.occupied) {
            table.occupied = false
            table.currentReservation?.status = "completed"
            table.currentReservation = null
            seatNextWaitlistGuest()
        }
    }
    private fun seatNextWaitlistGuest() {
        if (waitlist.isEmpty()) return
        waitlist.sortByDescending { it.guest.isKeyGuest }
        val next = waitlist.removeAt(0)
        assignTable(next)
    }
    fun getCurrentReservations(): List<Reservation> {
        return reservations.filter { it.status in listOf("pending", "seated", "waiting") }
    }
    fun getTableStatus(): List<Map<String, Any?>> {
        return tables.map {
            mapOf(
                "id" to it.id,
                "area" to it.area,
                "seats" to it.seats,
                "occupied" to it.occupied,
                "currentReservation" to it.currentReservation?.guest?.name
            )
        }
    }
}

fun main() {
    val tables = listOf(
        Table(1, "Window", 4),
        Table(2, "Main", 2),
        Table(3, "Main", 4),
        Table(4, "Patio", 6),
        Table(5, "Window", 2)
    )
    val system = ReservationSystem(tables)
    val guest1 = Guest("Lwam", 2, true, mapOf("area" to "Window"))
    system.addReservation(guest1, LocalDateTime.of(2025, 5, 27, 19, 0))
    val guest2 = Guest("Bisrat", 4)
    system.addWalkIn(guest2)
    val guest3 = Guest("VIP Hewan", 2, true, mapOf("tableId" to 5))
    system.addReservation(guest3, LocalDateTime.of(2025, 5, 27, 19, 15))
    system.releaseTable(1)
    println(system.getTableStatus())
    println(system.getCurrentReservations().map {
        mapOf(
            "guest" to it.guest.name,
            "status" to it.status,
            "table" to it.assignedTable?.id
        )
    })
} 