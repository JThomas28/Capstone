
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
	//private JFrame frame2;
	private JLayeredPane layeredPane2;
	private JPanel panel1, panel2;
	
	private class GUIPanel extends JPanel
	{
		private final JFileChooser fileChooser = new JFileChooser();
		//fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		private Image image;
		
		//getImageFilePath
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
			//setSize(400, 400);
			//setBackground(Color.white);
			
			JButton uploadButton = new JButton();
			JLabel fileFormatsAllowed = new JLabel(AVAILABLE_FILE_FORMATS);

			uploadButton.setText(CHOOSE_IMAGE_TEXT);
			uploadButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					//open file explorer
//					try
//					{
//						Desktop.getDesktop().open(imageFile);
//					}
//					catch (IOException e1)
//					{
//						e1.printStackTrace();
//					}
				}
			});
			add(uploadButton);
			add(fileFormatsAllowed);

			uploadButton.setVisible(true);
			setFocusable(true);
		}
	}

	public GUI()
	{
		super(FRAME_TITLE);// title
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame2.setSize(screenSize);
		
		//JFrame frame = new JFrame();
		
		frame2.setLayout(new BorderLayout());
		
//		JLayeredPane layeredPane = new JLayeredPane();
//		JPanel backgroundPanel = new JPanel();
//		JPanel uploadPanel = new JPanel();
		
		//frame.add(layeredPane, BorderLayout.CENTER);
		frame2.add(layeredPane2, BorderLayout.CENTER);
		
//		JPanel panel;
//		//draw background image.
//		try
//		{
//			panel = new GUIPanel(BACKGROUND_IMAGE_NAME);
//			getContentPane().add(panel);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
		
		//add panel for uploading image
		//JPanel panelForUploading = new GUIPanel();
		//add(panelForUploading);
	}

	public static void main(String[] args)
	{
		//JFrame f = new GUI();
		frame2.setVisible(true);
	}
}