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

import analysisStuff.Detector;

public class GUI extends JFrame implements Constants
{
	GUIPanel uploadPanel;

	private class GUIPanel extends JPanel
	{
		private int numClicks = 0;
		private Image myImage;
		private Point centerPoint = null;
		private boolean[] myEdges;

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
			pathToImage.setText("/Users/JonathanThomas/Desktop/Trees/dense_rings.jpg");

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

						if (numClicks == 1)
						{
							// center point
							centerPoint = new Point(x, y);
							myEdges = getEdgesToMeasureTo();
							JOptionPane.showMessageDialog(getContentPane(),
									"Almost done! Now choose a point between the rings.\n"
											+ "This will be used as a threshold, to detect the darker color of the rings.");
						}

						else
						{
							threshold = myBuffImg.getRGB(x, y);
							setDetector(myBuffImg, centerPoint, myEdges, threshold);
						}
						System.out.println(x + ", " + y);
					}
					catch (ArrayIndexOutOfBoundsException exc)
					{
						JOptionPane.showMessageDialog(getContentPane(), "Must choose point on image");
						numClicks = 2;
					}
				}
			});

			add(picPanel);
			revalidate();
			repaint();
			JOptionPane.showMessageDialog(getContentPane(), "Click center of tree cookie");

		}

		private void setDetector(BufferedImage buffImg, Point start, boolean[] edges, Integer threshold)
		{
			Detector myDetector = new Detector(buffImg, start, edges, threshold);
			this.myImage = myDetector.getColorizedImage();
			resultGUI(myDetector.findAge());
		}

		private boolean[] getEdgesToMeasureTo()
		{
			boolean[] myBools = new boolean[4];
			String select = "Select which edges to measure to";
			JCheckBox right = new JCheckBox("Right");
			JCheckBox left = new JCheckBox("Left");
			JCheckBox up = new JCheckBox("Up");
			JCheckBox down = new JCheckBox("Down");
			JCheckBox myBoxes[] = { right, left, up, down };
			Object[] params = { select, right, left, up, down };

			JOptionPane.showMessageDialog(null, params, "Choose Edges", JOptionPane.PLAIN_MESSAGE);
			for (int i = 0; i < 4; i++)
				myBools[i] = myBoxes[i].isSelected();

			boolean somethingSelected = false;
			for (int i = 0; i < myBools.length; i++)
			{
				if (myBools[i])
					somethingSelected = true;
			}

			if (somethingSelected)
				return myBools;
			else
				return getEdgesToMeasureTo();
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

			add(new JLabel(new ImageIcon(myImage)));

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
					// new GUIPanel();
				}
			});
			add(panel);
			panel.add(panel2);
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

		JMenuBar menuBar = new JMenuBar();
		JMenu newUpload = new JMenu("New");
		JMenuItem newUploadButton = new JMenuItem("New Upload");

		newUpload.add(newUploadButton);
		menuBar.add(newUpload);
		setJMenuBar(menuBar);

		GUIPanel panel = new GUIPanel();

		newUploadButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int result = JOptionPane.showConfirmDialog(GUI.this, "Analyze new image?", "Confirm",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION)
				{
					// start new game
					contentPane.removeAll();

					// contentPane.add(new GUIPanel());
					contentPane.repaint();
				}
			}
		});
		contentPane.add(panel);
	}

	public static void main(String[] args)
	{
		JFrame f = new GUI();
		f.setVisible(true);
	}
}