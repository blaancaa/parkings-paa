package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import paa.parking.model.Parking;

public class DialogoParking extends JDialog
{
	public DialogoParking(Frame owner, Parking parking) 
	{
		super(owner, "Datos de la reserva de plaza de aparcamiento", true); //owner es la ventana principal
		
		setLayout(new BorderLayout(10,10));
		{//Center
			JPanel plab = new JPanel(new GridLayout(2, 1, 5, 5));//2 filas, 1 columna y con separacion
			plab.setBorder(BorderFactory.createEmptyBorder(5, 105, 0, 105));
			
			plab.add(new JLabel("Parking: " + parking.getName()));
			plab.add(new JLabel("Datos reserva: " + parking.getReservations()));
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
