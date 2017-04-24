package UIStuff;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import analysisStuff.Detector;

/**
 * GUI contains the main user interface data and functionality. 
 * This is where the program begins and it calls other classes and methods as needed
 * @author JonathanThomas
 * @version 4/28/17
 */
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
		private ArrayList<Integer> maxEdges = null;

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
				Image titleImage = ImageIO.read(new File(TITLE_IMAGE));
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
					// make sure file is jpg or png image
					if (pathToImage.getText().endsWith(".jpg") || pathToImage.getText().endsWith(".png"))
					{
						File imageFile = new File(pathToImage.getText());
						originalFile = imageFile;
						myImage = getImageFromFile(imageFile);
						TreeObj myTree = new TreeObj(0, myImage);

						choosePoints(myTree);
					}
					else
						JOptionPane.showMessageDialog(getContentPane(), NOT_VALID_FILE);
				}
			});

			// add components to panel
			componentPanel.add(chooseFileText);
			componentPanel.add(uploadButton);
			componentPanel.add(pathTextFieldLabel);
			componentPanel.add(pathToImage);
			componentPanel.add(fileFormatsAllowed);
			componentPanel.add(goButton);
			add(masterPanel);
		}

		/**
		 * returns buffered image from file passed in
		 * 
		 * @param imageFile
		 *            - file to read from
		 * @return bufferedImage contained in the file
		 */
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
				JOptionPane.showConfirmDialog(null, "Couldn't find image. Try again");
			}
			return myBuffImage;
		}

		/**
		 * Second 'page' of UI. Gets mouse cicks and passes info to the detector
		 * object to detect the rings
		 * 
		 * @param myTree
		 *            - tree object holding the image to analyze
		 */
		private void choosePoints(TreeObj myTree)
		{
			removeAll();

			Image treeImage = myTree.getPicture();
			BufferedImage myBuffImg = toBufferedImage(treeImage);

			this.myImage = treeImage;

			JPanel picPanel = new JPanel();
			picPanel.setLayout(new FlowLayout());

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

							// can't choose point too close to edge
							if (centerPoint.x < 4 || centerPoint.x > 496 || centerPoint.y < 4 || centerPoint.y > 496)
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

		/**
		 * Displays a joption pane with options on directions to measure in
		 * 
		 * @return boolean array with T/F values for each direction
		 */
		private boolean[] getEdgesToMeasureTo()
		{
			boolean[] myBools = new boolean[4];

			String select = SELECT_EDGES;
			JCheckBox right = new JCheckBox(RIGHT);
			JCheckBox left = new JCheckBox(LEFT);
			JCheckBox up = new JCheckBox(UP);
			JCheckBox down = new JCheckBox(DOWN);

			JCheckBox myBoxes[] = { right, left, up, down };
			Object[] params = { select, right, left, up, down };

			JOptionPane.showMessageDialog(null, params, CHOOSE_EDGES, JOptionPane.PLAIN_MESSAGE);
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

		/**
		 * Passes all the necessary data to detector to begin the pixel analysis
		 * 
		 * @param buffImg
		 *            - image to analyze
		 * @param start
		 *            - point chosen as starting point
		 * @param edges
		 *            - boolean array with direction to measure in data
		 * @param threshold
		 *            - color value of point chosen as threshold
		 * @param myTree
		 *            - tree object to analyze
		 */
		private void setDetector(BufferedImage buffImg, Point start, boolean[] edges, int threshold, TreeObj myTree)
		{
			Detector myDetector = new Detector(buffImg, start, edges, threshold);

			// get the age
			int age = myDetector.findAge();
			myTree.setAge(age);

			// set my image to the image with rings detected
			this.myImage = myDetector.getColorizedImage();
			myTree.setPicture(myDetector.getColorizedImage());

			// get the widest edges. To be used if user enters death year
			this.maxEdges = myDetector.getMaxPoints();

			// call method to show results
			resultGUI(myTree);
		}

		/**
		 * Converts image to bufferedImage
		 * 
		 * @param img
		 *            - image to be converted
		 * @return converted buffered image
		 */
		private BufferedImage toBufferedImage(Image img)
		{
			if (img instanceof BufferedImage)
			{
				return (BufferedImage) img;
			}
			BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D bGr = bimage.createGraphics();
			bGr.drawImage(img, 0, 0, null);
			bGr.dispose();

			return bimage;
		}

		/**
		 * Resizes image passed in to 500 by 500. 
		 * Useful because all images will be 500 pixels by 500
		 * @param originalImage - image passed in. (Pre-resize)
		 * @param type - type of image
		 * @return resized buffered image
		 */
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
			master.setPreferredSize(new Dimension(1000, 500));

			JPanel panel2 = new JPanel();
			JPanel panel = new JPanel();
			master.add(panel2);
			master.add(panel);

			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			panel2.setLayout(new GridBagLayout());

			JLabel image = new JLabel(new ImageIcon(myImage));

			JLabel information = new JLabel(
					"<html><div style='text-align: center;'>" + Constants.INFORMATION + "</div></html>");
			JButton moreInfo = new JButton(Constants.MORE_INFO);
			JLabel ageLabel = new JLabel("Estimated age: " + myTree.getAge() + "");
			JButton addDeathYear = new JButton(ADD_DEATH_YEAR);
			JLabel greaterGrowth = new JLabel();

			information.setFont(new Font("Serif", Font.BOLD, 20));
			ageLabel.setFont(new Font("Serif", Font.BOLD, 16));
			information.setAlignmentX(Component.CENTER_ALIGNMENT);

			panel.add(information);
			panel.add(Box.createRigidArea(new Dimension(0, 42)));
			panel.add(ageLabel);
			panel.add(Box.createRigidArea(new Dimension(0, 42)));
			panel2.add(image);

			moreInfo.setEnabled(false);
			if (myTree.getAge() <= 5 || maxEdges.size() == 0)
			{
				//need 5 rings before we can add a death year
				addDeathYear.setEnabled(false);
			}

			addDeathYear.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						boolean accepted = false;
						while (!accepted)
						{
							String ans = JOptionPane.showInputDialog(null, ENTER_ESTIMATE);
							if (ans.equals(JOptionPane.CANCEL_OPTION))
								break;
							else
							{
								try
								{
									int deathYear = 0;
									deathYear = Integer.parseInt(ans);
									if (deathYear < 1000)
									{
										JOptionPane.showMessageDialog(null, YEAR_REQUIREMENT);
									}
									else
									{
										moreInfo.setEnabled(true);
										accepted = true;
										String years = "";
										for (int i = 0; i < maxEdges.size() - 1; i++)
										{
											years += (deathYear - myTree.getAge()) + maxEdges.get(i) + ",";
										}
										years += deathYear - myTree.getAge() + maxEdges.get(maxEdges.size() - 1);
										greaterGrowth.setText(MORE_THAN_AVERAGE + (years));
										panel.add(greaterGrowth);
										
										panel.add(Box.createRigidArea(new Dimension(0, 42)));
										panel.add(Box.createRigidArea(new Dimension(0, 42)));
										panel.revalidate();
										panel.repaint();
									}
								}
								catch (NumberFormatException e2)
								{
									JOptionPane.showMessageDialog(null, "Year must be a number");
								}
							}
						}
					}
					catch (NullPointerException e3)
					{
					}
				}
			});
			
			//listener for moreinfo button. Opens webpage in default browser
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
			panel.add(Box.createRigidArea(new Dimension(0, 42)));
			panel.add(addDeathYear);
			panel.add(Box.createRigidArea(new Dimension(0, 42)));
			add(master);
			revalidate();
			repaint();
		}
	}

	/**
	 * Setup JFrame with all components/background
	 */
	public GUI()
	{
		super(FRAME_TITLE);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		//GUI fills screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);

		// set background
		setContentPane(new JLabel(new ImageIcon(PATH_TO_BACKGROUND_IMAGE)));

		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		JMenuBar menuBar = new JMenuBar();
		JMenu newUpload = new JMenu(NEW);
		JMenuItem newUploadButton = new JMenuItem(NEW_UPLOAD);
		JMenuItem newThreshold = new JMenuItem(NEW_THRESHOLD);

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
				int result = JOptionPane.showConfirmDialog(GUI.this, ANALYZE_NEW, CONFIRM,
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

	/**
	 * Creates and shows GUI
	 * @param args
	 */
	public static void main(String[] args)
	{
		JFrame f = new GUI();
		f.setVisible(true);
	}
}