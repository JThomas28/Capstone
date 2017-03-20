import java.io.*;
import java.awt.*;
import javax.swing.*;

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
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		int returnVal = fc.showOpenDialog(FileChooser.this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			chosenFile = file;
		}
	}

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