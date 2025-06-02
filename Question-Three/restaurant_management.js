class Table {
  constructor(id, area, seats) {
    this.id = id;
    this.area = area;
    this.seats = seats;
    this.occupied = false;
    this.currentReservation = null;
  }
}
class Guest {
  constructor(name, partySize, isKeyGuest = false, preferences = {}) {
    this.name = name;
    this.partySize = partySize;
    this.isKeyGuest = isKeyGuest;
    this.preferences = preferences;
  }
}
class Reservation {
  constructor(guest, type, time, source) {
    this.guest = guest;
    this.type = type;
    this.time = time;
    this.source = source;
    this.status = "pending";
    this.assignedTable = null;
  }
}
class ReservationSystem {
  constructor(tables) {
    this.tables = tables;
    this.reservations = [];
    this.waitlist = [];
  }
  addReservation(guest, time, source = "website", type = "online") {
    const reservation = new Reservation(guest, type, time, source);
    this.reservations.push(reservation);
    this.assignTable(reservation);
    return reservation;
  }
  addWalkIn(guest, time = new Date()) {
    return this.addReservation(guest, time, "walk-in", "walk-in");
  }
  assignTable(reservation) {
    let candidateTables = this.tables.filter(
      t => !t.occupied && t.seats >= reservation.guest.partySize
    );
    if (reservation.guest.preferences.tableId) {
      candidateTables = candidateTables.filter(
        t => t.id === reservation.guest.preferences.tableId
      );
    } else if (reservation.guest.preferences.area) {
      candidateTables = candidateTables.filter(
        t => t.area === reservation.guest.preferences.area
      );
    }
    candidateTables = candidateTables.sort((a, b) => {
      if (reservation.guest.isKeyGuest) {
        return a.id - b.id;
      }
      return 0;
    });
    if (candidateTables.length > 0) {
      const table = candidateTables[0];
      table.occupied = true;
      table.currentReservation = reservation;
      reservation.assignedTable = table;
      reservation.status = "seated";
    } else {
      this.waitlist.push(reservation);
      reservation.status = "waiting";
    }
  }
  releaseTable(tableId) {
    const table = this.tables.find(t => t.id === tableId);
    if (table && table.occupied) {
      table.occupied = false;
      if (table.currentReservation) {
        table.currentReservation.status = "completed";
        table.currentReservation = null;
      }
      this.seatNextWaitlistGuest();
    }
  }
  seatNextWaitlistGuest() {
    if (this.waitlist.length === 0) return;
    this.waitlist.sort((a, b) => b.guest.isKeyGuest - a.guest.isKeyGuest);
    const reservation = this.waitlist.shift();
    this.assignTable(reservation);
  }
  getCurrentReservations() {
    return this.reservations.filter(
      r => ["pending", "seated", "waiting"].includes(r.status)
    );
  }
  getTableStatus() {
    return this.tables.map(t => ({
      id: t.id,
      area: t.area,
      seats: t.seats,
      occupied: t.occupied,
      currentReservation: t.currentReservation
        ? t.currentReservation.guest.name
        : null,
    }));
  }
}
const tables = [
  new Table(1, "Window", 4),
  new Table(2, "Main", 2),
  new Table(3, "Main", 4),
  new Table(4, "Patio", 6),
  new Table(5, "Window", 2),
];
const system = new ReservationSystem(tables);
const guest1 = new Guest("Kisanet", 2, true, { area: "Window" });
system.addReservation(guest1, new Date("2025-05-27T19:00:00"));
const guest2 = new Guest("Meaza", 4, false);
system.addWalkIn(guest2);
const guest3 = new Guest("Eyuel", 2, true, { tableId: 5 });
system.addReservation(guest3, new Date("2025-05-27T19:15:00"));
system.releaseTable(1);
console.log(system.getTableStatus());
console.log(system.getCurrentReservations()); 