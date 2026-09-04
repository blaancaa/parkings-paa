package paa.parking.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import paa.parking.business.IParkingService;
import paa.parking.business.RemoteParkingService;
import paa.parking.model.Parking;
import paa.parking.model.Reservation;
import paa.parking.util.ParkingMap;

public class MainJFrame  extends JFrame
{
	private final IParkingService service = new RemoteParkingService();
	private ParkingMap p_map = new ParkingMap(800, 600, service);
	private JComboBox<Parking> opcionesParkings;
	private JList<Reservation> listaReservas = new JList<Reservation>();
	private LocalDate fecha_sel = LocalDate.now();;
	private JComboBox<LocalDate> fechas_JComboBox = new JComboBox<>();
	private JTextField fecha;
	
	public MainJFrame(String title)
	{
		super(title);
		
		//dialogo que muestre el nombre del programa, con versión y autor
		//JOptionPane.showMessageDialog(null, "Sistema de gestión de Parkings PAA\nVersión: 2.0\nAutor: Blanca Loma", "Acerca de", JOptionPane.INFORMATION_MESSAGE); //null se usa porque la ventana principal aun no es visible
		DialogoInicio d = new DialogoInicio(this);
		d.setVisible(true);
		
		//initData(); //inicializamos algunos parkings y reservas
		createComponents();
	}
	
	private void createComponents()
	{
		setLayout(new BorderLayout(0,0));
		{//North
			North();
		}
		{//Center
			Center();
		}
		{//West
			West();
		}	
		
	}
	
