package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import paa.parking.model.Reservation;

public class DialogoReserva extends JDialog
{
	public DialogoReserva(Frame owner, Reservation reserva) 
	{
		super(owner, "Datos de la reserva específica", true); //owner es la ventana principal
		
		setLayout(new BorderLayout(10,10));
		{//Center
			JPanel plab = new JPanel(new GridLayout(4, 1, 5, 5));//4 filas, 1 columna y con separacion
			plab.setBorder(BorderFactory.createEmptyBorder(5, 105, 0, 105));
			
			plab.add(new JLabel("Parking: " + reserva.getParking().getName()));
			plab.add(new JLabel("Id: " + reserva.getId()));
			plab.add(new JLabel("Fecha: " + reserva.getDate()));
			plab.add(new JLabel("Matrícula: " + reserva.getLicensePlate()));
			
			add(plab, BorderLayout.CENTER);
		}
		{//South
			JPanel pbot = new JPanel(new FlowLayout(FlowLayout.CENTER)); //para que el boton no ocupe todo el sur
			pbot.setBorder(BorderFactory.createEmptyBorder(3, 0, 5, 0));
			
			pbot.add(Botones.crearCerrarBoton(this));
			add(pbot, BorderLayout.SOUTH);
		}

		pack();
		setLocationRelativeTo(owner);
	}
	
}
