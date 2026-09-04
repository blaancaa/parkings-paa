package paa.parking.presentation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;

import paa.parking.business.IParkingService;
import paa.parking.business.RemoteParkingService;
import paa.parking.model.Parking;
import paa.parking.model.Reservation;

public class Botones
{
	public static JButton crearCerrarBoton(JDialog parent)
	{
		JButton cerrar = new JButton("Cerrar");
		cerrar.addActionListener(e -> parent.dispose());
		return cerrar;
	}
	
	public static JButton crearOkBoton(JDialog parent)
	{
		JButton ok = new JButton("Ok");
		ok.addActionListener(e -> parent.dispose());
		return ok;
	}
	
	public static JButton crearSiguienteBoton(MainJFrame mainJFrame)
	{
		JButton siguiente = new JButton(">");
		siguiente.setFont(siguiente.getFont().deriveFont(10f));//hacemos la fuente mas pequeña
		siguiente.setPreferredSize(new Dimension(35, 23));
		siguiente.setMargin(new java.awt.Insets(0, 0, 0, 0));
		
		siguiente.addActionListener(e -> mainJFrame.actualizarFechaSel(1)); //1 es siguiente
		return siguiente;
	}
	
	public static JButton crearAnteriorBoton(MainJFrame mainJFrame)
	{
		JButton anterior = new JButton("<");
		anterior.setFont(anterior.getFont().deriveFont(10f));//hacemos la fuente mas pequeña
		anterior.setPreferredSize(new Dimension(35, 23));
		anterior.setMargin(new java.awt.Insets(0, 0, 0, 0));
		anterior.addActionListener(e -> mainJFrame.actualizarFechaSel(2)); //2 es anterior
		return anterior;
	}

	public static JButton crearCancelarBoton(JDialog parent) 
	{
		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(e -> parent.dispose());
		return cancelar;
	}

	public static Component crearAñadirParkingBoton(DialogoAñadirParking dialogoReservarParking, IParkingService service, Frame owner) 
	{
		JButton añadirParking = new JButton("Añadir");
		
		añadirParking.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//leemos y convertimos aqui al hacer click
				String name = dialogoReservarParking.getNombre();
				String address = dialogoReservarParking.getDireccion();
				int spaces = Integer.parseInt(dialogoReservarParking.getPlazas());
				int maxDailyReservations = Integer.parseInt(dialogoReservarParking.getMaxReservas());
				double longitude = Double.parseDouble(dialogoReservarParking.getLongitud());
				double latitude = Double.parseDouble(dialogoReservarParking.getLatitud());
				
				service.createParking(name, address, spaces, maxDailyReservations ,longitude, latitude);
				((MainJFrame) owner).actualizarParkings();
				((MainJFrame) owner).actualizarMapa();
				dialogoReservarParking.dispose();
			}
		});
		return añadirParking;
	}

	public static Component crearAñadirReservaBoton(DialogoReservarPlaza dialogoReservarPlaza, IParkingService service, Frame owner) 
	{
		JButton añadirReserva = new JButton("Añadir");
		
		añadirReserva.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//leemos y convertimos aqui al hacer click
				LocalDate dia = dialogoReservarPlaza.getDia();
				String matricula = dialogoReservarPlaza.getTxtMatricula();
				Parking parking = dialogoReservarPlaza.getParking();
				
				service.reserve(parking.getId(), matricula, dia, LocalDate.now());
				((MainJFrame) owner).actualizarListaReservas(false); //no mostramos inf pq se mostraria antes del mapa
				((MainJFrame) owner).actualizarMapa();
				dialogoReservarPlaza.dispose();
			}
		});
		return añadirReserva;
	}

	public static Component crearCancelarReservaBoton(DialogoCancelarReserva dialogoCancelarReserva, IParkingService service, Frame owner) 
	{
		JButton eliminarReserva = new JButton("Eliminar");
		
		eliminarReserva.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//leemos y convertimos aqui al hacer click
				Reservation reserva = dialogoCancelarReserva.getReserva();
				Parking parking = dialogoCancelarReserva.getParking();
				
				service.cancelReservation(reserva.getId(), parking.getId(), LocalDate.now());
				((MainJFrame) owner).actualizarListaReservas(false); //no mostramos inf pq se mostraria antes del mapa
				((MainJFrame) owner).actualizarMapa();
				dialogoCancelarReserva.dispose();
			}
		});
		return eliminarReserva;
	}
	

}
