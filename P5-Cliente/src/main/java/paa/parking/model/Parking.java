package paa.parking.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import paa.parking.business.ParkingServiceException;

@Entity
@Table(name="PARKING")

public class Parking {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String address;
	private int spaces;
	private int maxReservationsPerDay;
	private double longitud;
	private double latitud;

	@OneToMany(mappedBy="parking") //Un parking tiene muchas reservas
	private List<Reservation> reservations;

	public Parking() {}

	public Parking(Long id, String name, String address, int spaces,
			int maxReservationsPerDay, double longitud, double latitud) {
		this.setId(id);
		this.setName(name);
		this.setAddress(address);
		this.setSpaces(spaces);
		this.setMaxReservationsPerDay(maxReservationsPerDay);
		this.setLongitud(longitud);
		this.setLatitud(latitud);
		validate();
	}
	
	public void validate()
	{
		require(name == null || name.isBlank() || address == null || address.isBlank(), "Parametros de parking no pueden ser nulos o dejarse en blanco");
	
		require(spaces < maxReservationsPerDay, "Numero total de plazas no puede ser menos que máximas plazas reservables");
		
		require(spaces < 0 || maxReservationsPerDay < 0, "Numero de plazas no pueden ser negativas");
		
		require(longitud < -180 || longitud > 180, "Longitud fuera de rango");
		
		require(latitud < -90 || latitud > 90, "Latitud fuera de rango");
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getSpaces() {
		return spaces;
	}
	public void setSpaces(int spaces) {
		this.spaces = spaces;
	}
	public int getMaxReservationsPerDay() {
		return maxReservationsPerDay;
	}
	public void setMaxReservationsPerDay(int maxReservationsPerDay) {
		this.maxReservationsPerDay = maxReservationsPerDay;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public List<Reservation> getReservations() {
		return reservations;
	}
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}


	@Override
	public String toString() {
		return String.format("[%d] %s", this.id, this.name);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Parking)) return false;
		
		Parking p = (Parking) obj;
		return Objects.equals(id, p.id);
	}
}
