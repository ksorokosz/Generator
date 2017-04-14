package gui;

import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.Border;


/**
 * 
 * @author kamil
 * Graphical User Interface
 */
public class MainWindow extends JFrame
{
	/**
	 * MainWindow class constructor
	 * @param ftdi FTDI module
	 */
	public MainWindow(rs232.FTDI ftdi)
	{
		Container aArea = getContentPane();
		Border aEtchedEdge = BorderFactory.createEtchedBorder();
		
		aTitlePanel = new TitlePanel(); 
		aOptionsPanel = new OptionsPanel();
		aButtonsPanel = new ButtonsPanel(ftdi);
		aButtonsPanel.registerAvailableOptions(aOptionsPanel);
	
		aTitlePanel.setBorder(aEtchedEdge);
		aButtonsPanel.setBorder(aEtchedEdge);
		
		MainWindowView aView = new MainWindowView(aArea, aTitlePanel, aButtonsPanel, aOptionsPanel);
		aView.createView();
	}
	
	@Override
	public String getTitle()
	{
		if(!aTitle.isEmpty())
			return aTitle;
		else
			return new String("");
	}
	
	
	/**
	 * Main window's title
	 */
	public static final String aTitle="Signal Generator";
	/**
	 * Serialized constant
	 */
	private static final long serialVersionUID = 8601947041197595341L;
	/**
	 * Window's title panel
	 */
	private TitlePanel aTitlePanel;
	/**
	 * Options panel
	 */
	private OptionsPanel aOptionsPanel;
	/**
	 * Buttons panel
	 */
	private ButtonsPanel aButtonsPanel;
	
}
