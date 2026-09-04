package paa.parking.presentation;


import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import paa.parking.presentation.MainJFrame;

public class App 
{
	public static void main (String[] args) 
	{
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new InterceptorExcepciones());
		
		SwingUtilities.invokeLater ( 
	         new Runnable() 
	         {
	        	 public void run() 
	        	 { 
			       	JFrame f = new MainJFrame("Parking Manager");
			    	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    	f.pack();     				    	    	
			    	f.setVisible(true);  //  ! No es bloqueante, el programa sigue !
			    	System.out.println("Salgo...");
			     }
	         });
	    }

}
