package api.boats;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api.boats.Boat.Type;

import java.net.URI;

import lombok.Getter;
import java.util.ArrayList;

@RestController
public class BoatsController {

	// lista wszystkich lodzi
	@Getter
	private static ArrayList<Boat> boats = new ArrayList<Boat>();

	// dodaje lodz b do listy boats
	private static void addBoatToList(Boat b) {
		boats.add(b);
	}

	// zwraca 200 i w body: JSON z ArrayList boats
	@ResponseBody
	@GetMapping("/boats")
	public ResponseEntity<?> getAllBoats() {
		return ResponseEntity.ok(boats);
	}

	// zwraca lodz o podanym id jesli istnieje - jesli nie, to zwraca null
	public static Boat findBoatById(Long id) {
		for (Boat boat : boats) {
			if (boat.getId().equals(id))
				return boat;
		}
		return null;
	}

	// zwraca lodz o podanym id oraz status 200 - 404 jesli nie ma lodzi o danym id
	@ResponseBody
	@GetMapping("/boats/{id}")
	public ResponseEntity<?> getBoatById(@PathVariable Long id) {
		if (findBoatById(id) != null) {
			return ResponseEntity.ok(findBoatById(id));
		}
		return ResponseEntity.notFound().build();
	}

	// testowe
	@GetMapping(value = "/")
	public ResponseEntity<String> testOK() {
		return ResponseEntity.ok(new String());
	}

	// POST - tworzenie lodzi danego typu -> 201
	// w body text/plain z wybranym z enuma (Boat.Type) typem lodzi
	// np. poprzez curl -X POST http://ip:port/boats -H "Content-type: text/plain" -d
	// "kajak"
	@PostMapping(value = "/boats", consumes = "text/plain")
	public ResponseEntity<?> addBoat(@RequestBody String type) {
		Boat tmpBoat = new Boat(Type.valueOf(type));
		addBoatToList(tmpBoat);
		return ResponseEntity.created(URI.create("/boats/" + tmpBoat.getId().toString())).build();
	}

	// DELETE - usuwanie łodzi o podanym id -> 200/404
	@DeleteMapping("/boats/{id}")
	public ResponseEntity<?> deleteBoat(@PathVariable Long id) {
		if (boats.remove(findBoatById(id))) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}