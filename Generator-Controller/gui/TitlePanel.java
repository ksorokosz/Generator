package gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author kamil
 * Title panel
 */
public class TitlePanel extends JPanel
{
	/**
	 * TitlePanel class constructor
	 */
	public TitlePanel() 
	{
		JLabel aLabel = new JLabel(MainWindow.aTitle);
		Font aFont = new Font("Comic Sans", Font.BOLD | Font.ITALIC, 20);
		aLabel.setFont(aFont);
		aLabel.setAlignmentY(CENTER_ALIGNMENT);
		aLabel.setAlignmentX(CENTER_ALIGNMENT);
		JPanel aTitle = new JPanel();
		aTitle.add(aLabel);
		super.add(aTitle,BorderLayout.CENTER);
	}
	
	/**
	 * Serialized constant
	 */
	private static final long serialVersionUID = -4717298241074799432L;
	
}
