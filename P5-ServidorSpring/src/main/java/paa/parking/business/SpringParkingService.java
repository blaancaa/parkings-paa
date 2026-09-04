package paa.parking.business;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import paa.parking.model.Parking;
import paa.parking.model.Reservation;
import paa.parking.repository.IParkingRepository;
import paa.parking.repository.IReservationRepository;

@Service
@Transactional
public class SpringParkingService implements IParkingService
{
	@Autowired
	 private IParkingRepository parkingRepository;
	
	@Autowired
	 private IReservationRepository reservationRepository;

	@Override
	public Parking createParking(String name, String address, int spaces, int maxReservationsPerDay, double longitude, double latitude) 
	{
		// Validaciones
		Parking parking = new Parking (null, name, address, spaces, maxReservationsPerDay, longitude, latitude);
		return parkingRepository.save(parking);
	}

	@Override
	public Parking findParking(Long parkingId) 
	{
		if (parkingId == null) throw new ParkingServiceException("El id del parking es nulo");
		
		Optional<Parking> parking_encontrado = parkingRepository.findById(parkingId);
		return parking_encontrado.orElse(null);
	}

	@Override
	public Parking findParkingWithReservation(Long parkingId) 
	{
		Parking parking = null;
		if (parkingId == null) throw new ParkingServiceException("El id del parking es nulo");
		
		Optional<Parking> parking_encontrado = parkingRepository.findById(parkingId);
		if (parking_encontrado.isPresent())
		{
			parking = parking_encontrado.get();
			if (parking.getReservations() != null)
			{
				parking.getReservations().size(); //Para evitar problemas de LazyInitialitation
			}
		}
		else
		{
			throw new ParkingServiceException("El aparcamiento con ID " + parkingId + " no tiene reservas.");
		}
		
		return parking;
	}

	@Override
	public List<Parking> findAllParkings() 
	{
		return parkingRepository.findAll();
	}

	@Override
	public int getAvailableSpaces(Long parkingId, LocalDate date, LocalDate operationDate) 
	{
		if(parkingId == null || date == null || operationDate == null) //1.
		{
			throw new ParkingServiceException("El aparcamiento debe existir.");
		}
			
		if(date.isBefore(operationDate)) //2.
		{
			throw new ParkingServiceException("No se pueden realizar reservas con fecha anterior a la de la operacion.");
		}
		
		Parking parking = findParking(parkingId);
		
		long reservasOcupadas = reservationRepository.ResOcupadas_ByParkingIdAndDate(parkingId, date);
		int disponibles = parking.getMaxReservationsPerDay() - (int) reservasOcupadas;
		
		return Math.max(0, disponibles);//por si hay error previo y da negativo
		
	}

	@Override
	public Reservation reserve(Long parkingId, String licensePlate, LocalDate date, LocalDate operationDate) 
	{
		if(parkingId == null || licensePlate == null || date == null || operationDate == null) 
		{
	        throw new ParkingServiceException("Ningún parámetro puede ser nulo.");
	    }
		
		if(date.isBefore(operationDate)) //1.
		{
			throw new ParkingServiceException("La fecha de reserva no puede ser anterior a la fecha en la\r\n"
					+ "que se realiza la operación.");
		}
		
		if(getAvailableSpaces(parkingId, date, operationDate) <= 0) //2.
		{
			throw new ParkingServiceException("Debe haber un hueco disponible en el aparcamiento.");
		}
		
		Parking parking = findParking(parkingId);
		
		if(reservationRepository.existsByLicensePlateAndDateAndParkingId(licensePlate, date, parkingId)) //3.
		{
			throw new ParkingServiceException("Un mismo vehículo no puede tener más de una reserva el\r\n"
					+ "mismo día en un aparcamiento.");
		}
		
		long totalReservasCoche = reservationRepository.totalReservas_ByLicensePlateAndDate(licensePlate, date);
		if(totalReservasCoche >= MAX_RESERVATIONS_ANYWHERE) //4.
		{
			throw new ParkingServiceException("Un mismo vehículo no puede tener más de\r\n"
			+ MAX_RESERVATIONS_ANYWHERE + " reservas en total entre todos los aparcamientos.");
		}
		
		Reservation reserva = new Reservation(null, licensePlate, date, parking);
		return reservationRepository.save(reserva);
	}

	@Override
	public List<Reservation> findByParkingId(Long parkingId) 
	{
		if (parkingId == null) throw new ParkingServiceException("El id del parking es nulo");
		
		return reservationRepository.findByParkingId(parkingId);
	}

	@Override
	public List<Reservation> findReservationsByparkingIdAndDate(Long parkingId, LocalDate date) 
	{
		if (parkingId == null || date == null) throw new ParkingServiceException("El id del parking o fecha es nulo");
		
		return reservationRepository.findByParkingIdAndDate(parkingId, date);
	}

	@Override
	public void cancelReservation(Long reservationId, Long parkingId, LocalDate operationDate) 
	{
		if (parkingId == null || reservationId == null || operationDate == null) throw new ParkingServiceException("El id del parking o de la reserva, o la fecha, son nulos");
		
		Optional<Reservation> res_encontrada = reservationRepository.findById(reservationId);
		
		if (res_encontrada.isEmpty())
		{
			throw new ParkingServiceException("Reserva no encontrada");
		}
		
		Reservation res_borrar = res_encontrada.get();
		
		if(!res_borrar.getParking().getId().equals(parkingId))
		{
			throw new ParkingServiceException("La reserva no pertenece al parking indicado.");
		}

		if (res_borrar.getDate().isBefore(operationDate)) 
		{
			throw new ParkingServiceException("Ya es tarde para borrar la reserva");
		}
		
		reservationRepository.deleteById(reservationId);
		
	}

}
