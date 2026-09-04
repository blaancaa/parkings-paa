package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import paa.parking.business.IParkingService;
import paa.parking.business.RemoteParkingService;

public class DialogoAñadirParking  extends JDialog
{
	JTextField txtNombre, txtDireccion, txtPlazas, txtMaxReservas, txtLongitud, txtLatitud;
	
	public DialogoAñadirParking(Frame owner, IParkingService service) 
	{
		super(owner, "Añadir un parking", true); //owner es la ventana principal y el mensaje lo que se pone arriba
		
		setLayout(new BorderLayout(3,3));
		{//Center
			JPanel plab = new JPanel();
			plab.setLayout(new BoxLayout(plab, BoxLayout.Y_AXIS));//6 filas, 2 columna y con separacion
			plab.setBorder(BorderFactory.createEmptyBorder(0, 120, 0, 0));
			
			txtNombre = new JTextField();
			txtDireccion = new JTextField();
			txtPlazas = new JTextField();
			txtMaxReservas = new JTextField();
			txtLongitud = new JTextField();
			txtLatitud = new JTextField();
			
			plab.add(crearFila("Nombre:", txtNombre));
			plab.add(crearFila("Dirección:", txtDireccion));
			plab.add(crearFila("Plazas:", txtPlazas));
			plab.add(crearFila("Max Reservas día:", txtMaxReservas));
			plab.add(crearFila("GPS. Longitud:", txtLongitud));
			plab.add(crearFila("GPS Latitud:", txtLatitud));
			
			add(plab, BorderLayout.CENTER);
		}
		{//South
			JPanel pbot = new JPanel(new FlowLayout(FlowLayout.CENTER)); //para que el boton no ocupe todo el sur
			
			pbot.add(Botones.crearAñadirParkingBoton(this, service, owner));
			pbot.add(Botones.crearCancelarBoton(this));
			
			add(pbot, BorderLayout.SOUTH);
		}

		pack();
		setLocationRelativeTo(owner);
	}
	
	private JPanel crearFila(String texto, JTextField campo) 
	{
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        // Ajustamos el tamaño del campo para que se vea grande
        campo.setPreferredSize(new Dimension(400, 25));
        
        fila.add(new JLabel(texto));
        fila.add(campo);
        
        return fila;
    }

	public String getNombre() {
		return txtNombre.getText();
	}

	public String getDireccion() {
		return txtDireccion.getText();
	}

	public String getPlazas() {
		return txtPlazas.getText();
	}

	public String getMaxReservas() {
		return txtMaxReservas.getText();
	}

	public String getLongitud() {
		return txtLongitud.getText();
	}

	public String getLatitud() {
		return txtLatitud.getText();
	}
	
}
