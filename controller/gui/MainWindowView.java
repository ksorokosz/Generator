package gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

/**
 * 
 * @author kamil
 * Main window arrangement
 */
public class MainWindowView 
{
	/**
	 * MainWidnowView class constructor
	 * @param aArea Container for panels
	 * @param aTitlePanel Title panel
	 * @param aButtonsPanel Buttons panel
	 * @param aOptionsPanel Options panel
	 */
	public MainWindowView(Container aArea, TitlePanel aTitlePanel, ButtonsPanel aButtonsPanel, OptionsPanel aOptionsPanel)
	{
		this.aArea = aArea;
		this.aButtonsPanel = aButtonsPanel;
		this.aTitlePanel = aTitlePanel;
		this.aOptionsPanel = aOptionsPanel;
	}
	
	/**
	 * Create window's view
	 */
	public void createView()
	{
		GridBagLayout aArrangement = new GridBagLayout();
		aArea.setLayout(aArrangement);
		
		GridBagConstraints aGridBagConstraints = new GridBagConstraints();
		
		aGridBagConstraints.fill = GridBagConstraints.BOTH;
		aGridBagConstraints.anchor = GridBagConstraints.NORTH;
		aGridBagConstraints.weightx = 100;
		aGridBagConstraints.weighty = 4;
		aGridBagConstraints.gridx = 0;
		aGridBagConstraints.gridy = 0;
		aGridBagConstraints.gridwidth = 3;
		aGridBagConstraints.gridheight = 1;
		aArea.add(aTitlePanel,aGridBagConstraints);
		
		aGridBagConstraints.fill = GridBagConstraints.BOTH;
		aGridBagConstraints.anchor = GridBagConstraints.CENTER;
		aGridBagConstraints.weightx = 200;
		aGridBagConstraints.weighty = 200;
		aGridBagConstraints.gridx = 0;
		aGridBagConstraints.gridy = 1;
		aGridBagConstraints.ipadx = 150;
		aGridBagConstraints.gridwidth = 1;
		aGridBagConstraints.gridheight = 1;
		aArea.add(new JPanel(),aGridBagConstraints);
		
		aGridBagConstraints.fill = GridBagConstraints.BOTH;
		aGridBagConstraints.anchor = GridBagConstraints.CENTER;
		aGridBagConstraints.weightx = 200;
		aGridBagConstraints.weighty = 200;
		aGridBagConstraints.gridx = 1;
		aGridBagConstraints.gridy = 1;
		aGridBagConstraints.ipadx = 25;
		aGridBagConstraints.gridwidth = 1;
		aGridBagConstraints.gridheight = 1;
		aArea.add(aOptionsPanel,aGridBagConstraints);
		
		aGridBagConstraints.fill = GridBagConstraints.BOTH;
		aGridBagConstraints.anchor = GridBagConstraints.CENTER;
		aGridBagConstraints.weightx = 200;
		aGridBagConstraints.weighty = 200;
		aGridBagConstraints.gridx = 2;
		aGridBagConstraints.gridy = 1;
		aGridBagConstraints.ipadx = 150;
		aGridBagConstraints.gridwidth = 1;
		aGridBagConstraints.gridheight = 1;
		aArea.add(new JPanel(),aGridBagConstraints);
		
		aGridBagConstraints.fill = GridBagConstraints.BOTH;
		aGridBagConstraints.anchor = GridBagConstraints.SOUTH;
		aGridBagConstraints.weightx = 100;
		aGridBagConstraints.weighty = 2;
		aGridBagConstraints.gridx = 0;
		aGridBagConstraints.gridy = 2;
		aGridBagConstraints.ipadx = 25;
		aGridBagConstraints.gridwidth = 3;
		aGridBagConstraints.gridheight = 1;
		aArea.add(aButtonsPanel,aGridBagConstraints);
	}
	
	/**
	 * Container 
	 */
	private Container aArea;
	/**
	 * Title panel
	 */
	private TitlePanel aTitlePanel;
	/**
	 * Buttons panel
	 */
	private ButtonsPanel aButtonsPanel;
	/**
	 * Options panel
	 */
	private OptionsPanel aOptionsPanel;
}
