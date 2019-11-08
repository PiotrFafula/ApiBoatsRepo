package api.boats;

import lombok.Getter;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Setter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Boat {
	// globalny licznik id
	@Getter
	private static AtomicLong idCounter = new AtomicLong(0);
	// id wybranej łodzi
	@Getter
	private Long id;

	// dostępne typy łodzi (podawane przy tworzeniu metodą POST jako text/plain)
	@Getter
	public enum Type {
		kajak, jacht, katamaran, skuter, ponton
	}

	// typ łodzi
	@Getter
	private Type type;

	public Boat(Type type) {
		this.type = type;
		this.id = idCounter.getAndIncrement();
	}

}
