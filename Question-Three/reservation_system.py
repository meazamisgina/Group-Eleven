class Table:
    def __init__(self, table_id, area, seats):
        self.table_id = table_id
        self.area = area
        self.seats = seats
        self.occupied = False
        self.current_reservation = None
class Guest:
    def __init__(self, name, party_size, is_key_guest=False, preference=None):
        self.name = name
        self.party_size = party_size
        self.is_key_guest = is_key_guest
        self.preference = preference
class Reservation:
    def __init__(self, guest, res_type, time, source):
        self.guest = guest
        self.res_type = res_type
        self.time = time
        self.source = source
        self.status = "pending"
        self.assigned_table = None
class ReservationSystem:
    def __init__(self, tables):
        self.tables = tables
        self.reservations = []
        self.waitlist = []
    def add_reservation(self, guest, time, source="website", res_type="online"):
        reservation = Reservation(guest, res_type, time, source)
        self.reservations.append(reservation)
        self.assign_table(reservation)
        return reservation
    def add_walk_in(self, guest, time=None):
        if time is None:
            time = datetime.now()
        return self.add_reservation(guest, time, source="walk-in", res_type="walk-in")
    def assign_table(self, reservation):
        suitable_tables = [t for t in self.tables if not t.occupied and t.seats >= reservation.guest.party_size]
        if reservation.guest.preference:
            if 'table_id' in reservation.guest.preference:
                suitable_tables = [t for t in suitable_tables if t.table_id == reservation.guest.preference['table_id']]
            elif 'area' in reservation.guest.preference:
                suitable_tables = [t for t in suitable_tables if t.area == reservation.guest.preference['area']]
        if reservation.guest.is_key_guest:
            suitable_tables.sort(key=lambda t: t.table_id)
        if suitable_tables:
            table = suitable_tables[0]
            table.occupied = True
            table.current_reservation = reservation
            reservation.assigned_table = table
            reservation.status = "seated"
        else:
            self.waitlist.append(reservation)
            reservation.status = "waiting"
    def release_table(self, table_id):
        table = next((t for t in self.tables if t.table_id == table_id), None)
        if table and table.occupied:
            table.occupied = False
            if table.current_reservation:
                table.current_reservation.status = "completed"
                table.current_reservation = None
            self.seat_next_waitlist_guest()
    def seat_next_waitlist_guest(self):
        if not self.waitlist:
            return
        self.waitlist.sort(key=lambda r: not r.guest.is_key_guest)
        reservation = self.waitlist.pop(0)
        self.assign_table(reservation)
    def get_current_reservations(self):
        return [r for r in self.reservations if r.status in ("pending", "seated", "waiting")]
    def get_table_status(self):
        status_list = []
        for t in self.tables:
            status_list.append({
                'table_id': t.table_id,
                'area': t.area,
                'seats': t.seats,
                'occupied': t.occupied,
                'current_reservation
                   })
        return status_list 