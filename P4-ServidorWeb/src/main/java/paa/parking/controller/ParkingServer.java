package paa.parking.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;


import paa.parking.business.*;
import paa.parking.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

// Añadir la anotacion para configurar la ruta o usar web.xml
@WebServlet("/ParkingServer")
public class ParkingServer extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    // Seria mejor declarar el EntityManagerFactory o inyectarlo 
    private IParkingService parkingService;
    private ObjectMapper objectMapper;

    // =========================================================
    // ========================= INIT ==========================
    // =========================================================

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        parkingService = new ParkingService(); // Seria mejor pasarle el EntityManagerFactory/inyectar
        objectMapper = createObjectMapper();
    }
    
    // =========================================================
    // ======================== doGet ==========================
    // =========================================================

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String action = request.getParameter("action");
            if (!isSupportedAction(action)) {
                throw new IllegalArgumentException("Acción no soportada: " + action);
            }

            switch (action) 
            {
                case "createParking":  // una operación de modificacion deberia ser tipo POST
                    createParking(request, response);
                    break;
                case "findParking":
                	findParking(request, response);
                	break;
                case "findParkingWithReservations":  
                	findParkingWithReservations(request, response);
                    break;
                case "findAllParkings":
                	findAllParkings(request, response);
                	break;
                case "getAvailableSpaces":
                	getAvailableSpaces(request, response);
	            	break;
	            case "reserve":  
	            	reserve(request, response);
	                break; 
	            case "findByParkingId":  
	            	findByParkingId(request, response);
	                break;
	            case "cancelReservation":
	            	cancelReservation(request, response);
	            	break;
                case "findReservationsByparkingIdAndDate":
                	findReservationsByparkingIdAndDate(request, response);
	            	break;
            }

        } catch (IllegalArgumentException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage()); //codigo 400
        } catch (Exception e) {
            sendError(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, //codigo 500
                    "Error procesando el servicio: " + e.getMessage()
            );
        }
    }

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                doGet(request,response);
    }
    // =========================================================
    // ===================== ACTION METHODS ====================
    // =========================================================

    private void createParking(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Parking parking = parkingService.createParking(
                requiredParam(req, "name"),
                requiredParam(req, "address"),
                getIntParam(req, "spaces"),
                getIntParam(req, "maxReservationsPerDay"),
                getDoubleParam(req, "longitude"),
                getDoubleParam(req, "latitude")
        );
        writeJson(res, parking);
        //PRUEBA: 
        // Parking 1: http://localhost:8080/p4-servidor/ParkingServer?action=createParking&name=ParkingCentro&address=CalleMayor1&spaces=100&maxReservationsPerDay=10&longitude=40.4&latitude=-3.7
        // Parking 2: http://localhost:8080/p4-servidor/ParkingServer?action=createParking&name=ParkingNorte&address=AvdaBurgos20&spaces=20&maxReservationsPerDay=2&longitude=40.48&latitude=-3.67
        // Parking 3: http://localhost:8080/p4-servidor/ParkingServer?action=createParking&name=ParkingSur&address=CalleLeganes5&spaces=100&maxReservationsPerDay=20&longitude=40.32&latitude=-3.76
        // Parking 4: http://localhost:8080/p4-servidor/ParkingServer?action=createParking&name=ParkingCentro&address=CalleMayor1&spaces=50&maxReservationsPerDay=5&longitude=40.41&latitude=-3.70
    }



	private void cancelReservation(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		long reservationId = getLongParam(request, "reservationId");
		parkingService.cancelReservation(
				 reservationId,
				 getLongParam(request, "parkingId"),
				 LocalDate.now());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"Reserva " + reservationId + " cancelada correctamente\"}");
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=cancelReservation&reservationId=1&parkingId=1
	}
	
    private void findReservationsByparkingIdAndDate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
    	List<Reservation> lista_reservas = parkingService.findReservationsByparkingIdAndDate(
				 getLongParam(request, "parkingId"),
				 LocalDate.parse(requiredParam(request, "date"), ISO_DATE));
		 writeJson(response, lista_reservas);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findReservationsByparkingIdAndDate&parkingId=1&date=2026-06-15
	}
	
	private void findByParkingId(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		List<Reservation> lista_reservas = parkingService.findByParkingId(
				 getLongParam(request, "parkingId"));
		 writeJson(response, lista_reservas);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findByParkingId&parkingId=1
	}

	private void reserve(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		Reservation reservation = parkingService.reserve(
				 getLongParam(request, "parkingId"),
				 requiredParam(request, "licensePlate"),
				 LocalDate.parse(requiredParam(request, "date"), ISO_DATE),
				 LocalDate.now());
		 writeJson(response, reservation);
	     //PRUEBA: 
		 // Reserva 1: http://localhost:8080/p4-servidor/ParkingServer?action=reserve&parkingId=1&licensePlate=1234BBB&date=2026-06-15
		 // Reserva 2: http://localhost:8080/p4-servidor/ParkingServer?action=reserve&parkingId=1&licensePlate=5678CCC&date=2026-06-15
	}

	private void getAvailableSpaces(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		int spaces = parkingService.getAvailableSpaces(
				 getLongParam(request, "parkingId"),
				 LocalDate.parse(requiredParam(request, "date"), ISO_DATE),
				 LocalDate.now());
		 writeJson(response, spaces);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=getAvailableSpaces&parkingId=1&date=2026-06-15
	}

	private void findAllParkings(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		List<Parking> lista_parkings = parkingService.findAllParkings();
		 writeJson(response, lista_parkings);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findAllParkings
	}

	private void findParkingWithReservations(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		Parking parking = parkingService.findParkingWithReservation(
				 getLongParam(request, "parkingId"));
		 writeJson(response, parking);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findParkingWithReservations&parkingId=1 
	}

	private void findParking(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		 Parking parking = parkingService.findParking(
				 getLongParam(request, "parkingId"));
		 writeJson(response, parking);
	     //PRUEBA: http://localhost:8080/p4-servidor/ParkingServer?action=findParking&parkingId=2
	}
  

    // =========================================================
    // ====================== HELPERS ==========================
    // =========================================================

    // El objetivo de los siguientes métodos auxiliares es reutilizar código.
    // ------------------ parámetros ------------------
	
	private String requiredParam(HttpServletRequest req, String nombre)
	{
		String valor = req.getParameter(nombre);
		if (valor == null || valor.isBlank())
		{
			throw new IllegalArgumentException("Falta el parámetro: " + nombre);
		}
		return valor;
	}
	
	private int getIntParam(HttpServletRequest req, String nombre)
	{
		return Integer.parseInt(requiredParam(req, nombre));
	}
	
	private double getDoubleParam(HttpServletRequest req, String nombre)
	{
		return Double.parseDouble(requiredParam(req, nombre));
	}
	
	private long getLongParam(HttpServletRequest req, String nombre)
	{
		return Long.parseLong(requiredParam(req, nombre));
	}
	
	private boolean isSupportedAction(String action)
	{
		if (action == null)
		{
			return false;
		}
		List<String> validActions = List.of("createParking", "findParking", "findParkingWithReservations", 
                							"findAllParkings", "getAvailableSpaces", "reserve", "findByParkingId", 
                							"cancelReservation", "findReservationsByparkingIdAndDate");
		return validActions.contains(action);
		//PRUEBA DE QUE NO ACEPTA ACCIONES EXTRA: http://localhost:8080/p4-servidor/ParkingServer?action=borrarTodo 
	}
	
	private ObjectMapper createObjectMapper()
	{
		ObjectMapper mapper = new ObjectMapper();
		//Esto permite que entienda las fechas LocalDate de Java 8
		mapper.findAndRegisterModules();
		mapper.registerModule(new com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module());
		return mapper;
	}
	
	
	private void writeJson(HttpServletResponse res, Object data) throws IOException
	{
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		objectMapper.writeValue(res.getWriter(), data);
	}
	
	private void sendError(HttpServletResponse res, int code, String message) throws IOException
	{
		res.setStatus(code);
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		res.getWriter().write("{\"error\": \"" + message + "\"}");
	}

}
