package paa.parking.util;

import paa.parking.business.*;
import paa.parking.model.Parking;
import paa.parking.util.impl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

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
public class ClickableParkingMap extends ParkingMap {
	private static final long serialVersionUID = 1L;
	private JComboBox<Parking> parkingSelector;

	/**
	 * Construye un nuevo mapa con la vista centrada en Madrid, con el tamaño
	 * preferido indicado por el usuario.
	 *
	 * @param preferredWidth  Ancho preferido
	 * @param preferredHeight Alto preferido
	 */
	public ClickableParkingMap(int preferredWidth, int preferredHeight, IParkingService service,
			JComboBox<Parking> parkingSelector) {
		super(preferredWidth, preferredHeight, service);
		this.parkingSelector = parkingSelector;

		this.getMainMap().addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Rectangle viewportBounds = ClickableParkingMap.this.getMainMap().getViewportBounds();
				Point2D px = new Point2D.Double(viewportBounds.x + arg0.getPoint().x,
						viewportBounds.y + arg0.getPoint().y);
				for (ParkingWaypoint wp : ClickableParkingMap.this.waypoints) {
					if (ParkingWaypointRenderer.isMouseInside(ClickableParkingMap.this.getMainMap(), wp, px)) {
						System.out.println(
								"Hemos pinchado en " + ClickableParkingMap.this.service.findParking(wp.getParkingId()));
						ClickableParkingMap.this.parkingSelector
								.setSelectedItem(ClickableParkingMap.this.service.findParking(wp.getParkingId()));
						break;
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// pendiente de programar

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// pendiente de programar
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// pendiente de programar
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// pendiente de programar
			}
		});

		this.getMainMap().addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent mouseEvent) {
				// pendiente de programar
			}

			@Override
			public void mouseMoved(MouseEvent mouseEvent) {
				Rectangle viewportBounds = ClickableParkingMap.this.getMainMap().getViewportBounds();
				Point2D px = new Point2D.Double(viewportBounds.x + mouseEvent.getPoint().x,
						viewportBounds.y + mouseEvent.getPoint().y);
				for (ParkingWaypoint wp : ClickableParkingMap.this.waypoints) {
					if (ParkingWaypointRenderer.isMouseInside(ClickableParkingMap.this.getMainMap(), wp, px)) {
						ClickableParkingMap.this.getMainMap().setCursor(new Cursor(Cursor.HAND_CURSOR));
						return;
					}
				}
				ClickableParkingMap.this.getMainMap().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}
}
