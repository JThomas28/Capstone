
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

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}

		public GUIPanel()
		{
			setLayout(new GridLayout(7, 1));
			
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
					//TODO make sure user selects an image
					secondGUI(pathToImage.getText());
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
		
		public void secondGUI(String picture)
		{
			removeAll();
			
			getContentPane().setLayout(new FlowLayout());
			
			JPanel panel = new JPanel();
			panel.setBackground(Color.GRAY);
			
			JPanel panel2 = new JPanel();
			
			panel.setLayout(new GridLayout(5, 1));
			panel2.setLayout(new FlowLayout());
			
			add(new JLabel(new ImageIcon(picture)));
			
			panel.add(new JLabel("Estimated age: "));
			panel.add(new JLabel("Greater than normal growth years: "));
			panel.add(new JLabel("Less than normal growth years: "));
			JButton moreInfo = new JButton("Click Here for More Info");
			panel.add(moreInfo);
			
			JButton addAnother = new JButton("Click Here To Add Another Image To Analyze");
			panel2.add(addAnother);
			
			moreInfo.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						openWebPage.main(null);
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			});
			
			addAnother.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					//TODO allow user to add another image to analyze
				}
				
			});
			
			add(panel);
			add(panel2);
			revalidate();
			repaint();	
		}
	}
	
	
	public GUI()
	{
		super(FRAME_TITLE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);

		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		//TODO add background image
		
		 JPanel panel = new GUIPanel();
		 contentPane.add(panel);
		 
		 //draw background image.
//		 try
//		 {
//		 imagePanel = new GUIPanel(PATH_TO_BACKGROUND_IMAGE);
//		 contentPane().add(panel);
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