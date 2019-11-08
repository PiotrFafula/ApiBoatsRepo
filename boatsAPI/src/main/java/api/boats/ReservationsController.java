package api.boats;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationsController {

	private Boolean boatExists(Long boatId) {
		if (BoatsController.findBoatById(boatId) != null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	// lista rezerwacji
	private static ArrayList<Reservation> reservations = new ArrayList<Reservation>();

	static void addReservation(Reservation reservation) {
		reservations.add(reservation);
	}

	// zwraca 200 i w body: JSON z ArrayList reservations
	@ResponseBody
	@GetMapping("/reservations")
	public ResponseEntity<?> getAllReservations() {
		return ResponseEntity.ok(reservations);
	}

	// zwraca rezerwację o podanym id jesli istnieje - jesli nie, to zwraca null
	private Reservation findReservationById(Long id) {
		for (Reservation reservation : reservations) {
			if (reservation.getId() == id)
				return reservation;
		}
		return null;
	}

	// dla danej łodzi zwraca listę jej rezerwacji
	static ArrayList<Reservation> getReservationsByBoatId(Long boatId) {
		ArrayList<Reservation> tmp = new ArrayList<Reservation>();
		for (Reservation reservation : reservations) {
			if (reservation.getBoatId() == boatId)
				tmp.add(reservation);
		}
		return tmp;
	}

	// zwraca wybraną rezerwację
	@ResponseBody
	@GetMapping("/reservations/{rId}")
	private ResponseEntity<?> getReservationById(@PathVariable Long rId) {
		if (findReservationById(rId) != null) {
			return ResponseEntity.ok(findReservationById(rId));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// rezerwacje dla łodzi z podanym id
	@ResponseBody
	@GetMapping("/boats/{boatId}/reservations")
	private ResponseEntity<?> getReservationsOfSelectedBoat(@PathVariable Long boatId) {
		if (boatExists(boatId)) {
			return ResponseEntity.ok(getReservationsByBoatId(boatId));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// METODA DELETE - usuwanie rezerwacji o podanym id
	@DeleteMapping("/reservations/{rId}")
	private ResponseEntity<?> deleteReservationById(@PathVariable Long rId) {

		if (reservations.remove(findReservationById(rId))) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	/*
	 * POST - Tworzenie nowej rezerwacji dla wybranej łodzi szablon:
	 * "idLodzi;startDate;endDate;clientPESEL" data jako string w formacie:
	 * dd/MM/yyyy
	 */
	@PostMapping(value = "/reservations", consumes = "text/plain")
	ResponseEntity<?> newReservationCreation(@RequestBody String input) {
		List<String> splitInput = Arrays.asList(input.split(";"));
		try {
			Long id = Long.valueOf(splitInput.get(0).toString());
			// jeśli łódz o danym id nie istnieje to rzucamy wyjątkiem
			if (!boatExists(id))
				throw new Exception("Boat with id: " + id + " does not exist!");
			Reservation tmpReservation = new Reservation(id,
					LocalDate.parse(splitInput.get(1).toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					LocalDate.parse(splitInput.get(2).toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy")),
					splitInput.get(3).toString());
			ReservationsController.addReservation(tmpReservation);
			return ResponseEntity.created(URI.create("/reservations/" + tmpReservation.getId())).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage().toString());
		}
	}

}
