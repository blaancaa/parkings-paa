package paa.parking.business;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import paa.parking.model.Parking;
import paa.parking.model.Reservation;

//RemoteParkingService deja de usar JPA (em, dao, ...), para usar HttpClient

public class RemoteParkingService implements IParkingService {

	private static String BASE_URL = "http://localhost:8080/p4-servidor/ParkingServer";
	private HttpClient cliente;
	private final ObjectMapper mapper;
	
	public RemoteParkingService()
	{
		//inicializamos el cliente HTTP en Java 21
		this.cliente = HttpClient.newHttpClient();
		
		//inicializamos Jackson para traducir JSON <-> Objetos
		this.mapper = new ObjectMapper();
		this.mapper.findAndRegisterModules(); //Para LocalDate
		this.mapper.registerModule(new com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module());
	}
	
	@Override
	public Parking createParking(String name, String address, int spaces, int maxDailyReservations, double longitude, double latitude) 
	{
		
		String parName = URLEncoder.encode(name, StandardCharsets.UTF_8);
		String parAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
		String url = String.format("%s?action=createParking&name=%s&address=%s&spaces=%s&maxReservationsPerDay=%s&longitude=%s&latitude=%s", 
				                   BASE_URL, parName, parAddress, spaces, maxDailyReservations, longitude, latitude);
		String json = peticionServicio(url);
		
		try
		{
			return mapper.readValue(json, Parking.class);
					
		} catch (Exception e) {
			throw new ParkingServiceException(e.getMessage());
		}	
			
	}

	@Override
	public Parking findParking(Long parkingId) {
		
		String url = String.format("%s?action=findParking&parkingId=%s", 
				   				   BASE_URL, parkingId);
		String json = peticionServicio(url);
		
		try
		{
			return mapper.readValue(json, Parking.class);
					
		} catch (Exception e) {
			throw new ParkingServiceException(e.getMessage());
		}
	}

	@Override
	public Parking findParkingWithReservation(Long parkingId) {
		
		String url = String.format("%s?action=findParkingWithReservation&parkingId=%s", 
								   BASE_URL, parkingId);
		String json = peticionServicio(url);
		
		try
		{
			return mapper.readValue(json, Parking.class);
					
		} catch (Exception e) {
			throw new ParkingServiceException(e.getMessage());
		}
	}

	@Override
	public List<Parking> findAllParkings() {
		String url = String.format("%s?action=findAllParkings", BASE_URL);
		String json = peticionServicio(url);
		
		try
		{
			return mapper.readValue(json, new TypeReference<List<Parking>>() {});
				
		} catch (Exception e) {
			throw new ParkingServiceException(e.getMessage());
		}
	}

	@Override
	public int getAvailableSpaces(Long parkingId, LocalDate date, LocalDate operationDate) {
		
		String url = String.format("%s?action=getAvailableSpaces&parkingId=%s&date=%s&operationDate=%s", 
				                   BASE_URL, parkingId, date.toString(), operationDate.toString());
		String json = peticionServicio(url);
		
		try
		{
			return mapper.readValue(json, Integer.class);
					
		} catch (Exception e) {
			throw new ParkingServiceException(e.getMessage());
		}
		
	}

	@Override
	public Reservation reserve(Long parkingId, String licensePlate, LocalDate date, LocalDate operationDate) {
		
		String parMatricula = URLEncoder.encode(licensePlate, StandardCharsets.UTF_8);
		String url = String.format("%s?action=reserve&parkingId=%s&licensePlate=%s&date=%s&operationDate=%s", 
				                   BASE_URL, parkingId, parMatricula, date.toString(), operationDate.toString());
		String json = peticionServicio(url);
		
		try
		{
			return mapper.readValue(json, Reservation.class);
						
		} catch (Exception e) {
			throw new ParkingServiceException(e.getMessage());
		}
	}

	@Override
	public List<Reservation> findByParkingId(Long parkingId) {
		
		String url = String.format("%s?action=findByParkingId&parkingId=%s", 
				   				   BASE_URL, parkingId);
		String json = peticionServicio(url);
		
		try
		{
			return mapper.readValue(json, new TypeReference<List<Reservation>>() {});
						
		} catch (Exception e) {
			throw new ParkingServiceException(e.getMessage());
		}
	}

	@Override
	public List<Reservation> findByParkingIdAndDate(Long parkingId, LocalDate date) {
		
		String url = String.format("%s?action=findByParkingIdAndDate&parkingId=%s&date=%s", 
					   			   BASE_URL, parkingId, date.toString());
		String json = peticionServicio(url);
		
		try
		{
			return mapper.readValue(json, new TypeReference<List<Reservation>>() {});
						
		} catch (Exception e) {
			throw new ParkingServiceException(e.getMessage());
		}
	}

	@Override
	public void cancelReservation(Long reservationId, Long parkingId, LocalDate operationDate) {

		String url = String.format("%s?action=cancelReservation&reservationId=%s&parkingId=%s&operationDate=%s", 
                				   BASE_URL, reservationId, parkingId, operationDate.toString());

		peticionServicio(url); //como es void no hacemos nada mas
	}
	
	
	private String peticionServicio(String url) throws ParkingServiceException {

	    try {
	        // Cliente HTTP moderno (thread-safe, reutilizable)
	        cliente = HttpClient.newHttpClient();

	        // Construcción de la petición
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(url))
	                .GET()
	                .build();

	        // Envío de la petición
	        HttpResponse<String> response = cliente.send(
	                request,
	                HttpResponse.BodyHandlers.ofString()
	        );

	        int status = response.statusCode();
	        String body = response.body();

	        // Gestión de códigos HTTP
	        if (status >= 200 && status < 300) {
	            return body;

	        } else if (status == 400) {
	            throw new ParkingServiceException("Error 400: " + body);

	        } else if (status == 404) {
	            throw new ParkingServiceException("Error 404: recurso no encontrado");

	        } else if (status >= 500) {
	            throw new ParkingServiceException("Error servidor (" + status + "): " + body);

	        } else {
	            throw new ParkingServiceException("Error HTTP " + status + ": " + body);
	        }

	    } catch (ParkingServiceException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new ParkingServiceException("Error en la conexión con URL: " + url, e);
	    }
	}

}
