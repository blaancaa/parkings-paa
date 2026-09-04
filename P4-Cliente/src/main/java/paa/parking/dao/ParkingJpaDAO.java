package paa.parking.dao;

import jakarta.persistence.EntityManager;
import paa.parking.model.Parking;

public class ParkingJpaDAO extends JPADAO<Parking, Long>{
	public ParkingJpaDAO(EntityManager em)
	{
		super(em, Parking.class);
	}

}
