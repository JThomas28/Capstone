package components;
 
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

//import java.awt.BorderLayout;
//import java.awt.Insets;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import javax.swing.filechooser.*;
//
//import javax.swing.*;

public class JFileChooser extends JPanel implements ActionListener
{
	JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fc;
    
    public JFileChooser()
    {
    	super(new BorderLayout());
    	
    	log = new JTextArea(5, 20);
    	log.setMargin(new Insets(5, 5, 5, 5));
    	log.setEditable(false);
    	JScrollPane logScrollPane = new JScrollPane(log);
    	
    	fc = new JFileChooser();
    	//fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    	
    	openButton = new JButton("Choose Image to analyze...");
    	openButton.addActionListener(this);
    	
    	JPanel buttonPanel = new JPanel();
    	buttonPanel.add(openButton);
    	
    	add(buttonPanel, BorderLayout.PAGE_START);
    	add(logScrollPane, BorderLayout.CENTER);
    }
    
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == openButton)
		{
			int returnVal = fc.
		}
	}

}
