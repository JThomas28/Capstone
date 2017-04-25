package UIStuff;

import java.io.*;
import java.awt.*;
import javax.swing.*;

/**
 * FileChooser is the class responsible for opening the upload dialog box. It
 * allows users to navigate to the image they would like to upload and select
 * it.
 * 
 * @author JonathanThomas
 * @version 4/28/17
 */
@SuppressWarnings("serial")
public class FileChooser extends JPanel
{
	JFileChooser fc;
	private static File chosenFile;

	/**
	 * Creates a filechooser object
	 */
	public FileChooser()
	{
		super(new BorderLayout());

		// Create a file chooser
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		int returnVal = fc.showOpenDialog(FileChooser.this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			chosenFile = file;
		}
	}

	/**
	 * Creates and shows the panel used for uploadiing
	 */
	private static void createAndShowGUI()
	{
		// Create and set up the window.
		JPanel pane = new JPanel();
		pane.add(new FileChooser());
		pane.setVisible(true);
	}

	/**
	 * returns the file we chose to upload
	 * 
	 * @return chosenFile - file chosen to upload
	 */
	public static File getFile()
	{
		return chosenFile;
	}

	/**
	 * invokes the method to create and show the upload window
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		createAndShowGUI();
	}
}