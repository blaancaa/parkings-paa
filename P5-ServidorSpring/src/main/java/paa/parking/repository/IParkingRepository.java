package paa.parking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import paa.parking.model.Parking;
import paa.parking.model.Reservation;

public interface IParkingRepository extends JpaRepository<Parking, Long> 
{
	
}
