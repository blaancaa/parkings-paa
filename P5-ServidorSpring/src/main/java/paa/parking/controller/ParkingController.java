package paa.parking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import paa.parking.business.SpringParkingService;
import paa.parking.model.Parking;
import paa.parking.model.Reservation;

@RestController
public class ParkingController 
{
	@Autowired
	 private SpringParkingService ps;
	
	//create parking
	@GetMapping(value = "/p4-servidor/ParkingServer", params = "action=createParking")
	public Parking createParking(
			@RequestParam("name") String name, 
			@RequestParam("address") String address,
			@RequestParam("spaces") int spaces,
			@RequestParam("maxReservationsPerDay") int maxReservationsPerDay,
			@RequestParam("longitude") double longitude,
			@RequestParam("latitude") double latitude)
	{
		return ps.createParking(name, address, spaces, maxReservationsPerDay, longitude, latitude);
		//PRUEBA: 
        // Parking 1: http://localhost:8080/p4-servidor/ParkingServer?action=createParking&name=ParkingCentro&address=CalleMayor1&spaces=100&maxReservationsPerDay=10&longitude=-3.7&latitude=40.4
        // Parking 2: http://localhost:8080/p4-servidor/ParkingServer?action=createParking&name=ParkingNorte&address=AvdaBurgos20&spaces=20&maxReservationsPerDay=2&longitude=-3.67&latitude=40.48
        // Parking 3: http://localhost:8080/p4-servidor/ParkingServer?action=createParking&name=ParkingSur&address=CalleLeganes5&spaces=100&maxReservationsPerDay=20&longitude=-3.762&latitude=40.32
        // Parking 4: http://localhost:8080/p4-servidor/ParkingServer?action=createParking&name=ParkingCentro&address=CalleMayor1&spaces=50&maxReservationsPerDay=5&longitude=-3.70&latitude=40.41
	}
	
	//find parking
	@GetMapping(value = "/p4-servidor/ParkingServer", params = "action=findParking")
	public Parking findParking(
			@RequestParam("parkingId") Long parkingId) 
	{
		return ps.findParking(parkingId);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findParking&parkingId=2
	}
	
	//find parking with reservations
	@GetMapping(value = "/p4-servidor/ParkingServer", params = "action=findParkingWithReservation")
	public Parking findParkingWithReservation(
			@RequestParam("parkingId") Long parkingId) 
	{
		return ps.findParkingWithReservation(parkingId);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findParkingWithReservation&parkingId=1 
	}
	
	//find all parkings
	@GetMapping(value = "/p4-servidor/ParkingServer", params = "action=findAllParkings")
	public List<Parking> findAllParkings() 
	{
		return ps.findAllParkings();
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findAllParkings
	}
	
	//get available spaces
	@GetMapping(value = "/p4-servidor/ParkingServer", params = "action=getAvailableSpaces")
	public int getAvailableSpaces(
			@RequestParam("parkingId") Long parkingId,
			@RequestParam("date") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam("operationDate") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate operationDate)
	{
		return ps.getAvailableSpaces(parkingId, date, operationDate);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=getAvailableSpaces&parkingId=1&date=2026-06-15&operationDate=2026-05-25
	}
	
	//reserve
	@GetMapping(value = "/p4-servidor/ParkingServer", params = "action=reserve")
	public Reservation reserve(
			@RequestParam("parkingId") Long parkingId,
			@RequestParam("licensePlate") String licensePlate,
			@RequestParam("date") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam("operationDate") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate operationDate)
	{
		return ps.reserve(parkingId, licensePlate, date, operationDate);
	     //PRUEBA: 
		 // Reserva 1: http://localhost:8080/p4-servidor/ParkingServer?action=reserve&parkingId=1&licensePlate=1234BBB&date=2026-06-15&operationDate=2026-05-25
		 // Reserva 2: http://localhost:8080/p4-servidor/ParkingServer?action=reserve&parkingId=1&licensePlate=5678CCC&date=2026-06-15&operationDate=2026-05-25 
	}
	
	//find by parking id
	@GetMapping(value = "/p4-servidor/ParkingServer", params = "action=findByParkingId")
	public List<Reservation> findByParkingId(
			@RequestParam("parkingId") Long parkingId) 
	{
		return ps.findByParkingId(parkingId);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findByParkingId&parkingId=1
	}
	
	//find by parking id and date
	@GetMapping(value = "/p4-servidor/ParkingServer", params = "action=findByParkingIdAndDate")
	public List<Reservation> findReservationsByparkingIdAndDate(
			@RequestParam("parkingId") Long parkingId,
			@RequestParam("date") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) 
	{
		return ps.findReservationsByparkingIdAndDate(parkingId, date);
		//PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findReservationsByparkingIdAndDate&parkingId=1&date=2026-06-15
	}
	
	//cancel reservation
	@GetMapping(value = "/p4-servidor/ParkingServer", params = "action=cancelReservation")
	public void cancelReservation(
			@RequestParam("reservationId") Long reservationId,
			@RequestParam("parkingId") Long parkingId,
			@RequestParam("operationDate") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate operationDate)
	{
		ps.cancelReservation(reservationId, parkingId, operationDate);

		//PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=cancelReservation&reservationId=1&parkingId=1&operationDate=2026-05-25
	}

}
