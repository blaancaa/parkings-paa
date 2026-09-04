package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import paa.parking.business.IParkingService;
import paa.parking.model.Parking;

public class DialogoReservarPlaza extends JDialog
{
	JTextField txtMatricula;
	JComboBox<LocalDate> ComboDia;
	JComboBox<Parking> ComboParking;
	
	public DialogoReservarPlaza(Frame owner, IParkingService service) 
	{
		super(owner, "Nueva reserva de plaza de aparcamiento", true); //owner es la ventana principal y el mensaje lo que se pone arriba
		
		setLayout(new BorderLayout(3,3));
		{//Center
			JPanel plab = new JPanel();
			plab.setLayout(new BoxLayout(plab, BoxLayout.Y_AXIS));
			plab.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 0));
			
			JPanel filaDia = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			JPanel filaMatricula = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			JPanel filaParking = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
			
			ComboDia = new JComboBox<>();//deben salir las mismas fechas que en el desplegable principal
			txtMatricula = new JTextField();
			ComboParking = new JComboBox<>();
			
			filaDia.add(new JLabel("Dia:"));
			for (int i = 0; i < ((MainJFrame) owner).getFechasJComboBox().getItemCount(); i++) 
			{
				ComboDia.addItem(((MainJFrame) owner).getFechasJComboBox().getItemAt(i));//añadimos uno a uno los dias
			}
			ComboDia.setPreferredSize(new Dimension(130, 25));
			ComboDia.setEditable(true);
			filaDia.add(ComboDia);
			
			filaMatricula.add(new JLabel("Matricula:"));
			txtMatricula.setPreferredSize(new Dimension(130, 25));
			filaMatricula.add(txtMatricula);
			
			filaParking.add(new JLabel("Parking:"));
			for (int i = 0; i < ((MainJFrame) owner).getOpcionesParkingsJComboBox().getItemCount(); i++) 
			{
			    ComboParking.addItem(((MainJFrame) owner).getOpcionesParkingsJComboBox().getItemAt(i)); //añadimos uno a uno los parkings 
			}

			ComboParking.setPreferredSize(new Dimension(130, 25));
			ComboParking.setEditable(true);
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
			filaParking.add(ComboParking);
			
			
			plab.add(filaDia);
			plab.add(filaMatricula);
			plab.add(filaParking);
			
			add(plab, BorderLayout.CENTER);
		}
		{//South
			JPanel pbot = new JPanel(new FlowLayout(FlowLayout.CENTER)); //para que el boton no ocupe todo el sur
			
			pbot.add(Botones.crearAñadirReservaBoton(this, service, owner));
			pbot.add(Botones.crearCancelarBoton(this));
			
			add(pbot, BorderLayout.SOUTH);
		}

		pack();
		setLocationRelativeTo(owner);
	}

	public String getTxtMatricula() {
		return txtMatricula.getText();
	}

	public Parking getParking() {
		return (Parking) ComboParking.getSelectedItem();
	}

	public LocalDate getDia() {
		return (LocalDate) ComboDia.getSelectedItem();
	}
}
