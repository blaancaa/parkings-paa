package paa.parking.util.impl;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * A waypoint that also has a color and a label
 * 
 * @author Martin Steiger
 */
public class ParkingWaypoint extends DefaultWaypoint {
	private final int available;
	private final int total;
	private final Long parkingId;

	/**
	 *
	 * @param parkingId
	 * @param longitude
	 * @param latitude
	 */
	public ParkingWaypoint(int available, int total, Long parkingId, double longitude, double latitude) { // GeoPosition
																											// coord) {
		super(new GeoPosition(latitude, longitude));
		this.available = available;
		this.total = total;
		this.parkingId = parkingId;
	}

	public int getAvailable() {
		return available;
	}

	public int getTotal() {
		return total;
	}

	public Long getParkingId() {
		return parkingId;
	}

}
