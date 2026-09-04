package paa.parking.business;

public class ParkingServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public ParkingServiceException(String message) {
        super(message);
    }

	public ParkingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

	public ParkingServiceException(Throwable cause) {
        super(cause);
    }
}
