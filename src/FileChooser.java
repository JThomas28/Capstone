import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class FileChooser extends JPanel
{
	JFileChooser fc;
	private static File chosenFile;

	public FileChooser()
	{
		super(new BorderLayout());

		// Create a file chooser
		fc = new JFileChooser();

		// Uncomment one of the following lines to try a different
		// file selection mode. The first allows just directories
		// to be selected (and, at least in the Java look and feel,
		// shown). The second allows both files and directories
		// to be selected. If you leave these lines commented out,
		// then the default mode (FILES_ONLY) will be used.
		//
		// fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		int returnVal = fc.showOpenDialog(FileChooser.this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			chosenFile = file;
			// This is where a real application would open the file.
			//log.append("Opening: " + file.getName() + "." + newline);
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
//	protected static ImageIcon createImageIcon(String path)
//	{
//		java.net.URL imgURL = FileChooser.class.getResource(path);
//		if (imgURL != null)
//		{
//			return new ImageIcon(imgURL);
//		}
//		else
//		{
//			System.err.println("Couldn't find file: " + path);
//			return null;
//		}
//	}

	private static void createAndShowGUI()
	{
		// Create and set up the window.
		JPanel pane = new JPanel();
		pane.add(new FileChooser());
		pane.setVisible(true);
	}

	public static File getFile()
	{
		return chosenFile;
	}

	public static void main(String[] args)
	{
		createAndShowGUI();
	}
}