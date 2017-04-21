package UIStuff;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import analysisStuff.Detector;

@SuppressWarnings("serial")
public class GUI extends JFrame implements Constants
{
	GUIPanel uploadPanel;

	private class GUIPanel extends JPanel
	{
		private int numClicks = 0;
		private Image myImage;
		private Point centerPoint = null;
		private boolean[] myEdges;
		private File originalFile = null;

		public GUIPanel()
		{
			JPanel masterPanel = new JPanel(new GridLayout(2, 1));
			JPanel titlePanel = new JPanel(new GridBagLayout());
			JPanel componentPanel = new JPanel();
			JLabel imageLabel = new JLabel();
			titlePanel.add(imageLabel);
			masterPanel.add(titlePanel);
			masterPanel.add(componentPanel);

			componentPanel.setLayout(new GridLayout(6, 1));
			getContentPane().setLayout(new GridBagLayout());
			JLabel chooseFileText = new JLabel(CHOOSE_IMAGE_TEXT);
			JButton uploadButton = new JButton(BROWSE);
			JButton goButton = new JButton(GO);
			JLabel fileFormatsAllowed = new JLabel(AVAILABLE_FILE_FORMATS);
			JTextField pathToImage = new JTextField(15);
			JLabel pathTextFieldLabel = new JLabel(PATH_TEXTFIELD);

			try
			{
				// add image to title page
				Image titleImage = ImageIO.read(new File("thomas_profile.png"));
				Image newImage = titleImage.getScaledInstance(200, 200, 200);
				imageLabel.setIcon(new ImageIcon(newImage));
				titlePanel.add(imageLabel);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}

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
						File imageFile = new File(pathToImage.getText());
						originalFile = imageFile;
						myImage = getImageFromFile(imageFile);
						// myImage = grayscaleImage(imageFile);
						TreeObj myTree = new TreeObj(0, myImage);

						choosePoints(myTree);
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

		public BufferedImage getImageFromFile(File imageFile)
		{
			BufferedImage myBuffImage = null;
			try
			{
				myBuffImage = ImageIO.read(imageFile);
				int type = myBuffImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : myBuffImage.getType();
				myBuffImage = resizeImage(myBuffImage, type);

			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
			return myBuffImage;
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
						// TODO don't allow a click near edges since we're
						// setting pixels to right, left, above, below current
						// pixel
						numClicks++;
						int x = e.getX();
						int y = e.getY();
						int threshold = 0;

						if (numClicks == 1)
						{
							// center point
							centerPoint = new Point(x, y);
							if (centerPoint.x < 4 || centerPoint.x > 496 || centerPoint.y < 4 || centerPoint.x > 496)
							{
								JOptionPane.showMessageDialog(getContentPane(),
										"Point too close to edge, choose anoter point closer to center of image");
								numClicks = 0;
							}
							else
							{
								myEdges = getEdgesToMeasureTo();
								JOptionPane.showMessageDialog(getContentPane(),
										"Almost done! Now choose a point between the rings.\n"
												+ "This will be used as a threshold, to detect the darker color of the rings.");
							}
						}

						else
						{
							threshold = myBuffImg.getRGB(x, y);
							setDetector(myBuffImg, centerPoint, myEdges, threshold, myTree);
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

		private void setDetector(BufferedImage buffImg, Point start, boolean[] edges, int threshold, TreeObj myTree)
		{
			Detector myDetector = new Detector(buffImg, start, edges, threshold);
			int age = myDetector.findAge();
			myTree.setAge(age);
			// myDetector.getLargestFive();
			this.myImage = myDetector.getColorizedImage();
			myTree.setPicture(myDetector.getColorizedImage());
			resultGUI(myTree);
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

		public void resultGUI(TreeObj myTree)
		{
			removeAll();
			setBackground(Color.DARK_GRAY);
			setLayout(new FlowLayout());
			JPanel master = new JPanel(new GridLayout(1, 2));
			master.setPreferredSize(new Dimension(1000, 700));

			JPanel panel2 = new JPanel();
			JPanel panel = new JPanel();
			master.add(panel2);
			master.add(panel);

			panel.setLayout(new GridLayout(6, 1));
			panel2.setLayout(new GridBagLayout());

			JLabel image = new JLabel(new ImageIcon(myImage));

			JLabel information = new JLabel(
					"<html><div style='text-align: center;'>" + Constants.INFORMATION + "</div></html>");
			JButton moreInfo = new JButton(Constants.MORE_INFO);
			JLabel ageLabel = new JLabel("Estimated age: " + myTree.getAge() + "");
			JButton addDeathYear = new JButton("Add Death Year");
			JLabel greaterGrowth = new JLabel("Greater than average growth years: ");
			JLabel lesserGrowth = new JLabel("Greater than average growth years: ");

			information.setFont(new Font("Serif", Font.BOLD, 20));
			ageLabel.setFont(new Font("Serif", Font.BOLD, 16));

			panel.add(information);
			panel.add(ageLabel);
			panel2.add(image);

			moreInfo.setEnabled(false);
			if (myTree.getAge() <= 5)
			{
				addDeathYear.setEnabled(false);
			}

			addDeathYear.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					boolean inputAccepted = false;
					while (!inputAccepted)
					{
						int deathYear = 0;
						try
						{
							deathYear = Integer
									.parseInt(JOptionPane.showInputDialog(null, "Enter estimated death year"));
							if (deathYear < 1000)
							{
								JOptionPane.showMessageDialog(null, "Year must be more than 1000");
							}
							else
							{
								inputAccepted = true;
								moreInfo.setEnabled(true);
								// TODO call method to calculate >avg, <avg
								// TODO fix this so it neatly displays
								// everything
								panel.add(greaterGrowth);
								panel.add(lesserGrowth);
								panel.revalidate();
								panel.repaint();
								break;
							}
						}
						catch (NumberFormatException e1)
						{
							JOptionPane.showMessageDialog(null, "Year must be an integer");
						}
					}
				}
			});
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
			panel.add(moreInfo);
			panel.add(addDeathYear);
			add(master);
			revalidate();
			repaint();
		}
	}

	public GUI()
	{
		super(FRAME_TITLE);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// set background
		setContentPane(new JLabel(new ImageIcon("forestBackground.jpg")));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);

		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		JMenuBar menuBar = new JMenuBar();
		JMenu newUpload = new JMenu("New");
		JMenuItem newUploadButton = new JMenuItem("New Upload");
		JMenuItem newThreshold = new JMenuItem("New Threshold");

		newUpload.add(newUploadButton);
		newUpload.add(newThreshold);
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
					FileChooser.main(null);
					panel.originalFile = FileChooser.getFile();
					panel.myImage = panel.getImageFromFile(FileChooser.getFile());
					panel.numClicks = 0;
					BufferedImage myBuffImage = panel.toBufferedImage(panel.myImage);
					int type = myBuffImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : myBuffImage.getType();
					panel.myImage = panel.resizeImage(myBuffImage, type);
					panel.repaint();
					panel.choosePoints(new TreeObj(0, panel.myImage));
					getContentPane().add(panel);
				}
			}
		});

		newThreshold.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				panel.myImage = panel.getImageFromFile(panel.originalFile);
				panel.numClicks = 0;
				BufferedImage myBuffImage = panel.toBufferedImage(panel.myImage);
				int type = myBuffImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : myBuffImage.getType();
				panel.myImage = panel.resizeImage(myBuffImage, type);
				panel.repaint();
				panel.choosePoints(new TreeObj(0, panel.myImage));
				getContentPane().add(panel);
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