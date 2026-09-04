package paa.parking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import paa.parking.model.Reservation;

public interface IReservationRepository  extends JpaRepository<Reservation, Long>
{
	List<Reservation> findByParkingId(Long parkingId);
	
	List<Reservation> findByParkingIdAndDate(Long parkingId, LocalDate date);
	
	@Query("SELECT COUNT(r) FROM Reservation r WHERE r.parking.id = :parkingId AND r.date = :date")
	long ResOcupadas_ByParkingIdAndDate(Long parkingId, LocalDate date);

	boolean existsByLicensePlateAndDateAndParkingId(String licensePlate, LocalDate date, Long parkingId);

	@Query("SELECT COUNT(r) FROM Reservation r WHERE r.licensePlate = :licensePlate AND r.date = :date")
	long totalReservas_ByLicensePlateAndDate(String licensePlate, LocalDate date);

}
