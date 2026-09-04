package paa.parking.presentation;

import java.awt.AWTEvent;
import java.awt.EventQueue;

import javax.swing.JOptionPane;

public class InterceptorExcepciones extends EventQueue
{
	protected void dispatchEvent (AWTEvent event)
	{
		try
		{
			super.dispatchEvent(event);
		} catch (Throwable t)
		{
			t.printStackTrace(); //para ver el error en consola
			
			JOptionPane.showMessageDialog(null, "Error: " + t.getLocalizedMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
		}
	}
}
