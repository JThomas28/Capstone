package UIStuff;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import analysisStuff.FindRings;

@SuppressWarnings("serial")
public class GUI extends JFrame implements Constants
{
	private class GUIPanel extends JPanel
	{
		private int numClicks = 0;
		private Image myImage;
		private Point centerPoint = null;
		private Point edge = null;

		// Backround fields
		JPanel panelHoldingBackgroundImage = new JPanel();
		JLabel backgroundImageLabel = new JLabel();

		// component fields
		JPanel panelHoldingComponentPanel = new JPanel();
		JPanel componentPanel = new JPanel();

		public GUIPanel()
		{
			JPanel masterPanel = new JPanel();
			setLayout(new BorderLayout());
			panelHoldingBackgroundImage.setLayout(new BorderLayout());
			backgroundImageLabel.setLayout(new BorderLayout());

			try
			{
				// set background image
				backgroundImageLabel.setIcon(new ImageIcon("forestBackground.jpg"));
			}
			catch (Exception e)
			{
				// catch exception where image isn't found
				e.printStackTrace();
			}
			// add image to panel
			panelHoldingBackgroundImage.add(backgroundImageLabel);

			masterPanel.add(panelHoldingBackgroundImage);

			panelHoldingComponentPanel.setLayout(new GridBagLayout());
			panelHoldingComponentPanel.setOpaque(false);

			componentPanel.setLayout(new GridLayout(6, 1));
			componentPanel.setOpaque(true);

			panelHoldingComponentPanel.add(componentPanel);
			backgroundImageLabel.add(panelHoldingComponentPanel);

			// components
			JLabel chooseFileText = new JLabel(CHOOSE_IMAGE_TEXT);
			JButton uploadButton = new JButton(BROWSE);
			JButton goButton = new JButton(GO);
			JLabel fileFormatsAllowed = new JLabel(AVAILABLE_FILE_FORMATS);
			JTextField pathToImage = new JTextField(15);
			JLabel pathTextFieldLabel = new JLabel(PATH_TEXTFIELD);

			// fileupload button listener
			uploadButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					FileChooser.main(null);
					pathToImage.setText(FileChooser.getFile().getAbsolutePath());
					pathToImage.setEditable(false); // can't change file path
				}
			});

			// begin analysis
			goButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (pathToImage.getText().endsWith(".jpg") || pathToImage.getText().endsWith(".png"))
					{
						BufferedImage myBuffImage = null;
						Image myImage = null;
						File imageFile = new File(pathToImage.getText());
						try
						{
							// convert to grayscale image
							myBuffImage = ImageIO.read(imageFile);
							int type = myBuffImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : myBuffImage.getType();
							myBuffImage = resizeImage(myBuffImage, type);

							ImageFilter filter = new GrayFilter(true, 50);
							ImageProducer producer = new FilteredImageSource(myBuffImage.getSource(), filter);
							myImage = Toolkit.getDefaultToolkit().createImage(producer);

						}
						catch (Exception exception)
						{
							exception.printStackTrace();
						}
						TreeObj myTree = new TreeObj("Tree1", myImage);

						choosePoints(myTree);

						// resultGUI(myTree);
					}
					else
						JOptionPane.showMessageDialog(getContentPane(), NOT_VALID_FILE);
				}
			});

			// add components to panel
			// TODO erase this line. Just using it to make testing easier
			pathToImage.setText("/Users/JonathanThomas/Desktop/Trees/treePic.jpg");

			componentPanel.add(chooseFileText);
			componentPanel.add(uploadButton);
			componentPanel.add(pathTextFieldLabel);
			componentPanel.add(pathToImage);
			componentPanel.add(fileFormatsAllowed);
			componentPanel.add(goButton);
			add(masterPanel);
		}

		private void choosePoints(TreeObj myTree)
		{
			removeAll();
			Image treeImage = myTree.getPicture();
			BufferedImage myBuffImg = toBufferedImage(treeImage);

			this.myImage = treeImage;

			JPanel picPanel = new JPanel();
			picPanel.setLayout(new FlowLayout());

			// myBuffImg = resizeImage(myBuffImg, type);
			picPanel.add(new JLabel(new ImageIcon(myBuffImg)));

			picPanel.addMouseListener(new MouseListener()
			{
				@Override
				public void mousePressed(MouseEvent e)
				{}

				@Override
				public void mouseReleased(MouseEvent e)
				{}

				@Override
				public void mouseEntered(MouseEvent e)
				{}

				@Override
				public void mouseExited(MouseEvent e)
				{}

				@Override
				public void mouseClicked(MouseEvent e)
				{
					try
					{
						numClicks++;
						int x = e.getX();
						int y = e.getY();
						int threshold = 0;

						if (numClicks == 3)
							threshold = myBuffImg.getRGB(x, y);

						setPoint(numClicks, x, y, threshold);
						// toBufferedImage(myTree.getPicture()).setRGB(x, y,
						// Color.RED.getRGB());
						// repaint();
						System.out.println(x + ", " + y);
					}
					catch (ArrayIndexOutOfBoundsException exc)
					{
						JOptionPane.showMessageDialog(getContentPane(), "Must choose point on image");
					}
				}
			});

			add(picPanel);
			revalidate();
			repaint();
			JOptionPane.showMessageDialog(getContentPane(), "Click center of tree cookie");

		}

		private void setPoint(int clickNum, int x, int y, int colorValue)
		{
			// TODO create a new findRings object using this data

			int threshold;
			switch (clickNum)
			{
				case 1:
					// this point is the center
					centerPoint = new Point(x, y);
					JOptionPane.showMessageDialog(getContentPane(), "Great! Now choose the edge to measure to");
					break;
				case 2:
					// this is the edge point
					edge = new Point(x, y);
					JOptionPane.showMessageDialog(getContentPane(),
							"Almost done! Now choose a point between the rings. "
									+ "This will be used as a threshold, to detect the darker color of the rings.");
					break;
				case 3:
					// this is the threshold. Don't care where it is, just the
					// colorValue
					threshold = colorValue;
					FindRings myRings = new FindRings(toBufferedImage(myImage), centerPoint, edge, threshold);
					int age = myRings.returnAge();
					System.out.println(age);
					resultGUI(age);
					break;
			}
		}

		private BufferedImage toBufferedImage(Image img)
		{
			if (img instanceof BufferedImage)
			{
				return (BufferedImage) img;
			}

			// Create a buffered image with transparency
			BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);

			// Draw the image on to the buffered image
			Graphics2D bGr = bimage.createGraphics();
			bGr.drawImage(img, 0, 0, null);
			bGr.dispose();

			// Return the buffered image
			return bimage;
		}

		private BufferedImage resizeImage(BufferedImage originalImage, int type)
		{
			BufferedImage resizedImage = new BufferedImage(500, 500, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, 500, 500, null);
			g.dispose();

			return resizedImage;
		}

		public void resultGUI(int age)// TreeObj myTree)
		{
			// TODO add background image to second GUI
			removeAll();
			// setLayout(new GridLayout(2, 1));
			setLayout(new FlowLayout());

			JPanel panel = new JPanel();
			JPanel panel2 = new JPanel();

			panel.setLayout(new GridLayout(5, 1));
			panel2.setLayout(new FlowLayout());

			add(new JLabel(new ImageIcon(myImage)));// myTree.getPicture());

			// myTree.getPicture().getGraphics().add(new JLabel(new
			// ImageIcon(myTree.getPicture())));
			// add(new JLabel(new ImageIcon(picture)));

			panel.add(new JLabel("Estimated age: " + age + "years old"));
			panel.add(new JLabel("Above Average growth years: 1996, 1998"));
			panel.add(new JLabel("Below Average growth years: 2002"));
			JButton moreInfo = new JButton("More Info");
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
					// TODO allow user to add another image to analyze
					// new GUI();
				}
			});

			add(panel);
			panel.add(panel2);
			// p.add(panel);
			// add(p);
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

		JPanel panel = new GUIPanel();
		contentPane.add(panel);
	}

	public static void main(String[] args)
	{
		JFrame f = new GUI();
		f.setVisible(true);
	}
}