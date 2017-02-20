
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
		//@SuppressWarnings("unused")
		public GUIPanel(String imgString) throws IOException
		{
			image = ImageIO.read(new File(imgString));
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			g.drawImage(image, 0, 0, this);
		}
		public GUIPanel()
		{
//			File imageFile = new File("/");
//			//setSize(500, 500);
//			setBackground(Color.blue);
//			
//			JPanel panel2 = new JPanel();
//			panel2.setSize(400, 400);
//			panel2.setBackground(Color.BLACK);
//			
//			JButton uploadButton = new JButton();
//			JLabel fileFormatsAllowed = new JLabel(AVAILABLE_FILE_FORMATS);
//
//			uploadButton.setText(CHOOSE_IMAGE_TEXT);
//			uploadButton.addActionListener(new ActionListener()
//			{
//				@Override
//				public void actionPerformed(ActionEvent e)
//				{
//					try
//					{
//						Desktop.getDesktop().open(imageFile);
//					}
//					catch (IOException e1)
//					{
//						e1.printStackTrace();
//					}
//				}
//			});
//			add(uploadButton);
//			add(fileFormatsAllowed);
//
//			uploadButton.setVisible(true);
//			setFocusable(true);
		}
	}

	public GUI()
	{
		super(FRAME_TITLE);// title
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//setLayout(new FlowLayout(FlowLayout.CENTER));
		//setLayout(new GridLayout(1, 2, 3, 3));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		
		JPanel panel = new GUIPanel();
		//add(panel, BorderLayout.CENTER);
		add(panel);
		
		//draw background image.
//		class imagePanel extends JComponent 
//		{
//			
//			@Override
//			protected void paintComponent(Graphics g)
//			{
//				super.paintComponent(g);
//				try
//				{
//					Image img = ImageIO.read(new File("/Users/JonathanThomas/Desktop/treeImage.png"));
//					g.drawImage(img, 0, 0, this);
//				}
//				catch(IOException e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
	}

	public static void main(String[] args)
	{
		JFrame f = new GUI();
		f.getContentPane().add(new GUIPanel("/Users/JonathanThomas/Desktop/treeImage.png"));
		f.setVisible(true);
	}
}