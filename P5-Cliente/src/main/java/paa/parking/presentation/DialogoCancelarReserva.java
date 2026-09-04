package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import paa.parking.business.IParkingService;
import paa.parking.model.Parking;
import paa.parking.model.Reservation;

public class DialogoCancelarReserva  extends JDialog
{
	JComboBox<Reservation> ComboReserva;
	JComboBox<Parking> ComboParking;
	List<Reservation> lista;
	
	public DialogoCancelarReserva(Frame owner, IParkingService service) 
	{
		super(owner, "Reserva de plaza a cancelar", true); //owner es la ventana principal y el mensaje lo que se pone arriba
		
		setLayout(new BorderLayout(3,3));
		{//Center
			JPanel plab = new JPanel();
			plab.setLayout(new BoxLayout(plab, BoxLayout.Y_AXIS));
			plab.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0));
			
			JPanel filaReserva = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			JPanel filaParking = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			
			ComboReserva = new JComboBox<>();//deben salir las mismas que en el desplegable principal
			ComboParking = new JComboBox<>();
			
			filaParking.add(new JLabel("Parking:")); //PARKING
			for (int i = 0; i < ((MainJFrame) owner).getOpcionesParkingsJComboBox().getItemCount(); i++) 
			{
				Parking p = ((MainJFrame) owner).getOpcionesParkingsJComboBox().getItemAt(i);
			    ComboParking.addItem(p); //añadimos uno a uno los parkings
			}
			//Debe quedar señalada por defecto la del desplegable principal
			Parking parking_sel = ((MainJFrame) owner).getSelectedParking();
			for (int i = 0; i < ComboParking.getItemCount(); i++) 
			{
			    Parking p = ComboParking.getItemAt(i);
			    
			    if (p.getId().equals(parking_sel.getId())) 
			    {
			        ComboParking.setSelectedIndex(i);
			        break; //para que deje de buscar
			    }
			}

			ComboParking.setPreferredSize(new Dimension(200, 25));
			ComboParking.setEditable(true);
			ComboParking.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//actualizamos la lista de reservas
						actualizarListaReservas(service);
					}
				}
			);
			filaParking.add(ComboParking);
			
			
			filaReserva.add(new JLabel("Matrícula asociada:")); //RESERVA
			actualizarListaReservas(service);
			ComboReserva.setPreferredSize(new Dimension(200, 25));
			ComboReserva.setEditable(true);
			Reservation reserva_sel = ((MainJFrame) owner).getSelectedReserve();
			if (reserva_sel != null)//por si no hay nada seleccionado
			{
				for (int i = 0; i < ComboReserva.getItemCount(); i++) 
				{
					Reservation r = ComboReserva.getItemAt(i);
				    
				    if (r.getId().equals(reserva_sel.getId())) 
				    {
				    	ComboReserva.setSelectedIndex(i);
				        break; //para que deje de buscar
				    }
				}
			}
			
			filaReserva.add(ComboReserva);
			
			
			plab.add(filaParking);
			plab.add(filaReserva);
			
			add(plab, BorderLayout.CENTER);
		}
		{//South
			JPanel pbot = new JPanel(new FlowLayout(FlowLayout.CENTER)); //para que el boton no ocupe todo el sur
			
			pbot.add(Botones.crearCancelarReservaBoton(this, service, owner));
			pbot.add(Botones.crearCancelarBoton(this));
			
			add(pbot, BorderLayout.SOUTH);
		}

		pack();
		setLocationRelativeTo(owner);
	}

	public Reservation getReserva() {
		return (Reservation) ComboReserva.getSelectedItem();
	}

	public Parking getParking() {
		return (Parking) ComboParking.getSelectedItem();
	}
	
	public void actualizarListaReservas(IParkingService service) //actualiza la lista de reservas del parking seleccionado
	{
	    Parking p = (Parking) ComboParking.getSelectedItem();
		lista = service.findByParkingId(p.getId()); //saco la lista de reservas de ese parking
		ComboReserva.removeAllItems();
		for (int i = 0; i < lista.size(); i++) 
		{
			ComboReserva.addItem(lista.get(i));
		}
	}

}
