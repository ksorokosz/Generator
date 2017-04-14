package app;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * 
 * @author kamil
 * Main application
 */
public class App 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Toolkit aToolkit = Toolkit.getDefaultToolkit();
		Dimension aScreenDimension = aToolkit.getScreenSize();
		int aScreenHeight = aScreenDimension.height;
		int aScreenWidth = aScreenDimension.width;
		int aWindowHeight = 700*aScreenWidth/1024;
		int aWindowWidth = 490*aScreenHeight/768;
		
		Dimension aWindowSize = new Dimension(aWindowHeight,aWindowWidth);
		
		rs232.FTDI ftdi = new rs232.FTDI();
		gui.MainWindow aMainWindow = new gui.MainWindow(ftdi);
		
		aMainWindow.setSize(aWindowSize);
		aMainWindow.setTitle(aMainWindow.getTitle());
		aMainWindow.setLocationRelativeTo(null);
		aMainWindow.setResizable(false);
		aMainWindow.toFront();
		aMainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aMainWindow.setVisible(true);
	}

}