	private void North()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout(0,0));
		{//North
			//Menu
			JMenuBar barraMenu = new JMenuBar();
			
			//MENU OPERATIONS
			JMenu operations = new JMenu("Operations");
			JMenuItem itemAñadirParking = new JMenuItem("Añadir Parking");
			JMenuItem itemReservarPlaza = new JMenuItem("Reservar plaza");
			JMenuItem itemCancelarReserva = new JMenuItem("Cancelar Reserva");
			JMenuItem itemSalir = new JMenuItem("Salir");
			
			itemAñadirParking.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						DialogoAñadirParking d_AñadirParking = new DialogoAñadirParking(MainJFrame.this, service);
						d_AñadirParking.setVisible(true);
					}
				}
			);
			itemReservarPlaza.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						DialogoReservarPlaza d_ReservarPlaza = new DialogoReservarPlaza(MainJFrame.this, service);
						d_ReservarPlaza.setVisible(true);
					}
				}
			);
			itemCancelarReserva.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						DialogoCancelarReserva d_CancelarReserva = new DialogoCancelarReserva(MainJFrame.this, service);
						d_CancelarReserva.setVisible(true);
					}
				}
			);
			itemSalir.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//Salimos de la app si se pulsa
						System.exit(0);
					}
				}
			);
			operations.add(itemAñadirParking);
			operations.add(itemReservarPlaza);
			operations.add(itemCancelarReserva);
			operations.add(itemSalir);
			
			//MENU AYUDA
			JMenu ayuda = new JMenu("Ayuda");
			JMenuItem itemAyuda = new JMenuItem("Acerca de");
			itemAyuda.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						DialogoAyuda d_ayuda = new DialogoAyuda(MainJFrame.this);
						d_ayuda.setVisible(true);
					}
				}
			);
			ayuda.add(itemAyuda);
			
			barraMenu.add(operations);
			barraMenu.add(ayuda);
			
			p.add(barraMenu, BorderLayout.NORTH);
		}
		{//Center
			//Barra de botones
			JPanel pbot = new JPanel();
			pbot.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			
			Icon añadir = new ImageIcon(getClass().getResource("/META-INF/anadir.png"));
			JButton b_añadir = new JButton(añadir);
			Icon cancelar = new ImageIcon(getClass().getResource("/META-INF/cancelar.png"));
			JButton b_cancelar = new JButton(cancelar);
			Icon parking = new ImageIcon(getClass().getResource("/META-INF/parking.png"));
			JButton b_parking = new JButton(parking);
			
			Border lineaGris = BorderFactory.createLineBorder(Color.GRAY);
			Border margenInterno = BorderFactory.createEmptyBorder(5, 15, 5, 15);
			
			for (JButton b : new JButton[]{b_parking, b_cancelar, b_añadir}) { 
			    b.setFocusPainted(false);
			    b.setContentAreaFilled(false);
			    b.setBorder(BorderFactory.createCompoundBorder(lineaGris, margenInterno));
			}
			
			
			b_parking.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						DialogoAñadirParking d_AñadirParking = new DialogoAñadirParking(MainJFrame.this, service);
						d_AñadirParking.setVisible(true);
					}
				}
			);
			b_añadir.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						DialogoReservarPlaza d_ReservarPlaza = new DialogoReservarPlaza(MainJFrame.this, service);
						d_ReservarPlaza.setVisible(true);
					}
				}
			);
			b_cancelar.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						DialogoCancelarReserva d_CancelarReserva = new DialogoCancelarReserva(MainJFrame.this, service);
						d_CancelarReserva.setVisible(true);
					}
				}
			);
			
			pbot.add(b_parking); pbot.add(b_añadir); pbot.add(b_cancelar);
			pbot.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));//para que no se pegue a la derecha
			pbot.setBackground(new Color(210, 210, 210));//cambiamos el fondo a gris oscuro
			p.add(pbot, BorderLayout.CENTER); 
		}
		{//East
			//Simulador de dias de operación
			JPanel pdias = new JPanel();
			pdias.setLayout(new BorderLayout(3,5));
			{//Center
				fecha = new JTextField(fecha_sel.toString(), 10);//le ponemos tamaño para que se vea el titulo del borde completo
				fecha.setBackground(new Color(235, 235, 235)); //ponemos fondo gris claro
				fecha.setHorizontalAlignment(JTextField.CENTER);
				fecha.setEditable(false); //solo cambia con los botones
				fecha.setFont(fecha.getFont().deriveFont(java.awt.Font.BOLD)); //hacemos que sea negrita
				pdias.add(fecha, BorderLayout.CENTER);
			}
			{//East
				
				JPanel psig = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));//al usar flow layout los botones no se estiran
				psig.setOpaque(false);
				psig.add(Botones.crearSiguienteBoton(this));
				pdias.add(psig, BorderLayout.EAST);
			}
			{//West
				
				JPanel pant = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));//al usar flow layout los botones no se estiran
				pant.setOpaque(false);
				pant.add(Botones.crearAnteriorBoton(this));
				pdias.add(pant, BorderLayout.WEST);
			}

			pdias.setBorder(BorderFactory.createTitledBorder("Calendario (Oper. Date Simul.)"));
			pdias.setBackground(new Color(210, 210, 210));//cambiamos el fondo a gris oscuro
			p.add(pdias, BorderLayout.EAST);
		}
		add(p, BorderLayout.NORTH);
	}//NORTH
	
	
	
	
	private void Center()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout(10,0));
		{//North
			//Selección de fecha
			fechas_JComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (fechas_JComboBox.getSelectedItem() != null) 
						{
							actualizarFechaSel(3); //3 es cuando viene de aqui
						}
					}
				}
			);
			actualizarFechas(); //para el inicio
			
			fechas_JComboBox.setEditable(true);
			((JTextField)fechas_JComboBox.getEditor().getEditorComponent()).setBackground(new Color(235, 235, 235)); //ponemos fondo gris claro
			p.add(fechas_JComboBox, BorderLayout.NORTH);
		}
		{//Center
			// mapa 
			actualizarMapa();
			p.add(p_map, BorderLayout.CENTER);
		}
		//borde mapa
		p.setBorder(BorderFactory.createTitledBorder("Mapa del parking"));
		
		add(p, BorderLayout.CENTER);
	}//CENTER
	
	
	
	
	private void West()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout(5,5));
		{//North
			//Desplegable parking
			opcionesParkings = new JComboBox<>();
			
			actualizarParkings();
			opcionesParkings.setEditable(true);
			opcionesParkings.setBorder(BorderFactory.createTitledBorder("Parking"));
			((JTextField)opcionesParkings.getEditor().getEditorComponent()).setBackground(new Color(235, 235, 235)); //ponemos fondo gris claro
			p.add(opcionesParkings, BorderLayout.NORTH);
			
			//debemos actualizar la lista de reservas cuando cambie el parking
			opcionesParkings.addActionListener(e -> actualizarListaReservas(true));
			actualizarListaReservas(false); //para carga inicial del primer parking (no mostramos inf pq se mostraria antes del mapa)
		}
		{//Center
			//Desplegable reservas
			
			listaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			JScrollPane scroller = new JScrollPane(listaReservas);
			scroller.setBorder(BorderFactory.createTitledBorder("Reservas existentes"));
			p.add(scroller, BorderLayout.CENTER);//añadimos el scroller y no la lista
		
			listaReservas.addListSelectionListener(e -> 
				{
					if (!e.getValueIsAdjusting())
					{
						datosReservas();
					}
				}
			);
		
		}
		p.setPreferredSize(new Dimension(250, 800));
		add(p, BorderLayout.WEST);
	}//WEST
	
	//FIN BORDERLAYOUT
	
	public JComboBox<LocalDate> getFechasJComboBox()
	{
		return fechas_JComboBox;
	}
	
	public JComboBox<Parking> getOpcionesParkingsJComboBox()
	{
		return opcionesParkings;
	}
	
	public JList<Reservation> getListaReservas()
	{
		return listaReservas;
	}
	
	public Parking getSelectedParking()
	{
		return (Parking) opcionesParkings.getSelectedItem();
	}
	
	public Reservation getSelectedReserve()
	{
		return listaReservas.getSelectedValue();
	}
	
	public void actualizarMapa()
	{
		p_map.showAvailability(fecha_sel);
		
		p_map.revalidate();                // Recalcula diseño
	    p_map.repaint();                   // Fuerza dibujo
	}
	
	public void actualizarParkings()//cuando  añadimos un parking
	{
		//actualizamos la lista de parkings
		
		// 1. Guardamos los listeners actuales y los quitamos
	    ActionListener[] listeners = opcionesParkings.getActionListeners();
	    for (ActionListener al : listeners) 
	    {
	        opcionesParkings.removeActionListener(al); //para que la proxima accion no de error
	    }
	    
	    // 2. realizamos acciones de actualizar el desplegable de parking
		opcionesParkings.removeAllItems(); //para que no se sobreescriba
		List<Parking> todosLosParkings = service.findAllParkings();
		for(Parking park : todosLosParkings)
		{
			opcionesParkings.addItem(park);
		}
		
		if (opcionesParkings.getItemCount() > 0) 
		{
	        opcionesParkings.setSelectedIndex(0);
	    }
		
		// 3. Volvemos a poner los listeners
	    for (ActionListener al : listeners) {
	        opcionesParkings.addActionListener(al);
	    }
		
	}
	
	//fecha de arriba
	public void actualizarFechaSel(int num)//la hacemos public para llamarla desde botones
	{
		if (fecha_sel == null) fecha_sel = LocalDate.now(); //por defecto es la de hoy
		
		if (num == 1) //boton siguiente
		{
			fecha_sel = fecha_sel.plusDays(1);
		}
		else if (num == 2) //boton anterior
		{
			fecha_sel = fecha_sel.minusDays(1);
			//ERRORES A PROPÓSITO: fecha de reserva anterior a fecha actual
			//Parking p7 = service.createParking("Atocha", "Glorieta del Emperador Carlos V", 14, 8, -3.679583, 40.3965354);
			//service.reserve(p7.getId(), "1234ABC", LocalDate.now(), LocalDate.now().plusDays(2));
		}
		else if (num == 3) //si cambiamos la fecha del desplegable
		{
			LocalDate seleccionada = (LocalDate) fechas_JComboBox.getSelectedItem();
			if (seleccionada != null)
			{
				fecha_sel = seleccionada;
			}
		}
		
		actualizarFechas(); //actualizamos el desplegable de fechas
		
		//Actualizamos el mapa y la fecha de arriba
		fecha.setText(fecha_sel.toString());
		actualizarMapa();
		
	}
	
	//fechas desplegable, actualizamos que salgan siempre los 14 dias despues de la fecha seleccionada arriba
	private void actualizarFechas()
	{
		fechas_JComboBox.removeAllItems();
		
		//añadimos la base y los 14 dias siguientes
		for(int i = 0; i < 15; i++)
		{
			fechas_JComboBox.addItem(fecha_sel.plusDays(i)); 
		}
		
		fechas_JComboBox.setSelectedIndex(0); //se selecciona la primera
	}
	
	public void actualizarListaReservas(boolean mostrarInf) //actualiza la lista de reservas del parking seleccionado
	{
	    Parking sel = (Parking) opcionesParkings.getSelectedItem();
	    
	    if(sel == null)
	    {
	    	listaReservas.setListData(new Vector<> ());
	    	return;
	    }
	    
	    Parking pCompleto = service.findParkingWithReservation(sel.getId()); //porque si lo hacemos con sel no funciona pq es lazy y las reservas no estan cargadas
	    if (mostrarInf)
    	{
    		DialogoParking d_par = new DialogoParking(this, pCompleto); //creamos ventana de inf
    		d_par.setVisible(true);
    	}
		
        // Obtenemos los datos y actualizamos la lista
        listaReservas.setListData(new Vector<>(service.findByParkingId(sel.getId())));
	}

	private void datosReservas() //dialogo al pinchar sobre una reserva
	{
		Reservation sel = listaReservas.getSelectedValue();
		if (sel != null)
		{
			DialogoReserva d_res = new DialogoReserva(this, sel); //creamos ventana de inf
			d_res.setVisible(true);
		}
		
	}
	
	private void initData() 
	{
	    // Creamos parkings
		Parking p1 = service.createParking("Atocha", "Glorieta del Emperador Carlos V", 14, 8, -3.679583, 40.3965354);
		Parking p2 = service.createParking("Moncloa", "Plaza de la Moncloa", 10, 5, -3.7116853, 40.4317665);
		Parking p3 = service.createParking("América", "Avenida de América, 1", 10, 6, -3.6770431, 40.4390217);
		Parking p4 = service.createParking("Chamartín", "Agustín de Foxá, 40", 2, 1, -3.681379, 40.4715852);
	    Parking p5 = service.createParking("Parking Centro", "Calle Mayor 1", 5, 5, -3.0, 40.0);
	    Parking p6 = service.createParking("Parking Norte", "Av. San Luis", 10, 10, -3.1, 40.1);
	    
	    // Añadimos un par de reservas 
	    service.reserve(p1.getId(), "1234ABC", LocalDate.now().plusDays(2), LocalDate.now());
	    service.reserve(p1.getId(), "5678DEF", LocalDate.now().plusDays(1), LocalDate.now());
	    service.reserve(p2.getId(), "9999XYZ", LocalDate.now().plusDays(5), LocalDate.now());
	    service.reserve(p2.getId(), "4720FTX", LocalDate.now().plusDays(3), LocalDate.now());
	    service.reserve(p2.getId(), "4721FTX", LocalDate.now().plusDays(2), LocalDate.now());
	    service.reserve(p3.getId(), "4722FTX", LocalDate.now().plusDays(3), LocalDate.now());
	    service.reserve(p3.getId(), "4723FTX", LocalDate.now().plusDays(3), LocalDate.now());
	    service.reserve(p3.getId(), "4721FTX", LocalDate.now().plusDays(1), LocalDate.now());
	    service.reserve(p2.getId(), "4722FTX", LocalDate.now().plusDays(1), LocalDate.now());
	    service.reserve(p3.getId(), "4720FTX", LocalDate.now().plusDays(1), LocalDate.now());
	    service.reserve(p4.getId(), "4720FTX", LocalDate.now().plusDays(1), LocalDate.now());
	    service.reserve(p5.getId(), "4720FTX", LocalDate.now().plusDays(1), LocalDate.now());
	    service.reserve(p6.getId(), "4720FTX", LocalDate.now().plusDays(1), LocalDate.now());
	}

}
