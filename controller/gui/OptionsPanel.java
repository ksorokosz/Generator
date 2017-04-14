package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

/**
 * 
 * @author kamil
 * Options panel
 */
public class OptionsPanel extends JPanel 
{
	/**
	 * Options panel constructor
	 */
	public OptionsPanel() 
	{
		JPanel aWritePanel = new JPanel();
		aWritePanel.setLayout(new GridLayout(4,1));

		JPanel aReadPanel = new JPanel();
		aReadPanel.setLayout(new BoxLayout(aReadPanel, BoxLayout.LINE_AXIS));
		aReadPanel.setMinimumSize(new Dimension(100,100));
		
		/* Title */
		
		JLabel aTitleLabel = new JLabel("Available settings:");
		Font aTitleFont = new Font("Tahoma",Font.BOLD | Font.ITALIC ,14);
		aTitleLabel.setFont(aTitleFont);
		
		/* Time */
		
		JLabel aTimeLabel = new JLabel("(1) Duration:");
		Font aTimeFont = new Font("Tahoma", Font.BOLD, 14);
		aTimeLabel.setFont(aTimeFont);
		
		aDurationValue = new JTextField("3");
		Object[] aTimeUnits = { "ms", "s", "min" };
		aDurationUnit = new JComboBox(aTimeUnits);
		aDurationUnit.setSelectedIndex(1);		
		/* Port */
		
		JLabel aPortLabel = new JLabel("(2) Port:");
		Font aPortFont = new Font("Tahoma",Font.BOLD,14);
		aPortLabel.setFont(aPortFont);
		
		Object[] aPorts = rs232.COMPorts.getAvaliableSerialPorts();
		if(aPorts == null || aPorts.length == 0)
		{
			aPorts = new Object[1];
			aPorts[0] = "No serial ports available";
		}
		aPortCheck = new JComboBox(aPorts);
		aPortCheck.setSize(aPortCheck.getPreferredSize());
		
		/* Speed */
		
		JLabel aBaudRatesLabel = new JLabel("(3) Speed:");
		Font aRateFont = new Font("Tahoma",Font.BOLD,14);
		aBaudRatesLabel.setFont(aRateFont);
		Object[] aBaudRates = {"1200","2400","9600","19200"};
		aSpeed = new JComboBox(aBaudRates);
		aSpeed.setSelectedIndex(2);
		aSpeed.setSize(aSpeed.getPreferredSize());
		aSpeed.setEnabled(false);
		
		/* Precision */
		
		JLabel aPrecision = new JLabel("(4) Precision:");
		Font aPrecisionFont = new Font("Tahoma", Font.BOLD, 14);
		aPrecision.setFont(aPrecisionFont);
		
		aPrecisionGroup = new ButtonGroup();
		JRadioButton aFourBytes = new JRadioButton("10 kHz");
		JRadioButton aFiveBytes = new JRadioButton("100 Hz");
		JRadioButton aSixBytes = new JRadioButton("1 Hz");
		aPrecisionGroup.add(aFourBytes);
		aPrecisionGroup.add(aFiveBytes);
		aPrecisionGroup.add(aSixBytes);
		
		aFourBytes.addActionListener(new ActionListener() 
		{	
			public void actionPerformed(ActionEvent e) 
			{
				data.Data.BYTES_SEND = 4;
			}
		});
		aFiveBytes.addActionListener(new ActionListener() 
		{	
			public void actionPerformed(ActionEvent e) 
			{
				data.Data.BYTES_SEND = 5;
			}
		});
		aSixBytes.addActionListener(new ActionListener() 
		{	
			public void actionPerformed(ActionEvent e) 
			{
				data.Data.BYTES_SEND = 6;
			}
		});
		aSixBytes.setSelected(true);
		
		/* Scanner */
		
		aPreview = new JTextArea(20,20);
		aPreview.setToolTipText("Load file with frequencies [Hz]. Each in separate line.");
		aPreview.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret)aPreview.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane aScrollPanel = new JScrollPane(aPreview, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				                                   JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		
		/* Instruction */
		
		Font aDescriptonFont = new Font("Tahoma", Font.ITALIC, 10);
		JLabel aD0 = new JLabel("----------------------------------------------------");
		JLabel aD1 = new JLabel("*(1) Type right value of duration - the same for each frequency");
		JLabel aD2 = new JLabel("*(2) Load file with frequencies");
		JLabel aD3 = new JLabel("*(3) Choose right port to connect");
		JLabel aD4 = new JLabel("*(4) Send data to the device");
		aD1.setFont(aDescriptonFont);
		aD2.setFont(aDescriptonFont);
		aD3.setFont(aDescriptonFont);
		aD4.setFont(aDescriptonFont);
		
		/* View - grids */
		
		JPanel aTime = new JPanel();
		JPanel aSubTime = new JPanel();
		JPanel aPort = new JPanel();
		JPanel aDesc = new JPanel();
		JPanel aPrecisionPanel = new JPanel();
		JPanel aBaudRatePanel = new JPanel();
			
		/* Time */
		
		aSubTime.setLayout(new GridLayout(1,2));
		aSubTime.add(aDurationValue);
		aSubTime.add(aDurationUnit);
		
		aTime.setLayout(new GridLayout(1,2));
		aTime.add(aTimeLabel);
		aTime.add(aSubTime);
		
		/* Port */
		
		aPort.setLayout(new GridLayout(1,2));
		aPort.add(aPortLabel);
		aPort.add(aPortCheck);
		
		/* Speed */
		
		aBaudRatePanel.setLayout(new GridLayout(1,2));
		aBaudRatePanel.add(aBaudRatesLabel);
		aBaudRatePanel.add(aSpeed);

		/* Precision */
		
		JPanel aSubPrecisionPanel = new JPanel(new GridLayout(1, 3));
		aSubPrecisionPanel.add(aSixBytes);
		aSubPrecisionPanel.add(aFiveBytes);
		aSubPrecisionPanel.add(aFourBytes);
		aFourBytes.setEnabled(false);
		aFiveBytes.setEnabled(false);
		
		aPrecisionPanel.setLayout(new GridLayout(1, 2));
		aPrecisionPanel.add(aPrecision);
		aPrecisionPanel.add(aSubPrecisionPanel);
		
		
		/* Description */
		
		aDesc.setLayout(new GridLayout(5,1));
		aDesc.add(aD0);
		aDesc.add(aD1);
		aDesc.add(aD2);
		aDesc.add(aD3);
		aDesc.add(aD4);
		
		/* Write Panel - settings */
		
		aWritePanel.add(aTime);
		aWritePanel.add(aPort);
		aWritePanel.add(aBaudRatePanel);
		aWritePanel.add(aPrecisionPanel);
		
		/* Scanner */
		aReadPanel.add(aScrollPanel);
		
		/* Main Panel */
		JPanel mainOptions = new JPanel();
		mainOptions.setLayout(new GridBagLayout());
		
		/* View */
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx = 0;
		c1.weighty=8;
		c1.gridy = 0;
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0;
		c2.weighty=1;
		c2.gridy = 1;
		
		GridBagConstraints c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.gridx = 0;
		c3.gridy = 2;
		
		mainOptions.add(aWritePanel,c1);
		mainOptions.add(aReadPanel,c2);
		mainOptions.add(aDesc,c3);
		
		super.setLayout(new BorderLayout());
		super.add(mainOptions,BorderLayout.CENTER);
	}

