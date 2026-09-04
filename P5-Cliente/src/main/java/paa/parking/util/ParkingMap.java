package paa.parking.util;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;
import paa.parking.business.*;
import paa.parking.model.*;
import paa.parking.util.impl.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * ParkingMap es una subclase de JXMapKit, a su vez una subclase de JPanel que
 * permite visualizar un mapa. Esta subclase añade un método que muestra
 * sobreimpresos al mapa todos los lockers conocidos por la capa de negocio en
 * la fecha que el usuario elija, así como su número actual de compartimentos
 * disponibles.
 *
 * La documentación de la clase JXMapKit original se puede consultar en <a href=
 * "https://github.com/msteiger/jxmapviewer2">https://github.com/msteiger/jxmapviewer2</a>
 *
 * @author PAA (PRL)
 */
public class ParkingMap extends JXMapKit 
{
	private static final long serialVersionUID = 1L;
	protected WaypointPainter<ParkingWaypoint> waypointPainter;
	protected Set<ParkingWaypoint> waypoints;
	protected IParkingService service;

	/**
	 * Construye un nuevo mapa con la vista centrada en Madrid, con el tamaño
	 * preferido indicado por el usuario.
	 *
	 * @param preferredWidth  Ancho preferido
	 * @param preferredHeight Alto preferido
	 */
	public ParkingMap(int preferredWidth, int preferredHeight, IParkingService service) 
	{
		super();
		this.setDefaultProvider(DefaultProviders.OpenStreetMaps);
		this.service = service;

		TileFactoryInfo info = new OSMTileFactoryInfo();
		TileFactory tf = new DefaultTileFactory(info);
		this.setTileFactory(tf);
		this.setZoom(7);
		this.setAddressLocation(new GeoPosition(40.438889, -3.691944)); // Madrid
		this.getMainMap().setRestrictOutsidePanning(true);
		this.getMainMap().setHorizontalWrapped(false);

		this.waypointPainter = new WaypointPainter<ParkingWaypoint>();
		waypointPainter.setRenderer(new ParkingWaypointRenderer());
		this.getMainMap().setOverlayPainter(this.waypointPainter);
		this.waypoints = new HashSet<ParkingWaypoint>();

		((DefaultTileFactory) this.getMainMap().getTileFactory()).setThreadPoolSize(8);
		this.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
	}

	/**
	 * Pinta en el mapa la situación de ocupación de los parkings del sistema en la
	 * fecha solicitada. Para ello hace uso de la interfaz ParkingService del
	 * paquete paa.locker.business.
	 *
	 * @param date Fecha para la que se desea pintar la disponibilidad.
	 */
	public void showAvailability(LocalDate date) 
	{
	    this.waypoints.clear();
	    // Usamos la fecha de hoy como fecha de operación para la consulta
	    //LocalDate today = LocalDate.now(); 
	    
	    try {
	        for (Parking p : this.service.findAllParkings()) 
	        {
	            // CAMBIO 1: Usar getAvailableSpaces en lugar de calcularlo a mano
	            // Esto evita acceder a p.getReservations() y el error de LazyLoading
	            int available = this.service.getAvailableSpaces(p.getId(), date, date);
	            
	            waypoints.add(new ParkingWaypoint(
	                    available,
	                    p.getMaxReservationsPerDay(), 
	                    p.getId(), 
	                    p.getLongitud(), 
	                    p.getLatitud()));
	        }
	    } catch (Exception e) {
	        // En aplicaciones Swing/AWT es mejor mostrar un diálogo al usuario
	        e.printStackTrace();
	    }
	    this.waypointPainter.setWaypoints(waypoints);
	    this.repaint();
	}

}
