package paa.parking.dao;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import paa.parking.model.Parking;
import paa.parking.model.Reservation;

public class ReservationJpaDAO extends JPADAO<Reservation, Long>{
	public ReservationJpaDAO(EntityManager em)
	{
		super(em, Reservation.class);
	}

	public long getReservasOcupadas(Long parkingId, LocalDate date) {
		TypedQuery<Long> q = em.createQuery(
		    "SELECT COUNT(r) FROM Reservation r WHERE r.parking.id = :pId AND r.date = :d", Long.class);
	    	q.setParameter("pId", parkingId);
	    	q.setParameter("d", date);
	    return q.getSingleResult();
	}
	
	public boolean existeReserva(String licensePlate, LocalDate date, Long parkingId) {
	    TypedQuery<Long> query = em.createQuery(
	        "SELECT COUNT(r) FROM Reservation r WHERE r.licensePlate = :lP AND r.date = :d AND r.parking.id = :pId", Long.class);
	    query.setParameter("lP", licensePlate);
	    query.setParameter("d", date);
	    query.setParameter("pId", parkingId);
	    return query.getSingleResult() > 0;
	}
	
	public long getReservasTotalesCoche(String licensePlate, LocalDate date) {
	    return em.createQuery(
	        "SELECT COUNT(r) FROM Reservation r WHERE r.licensePlate = :lP AND r.date = :d", Long.class)
	        .setParameter("lP", licensePlate)
	        .setParameter("d", date)
	        .getSingleResult();
	}
	
	public List<Reservation> listaReservasParking(Long parkingId) {
	    TypedQuery<Reservation> query = em.createQuery(
	        "SELECT r FROM Reservation r WHERE r.parking.id = :pId", Reservation.class);
	    query.setParameter("pId", parkingId);
	    return query.getResultList();
	}
	
	public List<Reservation> listaReservasParking(Long parkingId, LocalDate date) {
	    TypedQuery<Reservation> query = em.createQuery(
	        "SELECT r FROM Reservation r WHERE r.parking.id = :pId AND r.date = :d", Reservation.class);
	    query.setParameter("pId", parkingId);
	    query.setParameter("d", date);
	    return query.getResultList();
	}
}
