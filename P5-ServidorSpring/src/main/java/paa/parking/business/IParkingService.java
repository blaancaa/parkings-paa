package paa.parking.business;
import java.time.LocalDate;
import java.util.List;

import paa.parking.model.Parking;
import paa.parking.model.Reservation;

public interface IParkingService {

    /**
     * The maximum number of reservations a single license plate can hold across all parkings.
     */
    int MAX_RESERVATIONS_ANYWHERE   = 5;     // A given licencePlate cannot have more than MAX_RESERVATIONS_ANYWHERE reservations pending used across all parkings at any time

    /**
     * Registers a new parking in the system.
     * 
     * This method will create a new parking and insert it into the database,
     * provided that all the received parameters meet their specification and
     * the following rules are also observed.
     * <ul>
     *   <li>Any field is null</li>
     *   <li> the number of spaces must be positive. </li>
     *   <li> the max daily reservation cannot be greater than spaces, but greater than 0</li>
     * </ul>
     *
     * @param name. The name of the Parking. Must be not null or blank.
     * @param address. The address of the Parking. Must be not null or blank.
     * @param spaces. Number of spaces the Parking has. Must be greater than zero.
     * @param maxDailyReservations number of spaces that can be reserved on the same day. Must be 0 < maxDailyReservations <= spaces.
     * @param longitude geographical longitude coordinate of the Parking. Must not be null or blank.
     * @param latitude geographical latitude coordinate of the Parking. Must not be null or blank.
     * @return a new Parking object inserted into the database.
     * @throws ParkingServiceException
     */
	Parking createParking(String name, String address, int spaces, int maxDailyReservations, double longitude, double latitude);

    /**
     * Gets a parking by its unique identifier.
     *
     * @param parkingId id to look for
     * @return the Parking object with the specified id, or {@code null} if it does not exist.
     */
	Parking findParking(Long parkingId);

    /**
     * Get a parking by its unique identifier. The returned Parking has
     * access to its reservations.
     *
     * @param parkingId id to look for.
     * @return the Parking object with the specified code or {@code null} if
     * does not exist.
     */
    Parking findParkingWithReservation(Long parkingId);


    /**
     * Returns the list of all Parkings stored in the database
     *
     * @return list of all parking
     */
	List<Parking> findAllParkings();
	
    /**
     * Returns the number of available spaces in the specified parking on the given date.
     *
     * @param parkingId the Id of the parking where spaces are searched.
     * @param date the date on which availability is sought.
	 * @param operationDate the first date from which the search begins.
     * @return number of available spaces.
     */
	int getAvailableSpaces(Long parkingId, LocalDate date, LocalDate operationDate);

    /**
     * Creates a new reservation with the specified data, provided that
     * the following rules conditions are met.
     * 
     * <ul>
     *   <li> The date cannot be before current day.</li>
     *   <li> There must be enough free spaces in the parking.</li>
     * <li> The number of active reservation for {@code licensePlate} must not
     *      exceed {@link #MAX_RESERVATIONS_ANYWHERE}</li>
     * </ul>
     *
     * If any of these rules are violated, or if any field is null, a
     * {@code ParkingServiceException} will be thrown, and the reservation will
     * not be created.
     *
     * @param parkingId the id of the parking where the reservation will take place.
     * @param licensePlate  the licensePlate associated to the reservation.
     * @param date The date where the resevation will take place.
	 * @param operationDate the first date from which the search begins.
     * @return a new Reservation
     * @throws ParkingServiceException
     */
	Reservation reserve(Long parkingId, String licensePlate, LocalDate date, LocalDate operationDate);

    /**
     * Returns all the reservations belonging to the parking with the given id.
     * If parkingId is null, or does not exists, a {@code ParkingServiceException} is thrown.
     * 
     * @param parkingId the Id of the parking to search for
     * @return A list with all the reservations of the parking with parkingId.
     * @throws ParkingServiceException
     */
	List<Reservation> findByParkingId(Long parkingId);

    /**
     * Returns all the reservations belonging to the parking with the given Id on a specific date.
     * If parkingId is null, or does not exists, a {@code ParkingServiceException} is thrown.
     * 
     * @param parkingId the Id of the parking to search for
     * @param date the date used to filter reservations.
     * @return A list with all the reservations of the parking with the given id
     * on that take place on the given date.
     * @throws ParkingServiceException
     */
	List<Reservation> findReservationsByparkingIdAndDate(Long parkingId, LocalDate date );

    /**
     * Cancels the reservation with the given Id if it belongs to the specified parking.
     * A {@code ParkingServiceException} will be thrown in any of the following situations:
     * <ul>
     *   <li>parkingId or reservationId are null.</li>
     *   <li>The parkingId or the reservationId don't bellong to any Parking or Reservation respectively.</li>
     *   <li>The reservation does not belong to the Parking.</li>
     * </ul>
     *
     * @param parkingId the id of the parking where the reservation will be search.
     * @param reservationId the id of the reservation to be cancelled.
     * @throws ParkingServiceException
     */

	void cancelReservation(Long reservationId, Long parkingId, LocalDate operationDate);
}