	/**
	 * Returns checked port name
	 * @return Checked port name
	 * @throws NullPointerException 
	 */
	public String getPortName() throws NullPointerException
	{
		String port = aPortCheck.getSelectedItem().toString();
		if(port.contentEquals("No serial ports available"))
		{
			throw new NullPointerException();
		}
		return port;
	}
	/**
	 * Refresh available ports
	 */
	public void refreshPorts()
	{
		aPortCheck.removeAllItems();
		Object[] newItems = rs232.COMPorts.getAvaliableSerialPorts();
		if(newItems!=null && newItems.length>0)
		{
			for(int i=0;i<newItems.length;i++)
			{
				aPortCheck.addItem(newItems[i]);
			}
		}
		else
			aPortCheck.addItem("No serial ports available");
		
	}
	
	/**
	 * Return duration value
	 * @return Duration value
	 * @throws NumberFormatException
	 */
	public Double getDurationValue() throws NumberFormatException
	{
		return Double.parseDouble(aDurationValue.getText());
	}
	
	/**
	 * Get duration unit (us, ms, s)
	 * @return Duration unit
	 */
	public String getDurationUnit()
	{
		return aDurationUnit.getSelectedItem().toString();
	}
	
	/**
	 * Return transmission speed
	 * @return Transmission speed
	 */
	public int getSpeed()
	{
		return Integer.parseInt(aSpeed.getSelectedItem().toString());
	}
	
	/**
	 * Append next configuration data
	 * 
	 * @param file - file that contains data to read
	 * @throws IOException 
	 */
	public void appendPreview(String data)
	{
		aPreview.append(data + "\n");
	}
	
	/**
	 * Clear preview
	 */
	public void clearPreview()
	{
		aPreview.setText(null);
	}

	/** 
	 * Precision button group
	 */
	ButtonGroup aPrecisionGroup;
	/**
	 * Port combo-box
	 */
	JComboBox aPortCheck;
	/**
	 * Time value
	 */
	private JTextField aDurationValue;
	/**
	 * Time unit combo-box
	 */
	private JComboBox aDurationUnit;
	/**
	 * Data preview
	 */
	private JTextArea aPreview;
	/**
	 * Transmission speed combo box
	 */
	private JComboBox aSpeed;
	/**
	 * Serialized constant
	 */
	private static final long serialVersionUID = 8281321276860893898L;

}
