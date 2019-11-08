package api.boats;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;

public class Reservation {

	@Getter
	private static AtomicLong idCounter = new AtomicLong(0);
	@Getter
	private long id;
	@Getter
	private long boatId;
	@Getter
	private LocalDate startDate;
	@Getter
	private LocalDate endDate;
	@Getter
	private String clientPESEL;

	public Reservation(Long boatId, LocalDate startDate, LocalDate endDate, String clientPESEL) {
		this.id = idCounter.getAndIncrement();
		this.boatId = boatId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.clientPESEL = clientPESEL;
	}

}
