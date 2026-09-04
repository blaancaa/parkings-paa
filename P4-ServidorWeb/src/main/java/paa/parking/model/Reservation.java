package paa.parking.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import paa.parking.business.ParkingServiceException;

@Entity
@Table(name="RESERVATION")
public class Reservation {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String licensePlate;
	private LocalDate date;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="PARKING_CODE") //varias reservaciones por parking
	private Parking parking;

	public Reservation() {}

	public Reservation(Long id, String licensePlate,
			LocalDate date, Parking parking) {
		this.setId(id);
		this.setLicensePlate(licensePlate);
		this.setDate(date);
		this.setParking(parking);
		validate();
	}
	
	public void validate()
	{
		require(licensePlate == null || licensePlate.isBlank() || date == null || parking.getId() == null, "Parametros de reservación no pueden ser nulos o dejarse en blanco");
	}
	
	private static void require(boolean condition, String message) {
        if (condition) throw new ParkingServiceException(message);
    }

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Parking getParking() {
		return parking;
	}
	public void setParking(Parking parking) {
		this.parking = parking;
	}

	@Override
	public String toString() {
		return String.format("[%d], %s on %s", id, licensePlate,
			date.format((DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
	}
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if (!(obj instanceof Reservation)) return false;

        Reservation r = (Reservation) obj;

        return Objects.equals(id, r.id);
	}
}
