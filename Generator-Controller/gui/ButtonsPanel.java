package gui;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import data.RepeatableSequence;


/**
 * 
 * Buttons panel
 */
public class ButtonsPanel extends JPanel 
{
	/**
	 * ButtonsPanel class constructor
	 * @param ftdi FTDI module
	 */
	public ButtonsPanel(rs232.FTDI ftdi) 
	{	
		aButtonGroup = new ButtonGroup();
		aConnectButton = new JButton("Connect");
		aFinishButton = new JButton("Finish");
		aSendButton = new JButton("Send");
		aSendButton.setEnabled(false);
		aResetButton = new JButton("Reset");
		aLoadButton = new JButton("Load Frequencies");
		
		aButtonGroup.add(aSendButton);
		aButtonGroup.add(aConnectButton);
		aButtonGroup.add(aFinishButton);
		aButtonGroup.add(aResetButton);
		aButtonGroup.add(aLoadButton);
		
		setSendListener();
		setLoadListener();
		setFinishListener();
		setConnectListener();
		setResetListener();
		
		super.setLayout(new GridLayout(0, 5, 20, 0));
		super.add(aSendButton);
		super.add(aLoadButton);
		super.add(aResetButton);
		super.add(aConnectButton);
		super.add(aFinishButton);
		
		/* FTDI instance */
		aFTDI = ftdi;
		
		/* Create repeat sequence instance */
		sequence = new RepeatableSequence(aFTDI);
		
		/* Initialize file chooser */
		aFileChooser = new JFileChooser();
		
		/* Create scheduled executor service pool */
		// scheduledExecutorService = Executors.newScheduledThreadPool(5);
	}
	
	/**
	 * Register available options 
	 * @param aOptionsPanel Available options
	 */
	public void registerAvailableOptions(OptionsPanel aOptionsPanel)
	{
		this.aOptionsPanel = aOptionsPanel;
	}
	
	private void setLoadListener()
	{
		aLoadButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				aLoadButton.setEnabled(false);
				
				Integer fileChooserStatus = aFileChooser.showOpenDialog(null);
				
				if ( fileChooserStatus == JFileChooser.APPROVE_OPTION )
				{
					/* Get file */
					File file = aFileChooser.getSelectedFile();
					
					/* Append preview */
					try 
					{
						/* Get duration */
						Double durationValue  = aOptionsPanel.getDurationValue();
						String durationUnit   = aOptionsPanel.getDurationUnit();
					
						
						
						/* Read file */
						Scanner input = new Scanner(new FileReader(file));
						
						/* Current ID */
						Long id = 0L;
						while (input.hasNextLong())
						{
							/* Get frequency */
							Double frequencyValue = (double)input.nextLong();
							String frequencyUnit  = "Hz";
						
							/* Store as data object */
							data.Data aData = new data.Data(frequencyValue, frequencyUnit, durationValue, durationUnit);
						
							/* For the first element */
							if ( id == 0 )
							{
								((data.RepeatableSequence)sequence).setDuration(aData.getDurationInMs());
							}
							
							/* Add data to send */
							((data.RepeatableSequence)sequence).addToSend(aData);

							/* Append preview */
							aOptionsPanel.appendPreview("ID: " + id.toString() + "\t" + aData.toString());
							id++;
						}
					} 
					catch (IOException e1) 
					{
						JOptionPane.showMessageDialog(null, "Problem to read file");
					} 	
				}
			}
		});
	}
	
	/**
	 * Sets listener for send button
	 *
	 */
	private void setSendListener()
	{
		aSendButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				/* Sending is disabled */
				aSendButton.setEnabled(false);
				
				/* Send buffer */
				((data.RepeatableSequence)sequence).run();
			}
		});
	}
	
	/**
	 * Sets listener for refresh button
	 */
	private void setResetListener()
	{
		aResetButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{	
				/* Close FTDI */
				aFTDI.close();
				
				/* Sending is disabled */
				aSendButton.setEnabled(false);
				aLoadButton.setEnabled(true);
				aConnectButton.setEnabled(true);
				
				/* Refresh ports */
				aOptionsPanel.refreshPorts();
				
				/* Clear buffer */
				((data.RepeatableSequence)sequence).clearSendBuffer();	
				
				/* Clear preview */
				aOptionsPanel.clearPreview();
			}
		});
	}
	
	/**
	 * Sets listener for finish button
	 * 
	 */
	private void setFinishListener()
	{
		aFinishButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{	
				/* Close connection */
				aFTDI.close();
				
				/* Exit */
				System.exit(0);
			}
		});
	}
	
	/**
	 * Sets listener for connect button
	 * 
	 */
	private void setConnectListener()
	{
		aConnectButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String portName="";
				try
				{
					portName  = aOptionsPanel.getPortName();
					int speed = aOptionsPanel.getSpeed();
					
					/* Get port */
					CommPortIdentifier port = CommPortIdentifier.getPortIdentifier(portName);
					
					/* Connect */
					if(aFTDI.connect(port,speed))
					{
						aSendButton.setEnabled(true);
					}
				}
				catch(NullPointerException exc)
				{
					JOptionPane.showMessageDialog(null, "Error: No ports available", "Error", JOptionPane.ERROR_MESSAGE);
				} 
				catch (NoSuchPortException exc) 
				{
					JOptionPane.showMessageDialog(null, "Error: Port " + portName + " isn't available", "Error", JOptionPane.ERROR_MESSAGE);
				} 
				catch (PortInUseException exc) 
				{
					JOptionPane.showMessageDialog(null, "Error: Port " + portName + " is in use", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	/**
	 * Available options
	 */
	private OptionsPanel aOptionsPanel;
	/**
	 * Button group
	 */
	private ButtonGroup aButtonGroup;
	/**
	 * Ports rescan
	 */
	private JButton aResetButton;
	/**
	 * Send button - send data
	 */
	private JButton aSendButton;
	/**
	 * Add frequency to the buffer
	 */
	private JButton aLoadButton;
	/**
	 * Finish button - close application
	 */
	private JButton aFinishButton;
	/**
	 * Connect button - connect to FTDI
	 */
	private JButton aConnectButton;
	/**
	 * Serialized constant
	 */
	private static final long serialVersionUID = 8216223745201426275L;
	
	/**
	 * FTDI instance
	 */
	private rs232.FTDI aFTDI;
	
	/**
	 * Sequence instance
	 */
	private TimerTask sequence;
	/**
	 * File chooser
	 */
	private JFileChooser aFileChooser;

}
