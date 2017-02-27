
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class GUI extends JFrame implements Constants
{
	private class GUIPanel extends JPanel
	{
		private Image image;

		// getImageFilePath
		public GUIPanel(String imgString) throws IOException
		{
			image = ImageIO.read(new File(imgString));
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}

		public GUIPanel()
		{
			setLayout(new FlowLayout());
			setPreferredSize(new Dimension(250, 250));
			setBackground(Color.BLUE);
			
			JPanel panel1 = new JPanel();
			JPanel panel2 = new JPanel();
			JPanel panel3 = new JPanel();

			JLabel chooseFileText = new JLabel(CHOOSE_IMAGE_TEXT);
			JButton uploadButton = new JButton(BROWSE);
			JButton goButton = new JButton(GO);
			//JLabel fileFormatsAllowed = new JLabel(AVAILABLE_FILE_FORMATS);
			JTextField pathToImage = new JTextField(15);
			JLabel pathTextFieldLabel = new JLabel(PATH_TEXTFIELD);
			
			JLabel enterZipCode = new JLabel(ZIPCODE_TEXT);
			JTextField zipcode = new JTextField(5);

			//chooseFileText.setText(CHOOSE_IMAGE_TEXT);
			//uploadButton.setText(BROWSE);// CHOOSE_IMAGE_TEXT);
			uploadButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					 //open file explorer
//					 try
//					 {
//					 Desktop.getDesktop().open(imageFile);
//					 }
//					 catch (IOException e1)
//					 {
//					 e1.printStackTrace();
//					 }
				}
			});
			add(panel1);
			panel1.add(chooseFileText);
			panel1.add(uploadButton);
			panel1.add(pathTextFieldLabel);
			panel2.add(pathToImage);
			//add(fileFormatsAllowed);
			panel2.add(enterZipCode);
			panel2.add(zipcode);
			panel3.add(goButton);
		}
	}

	public GUI()
	{
		super(FRAME_TITLE);// title
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);

		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 90));

		JPanel panel = new GUIPanel();
		getContentPane().add(panel);

		// JPanel panel;
		// draw background image.
		// try
		// {
		// panel = new GUIPanel(PATH_TO_BACKGROUND_IMAGE);
		// getContentPane().add(panel);
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }
	}

	public static void main(String[] args)
	{
		JFrame f = new GUI();
		f.setVisible(true);
	}
}