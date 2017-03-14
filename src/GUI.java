
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
//		public GUIPanel(String imgString) throws IOException
//		{
//			image = ImageIO.read(new File(imgString));
//		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}

		public GUIPanel()
		{
			setLayout(new GridLayout(7, 1));
			//setBackground(Color.BLUE);
			
			JLabel chooseFileText = new JLabel(CHOOSE_IMAGE_TEXT);
			JButton uploadButton = new JButton(BROWSE);
			JButton goButton = new JButton(GO);
			//JLabel fileFormatsAllowed = new JLabel(AVAILABLE_FILE_FORMATS);
			JTextField pathToImage = new JTextField(15);
			JLabel pathTextFieldLabel = new JLabel(PATH_TEXTFIELD);
			
			JLabel enterZipCode = new JLabel(ZIPCODE_TEXT);
			JTextField zipcode = new JTextField(5);

			uploadButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					FileChooser.main(null);
					pathToImage.setText(FileChooser.getFile().getAbsolutePath());
					pathToImage.setEditable(false); //can't change file path
				}
			});
			
			goButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					secondGUI(pathToImage.getText());
//					try
//					{
//						openWebPage.main(null);
//					}
//					catch (IOException e1)
//					{
//						e1.printStackTrace();
//					}
				}
				
			});
			add(chooseFileText);
			add(uploadButton);
			add(pathTextFieldLabel);
			add(pathToImage);
			//add(fileFormatsAllowed);
			add(enterZipCode);
			add(zipcode);
			add(goButton);
		}
	}
	
	public void secondGUI(String picture)
	{
		setLayout(new GridLayout(1, 2));//one section for picture, one for data found
		
		add(new JLabel(new ImageIcon(picture)));
		
		add(new JLabel("Estimated age: "));
		add(new JButton("Click Here for More Info"));
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

//		 JPanel panel;
//		 //draw background image.
//		 try
//		 {
//		 panel = new GUIPanel(PATH_TO_BACKGROUND_IMAGE);
//		 getContentPane().add(panel);
//		 }
//		 catch (IOException e)
//		 {
//		 e.printStackTrace();
//		 }
	}

	public static void main(String[] args)
	{
		JFrame f = new GUI();
		f.setVisible(true);
	}
}