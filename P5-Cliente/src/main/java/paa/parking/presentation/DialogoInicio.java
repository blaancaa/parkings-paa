package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogoInicio  extends JDialog
{
	public DialogoInicio(Frame owner) 
	{
		super(owner, "Acerca de: ", true); //owner es la ventana principal y el mensaje lo que se pone arriba
		
		setLayout(new BorderLayout(3,3));
		{//Center
			JPanel plab = new JPanel(new GridLayout(3, 1, 2, 5));//3 filas, 1 columna y con separacion
			plab.setBorder(BorderFactory.createEmptyBorder(25, 20, 0, 20));
			
			plab.add(new JLabel("Sistema de gestión de Parkings PAA"));
			plab.add(new JLabel("Versión: 2.0"));
			plab.add(new JLabel("Autor: Blanca Loma"));
			
			add(plab, BorderLayout.CENTER);
		}
		{//South
			JPanel pbot = new JPanel(new FlowLayout(FlowLayout.CENTER)); //para que el boton no ocupe todo el sur
			pbot.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
			
			pbot.add(Botones.crearOkBoton(this));
			add(pbot, BorderLayout.SOUTH);
		}

		pack();
		setLocationRelativeTo(owner);
	}
	

}
