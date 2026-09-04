package paa.parking.business;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import paa.parking.dao.ParkingJpaDAO;
import paa.parking.dao.ReservationJpaDAO;
import paa.parking.model.Parking;
import paa.parking.model.Reservation;

public class ParkingService implements IParkingService {

	protected static String PERSISTENCE_UNIT_NAME = "paa";
	protected EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	
	@Override
	public Parking createParking(String name, String address, int spaces, int maxDailyReservations, double longitude, double latitude) 
	{
		//Crea un nuevo aparcamiento con los parámetros indicados y lodevuelve
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		Parking parking_creado = null; 
		
		//como vamos a modificar usamos tx
		try {
			tx.begin();
			ParkingJpaDAO pDAO = new ParkingJpaDAO(em);
			//ID null porque se genera automáticamente
			parking_creado = new Parking(null, name, address, spaces, maxDailyReservations ,longitude, latitude);
			pDAO.create(parking_creado); //Persistencia
			tx.commit();
		} catch(Exception e) {
			if(tx.isActive()){
				tx.rollback();
			} throw new ParkingServiceException("Error al crear el aparcamiento: " + e.getMessage());
		} finally {
			em.close();
		}
		
		return parking_creado;
	}

	@Override
	public Parking findParking(Long parkingId) {
		//Busca el aparcamiento con el código indicado y lo devuelve, sin
		//incluir la lista de reservas asociadas al mismo. En caso de que no
		//exista devuelve null.
		if (parkingId == null) throw new ParkingServiceException("El id del parking es nulo");
		
		EntityManager em = emf.createEntityManager();
		Parking parking_encontrado = null;
		
		//como es operacion de lectura no usamos tx
		try
		{
			ParkingJpaDAO pDAO = new ParkingJpaDAO(em);
			parking_encontrado = pDAO.find(parkingId);
		} catch (Exception e) {
			throw new ParkingServiceException("Error al buscar el parking por su id: " + e.getMessage());
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
		
		return parking_encontrado;
	}

	@Override
	public Parking findParkingWithReservation(Long parkingId) {
		// Mismo comportamiento que el método anterior, con la diferencia
		// de que ahora devuelve el parking con su lista de reservas
		// asociadas
		
		if (parkingId == null) throw new ParkingServiceException("El id del parking es nulo");
		
		EntityManager em = emf.createEntityManager();
		Parking parking_encontrado = null;
		
		//como es operacion de lectura no usamos tx
		try
		{
			ParkingJpaDAO pDAO = new ParkingJpaDAO(em);
			parking_encontrado = pDAO.find(parkingId);
			
			//Forzamos la carga de la lista de reservas
			if(parking_encontrado != null && parking_encontrado.getReservations() != null)
			{
				//Con llamar a size se cargan los datos
				parking_encontrado.getReservations().size(); 
			}
		} catch (Exception e) {
			throw new ParkingServiceException("Error al buscar el parking con reservas: " + e.getMessage());
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
		
		return parking_encontrado;
	}

	@Override
	public List<Parking> findAllParkings() {
		//Devuelve una lista de todos los aparcamientos existentes.
		//Este método se utiliza principalmente para mostrar la lista con la
		//información de los aparcamientos. Los objetos devueltos no
		//incluyen la lista de reservas asociadas.
		
		EntityManager em = emf.createEntityManager();
		List<Parking> lista_parking = null;
		
		//como es operacion de lectura no usamos tx
		try
		{
			ParkingJpaDAO pDAO = new ParkingJpaDAO(em);
			lista_parking = pDAO.findAll();
		} catch (Exception e) {
			throw new ParkingServiceException("Error al recuperar la lista de aparcamientos: " + e.getMessage());
		}finally {
			if (em != null && em.isOpen())
				em.close();
		}
		
		return lista_parking;
	}

	@Override
	public int getAvailableSpaces(Long parkingId, LocalDate date, LocalDate operationDate) {
		// Devuelve el número de plazas libres para reservar en el
		// aparcamiento y fecha indicados, con las siguientes reglas:
			// 1. El aparcamiento debe existir;
			// 2. La fecha debe ser correcta, no puede ser anterior a la
			//    fecha en la que se realiza la operación (operationDate)
		
		if(parkingId == null || date == null || operationDate == null) //1.
		{
			throw new ParkingServiceException("El aparcamiento debe existir.");
		}
			
		if(date.isBefore(operationDate)) //2.
		{
			throw new ParkingServiceException("No se pueden realizar reservas con fecha anterior a la de la operacion.");
		}
		
		EntityManager em = emf.createEntityManager();
		Parking parking_encontrado = null;
		
		//como es operacion de lectura no usamos tx
		try
		{
			ParkingJpaDAO pDAO = new ParkingJpaDAO(em);
			parking_encontrado = pDAO.find(parkingId);
			if (parking_encontrado == null)
			{
				throw new ParkingServiceException("El aparcamiento con ID " + parkingId + " no existe.");
			}
			
			ReservationJpaDAO rDAO = new ReservationJpaDAO(em);
			long reservasOcupadas = rDAO.getReservasOcupadas(parkingId, date);
			
			int disponibles = parking_encontrado.getMaxReservationsPerDay() - (int) reservasOcupadas;
			
			return Math.max(0, disponibles);//por si hay error previo y da negativo
		}finally {
			if (em != null && em.isOpen())
				em.close();
		}
		
	}

	@Override
	public Reservation reserve(Long parkingId, String licensePlate, LocalDate date, LocalDate operationDate) {
		// Crea una nueva reserva con los datos indicados, siempre y cuando
		// se cumplan las siguientes reglas de negocio:

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
		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		Reservation r = null;
		Parking p = null;
		
		//como vamos a modificar usamos tx
		try {
			ReservationJpaDAO rDAO = new ReservationJpaDAO(em);
			ParkingJpaDAO pDAO = new ParkingJpaDAO(em);
			
			tx.begin();
			p = pDAO.find(parkingId);
			
			if(rDAO.existeReserva(licensePlate, date, parkingId)) //3.
			{
				throw new ParkingServiceException("Un mismo vehículo no puede tener más de una reserva el\r\n"
						+ "mismo día en un aparcamiento.");
			}
			
			long totalReservasCoche = rDAO.getReservasTotalesCoche(licensePlate, date);
			if(totalReservasCoche >= MAX_RESERVATIONS_ANYWHERE) //4.
			{
				throw new ParkingServiceException("Un mismo vehículo no puede tener más de\r\n"
				+ "MAX_RESERVATIONS_ANYWHERE reservas en total entre todos los aparcamientos.");
			}
			
			
			//ID null porque se genera automáticamente
			r = new Reservation(null, licensePlate, date, p);
			rDAO.create(r); //Persistencia
			tx.commit();
			
		} catch(Exception e) {
			if(tx.isActive()){
				tx.rollback();
			} throw new ParkingServiceException("Error al reservar el aparcamiento: " + e.getMessage());
		} finally {
			em.close();
		}
		
		return r;
	}

	@Override
	public List<Reservation> findByParkingId(Long parkingId) {
		// Devuelve una lista con todas las plazas reservadas perteneciente a
		// un parking.
		
		if (parkingId == null) throw new ParkingServiceException("El id del parking es nulo");
		
		EntityManager em = emf.createEntityManager();
		List<Reservation> lista_reservas = null;
		
		//como es operacion de lectura no usamos tx
		try
		{
			ReservationJpaDAO rDAO = new ReservationJpaDAO(em);
			lista_reservas = rDAO.listaReservasParking(parkingId);
		} catch (Exception e) {
			throw new ParkingServiceException("Error al buscar reservas por parking: " + e.getMessage());
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

		return lista_reservas;
	}

	@Override
	public List<Reservation> findReservationsByparkingIdAndDate(Long parkingId, LocalDate date) {
		// Devuelve una lista con todas las plazas reservadas pertenecientes a
		// un parking en una fecha determinada

		if (parkingId == null || date == null) throw new ParkingServiceException("El id del parking o fecha es nulo");
		
		EntityManager em = emf.createEntityManager();
		List<Reservation> lista_reservas = null;
		
		//como es operacion de lectura no usamos tx
		try
		{
			ReservationJpaDAO rDAO = new ReservationJpaDAO(em);
			lista_reservas = rDAO.listaReservasParking(parkingId, date);
		} catch (Exception e) {
			throw new ParkingServiceException("Error al buscar reservas por parking: " + e.getMessage());
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

		return lista_reservas;
	}

	@Override
	public void cancelReservation(Long reservationId, Long parkingId, LocalDate operationDate) {
		// Esta operación cancela una reserva, eliminándola del aparcamiento
		// y liberando sitio. Solo es posible si la reserva se encuentra en el
		// aparcamiento en la fecha en la que se realiza la operación, o
		// futuras. Si alguno de los parámetros es nulo, o la reserva no existe
		// o se viola la condición anterior, no se eliminará del sistema y se
		// lanzará una excepción de tipo ParkingServiceException con un
		// mensaje explicativo.
		
		if (parkingId == null || reservationId == null || operationDate == null) throw new ParkingServiceException("El id del parking o de la reserva, o la fecha, son nulos");
		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		//como vamos a modificar usamos tx
		try {
			
			ReservationJpaDAO rDAO = new ReservationJpaDAO(em);
			ParkingJpaDAO pDAO = new ParkingJpaDAO(em);
			
			Reservation res_borrar = rDAO.find(reservationId);
			
			if (res_borrar == null)
			{
				throw new ParkingServiceException("Reserva no encontrada");
			}
			
			if(!res_borrar.getParking().getId().equals(parkingId))
			{
				throw new ParkingServiceException("La reserva no pertenece al parking indicado.");
			}

			if (res_borrar.getDate().isBefore(operationDate)) 
			{
				throw new ParkingServiceException("Ya es tarde para borrar la reserva");
			}
			
			tx.begin();
			rDAO.delete(res_borrar);
			tx.commit();

		} catch(Exception e) {
			if(tx.isActive()){
				tx.rollback();
			} throw new ParkingServiceException("Error al borrar la reserva: " + e.getMessage());
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}
	}

}
