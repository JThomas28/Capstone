package UIStuff;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import analysisStuff.FindRings;

/* Here we are first declaring our class that will act as the
 * base for other panels or in other terms the base for CardLayout.
 */

public class CardLayoutGUI extends JFrame
{
	private File imageFile;
    private static final String CARD_JBUTTON =  "Card JButton";
    private static final String CARD_JTEXTFIELD = "Card JTextField";    
    private static final String CARD_JRADIOBUTTON = "Card JRadioButton";

    private static void createAndShowGUI()
    {
        JFrame frame = new JFrame("Ring Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // This JPanel is the base for CardLayout for other JPanels.
        final JPanel contentPane = new JPanel();
        
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize);
		contentPane.setPreferredSize(screenSize);
		
		int width = screenSize.width;
		int height = screenSize.height;
		
        contentPane.setLayout(new CardLayout());

        /* Here we be making objects of the Window Series classes
         * so that, each one of them can be added to the JPanel 
         * having CardLayout. 
         */
        UploadPanel win1 = new UploadPanel();
        contentPane.add(win1, CARD_JBUTTON);
        ChoosePoints win2 = new ChoosePoints(win1.getTree());
        contentPane.add(win2, CARD_JTEXTFIELD);
//        Window3 win3 = new Window3();
//        contentPane.add(win3, CARD_JRADIOBUTTON);

        /* We need two JButtons to go to the next Card
         * or come back to the previous Card, as and when
         * desired by the User.
         */
        JPanel buttonPanel = new JPanel(); 
//        final JButton previousButton = new JButton("PREVIOUS");
//        final JButton nextButton = new JButton("NEXT");
//        buttonPanel.add(previousButton);
//        buttonPanel.add(nextButton);

        /* Adding the ActionListeners to the JButton,
         * so that the user can see the next Card or
         * come back to the previous Card, as desired.
         */
//        previousButton.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent ae)
//            {
//                CardLayout cardLayout = (CardLayout) contentPane.getLayout();
//                cardLayout.previous(contentPane);
//            }
//        });
//        nextButton.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent ae)
//            {
//                CardLayout cardLayout = (CardLayout) contentPane.getLayout();
//                cardLayout.next(contentPane);   
//            }
//        });

        // Adding the contentPane (JPanel) and buttonPanel to JFrame.
        frame.add(contentPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }
} 

class UploadPanel extends JPanel implements Constants
{
	JPanel panelHoldingBackgroundImage = new JPanel();
	JLabel backgroundImageLabel = new JLabel();

	// component fields
	JPanel panelHoldingComponentPanel = new JPanel();
	JPanel componentPanel = new JPanel();
    /*
     * Here this is our first Card of CardLayout, which will
     * be added to the contentPane object of JPanel, which
     * has the LayoutManager set to CardLayout.
     * This card consists of Two JButtons.
     */  
    private ActionListener action;
    private File imageFile;
    private TreeObj myTree;

    public TreeObj getTree()
    {
    	return myTree;
    }
    public File getImageFile()
    {
    	return imageFile;
    }
    
    public UploadPanel()
    {
        init();
    }

    private void init() 
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
					imageFile = new File(pathToImage.getText());
					try
					{
						// convert to grayscale image
						myBuffImage = ImageIO.read(imageFile);
						int type = myBuffImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : myBuffImage.getType();
						//myBuffImage = resizeImage(myBuffImage, type);

						ImageFilter filter = new GrayFilter(true, 50);
						ImageProducer producer = new FilteredImageSource(myBuffImage.getSource(), filter);
						myImage = Toolkit.getDefaultToolkit().createImage(producer);
					}
					catch (Exception exception)
					{
						exception.printStackTrace();
					}
					myTree = new TreeObj("Tree1", myImage);

					//ChoosePoints(myTree);

					//resultGUI(myTree);
				}
				else
					JOptionPane.showMessageDialog(null, NOT_VALID_FILE);
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
    
    private void choosePoints(TreeObj myTree)
	{
		removeAll();
		Image treeImage = myTree.getPicture();
		BufferedImage myBuffImg = toBufferedImage(treeImage);

		myImage = treeImage;

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
					numClicks = 2;
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

		// int threshold;
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
				// threshold = colorValue;
				FindRings myRings = new FindRings(toBufferedImage(myImage), centerPoint, edge, colorValue);
				int age = myRings.findAge();// returnAge();
				System.out.println(age);
				this.myImage = myRings.getColorizedImage();
				resultGUI(age);
				break;
		}
	}
}

class ChoosePoints extends JPanel
{
	private Image myImage;
	private int numClicks = 0;
	private Point centerPoint, edge;
    /*
     * Here this is our second Card of CardLayout, which will
     * be added to the contentPane object of JPanel, which
     * has the LayoutManager set to CardLayout.
     * This card consists of a JLabel and a  JTextField
     * with GridLayout.
     */  

    //private JTextField textField;

    public ChoosePoints(TreeObj myTree) 
    {
        init(myTree);
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
    
    private void init(TreeObj myTree) 
    {
    	Image treeImage = myTree.getPicture();
		BufferedImage myBuffImg = toBufferedImage(treeImage);

		myImage = treeImage;

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
					System.out.println(x + ", " + y);
				}
				catch (ArrayIndexOutOfBoundsException exc)
				{
					JOptionPane.showMessageDialog(null, "Must choose point on image");
					numClicks = 2;
				}
			}
			
		});

		add(picPanel);
		revalidate();
		repaint();
		JOptionPane.showMessageDialog(null, "Click center of tree cookie");
        //setLayout(new GridLayout(1, 2));
//        JLabel userLabel = new JLabel("Your Name : ");
//        textField = new JTextField();
//        textField.addActionListener(this);
//
//        add(userLabel);
//        add(textField);
    }
    
    private void setPoint(int clickNum, int x, int y, int colorValue)
	{
		// TODO create a new findRings object using this data

		// int threshold;
		switch (clickNum)
		{
			case 1:
				// this point is the center
				centerPoint = new Point(x, y);
				JOptionPane.showMessageDialog(null, "Great! Now choose the edge to measure to");
				break;
			case 2:
				// this is the edge point
				edge = new Point(x, y);
				JOptionPane.showMessageDialog(null,
						"Almost done! Now choose a point between the rings. "
								+ "This will be used as a threshold, to detect the darker color of the rings.");
				break;
			case 3:
				// this is the threshold. Don't care where it is, just the
				// colorValue
				// threshold = colorValue;
				FindRings myRings = new FindRings(toBufferedImage(myImage), centerPoint, edge, colorValue);
				int age = myRings.findAge();// returnAge();
				System.out.println(age);
				this.myImage = myRings.getColorizedImage();
				//resultGUI(age);
				break;
		}
	}
}

class Window3 extends JPanel
{
    /*
     * Here this is our third Card of CardLayout, which will
     * be added to the contentPane object of JPanel, which
     * has the LayoutManager set to CardLayout.
     * This card consists of Two JLabels and two JCheckBox
     * with GridLayout.
     */  
    private ActionListener state;

    public Window3()
    {
        init();
    }

    public void init()
    {
        setLayout(new GridLayout(2, 2));
        JLabel maleLabel = new JLabel("MALE", JLabel.CENTER);
        final JCheckBox maleBox = new JCheckBox();
        JLabel femaleLabel = new JLabel("FEMALE", JLabel.CENTER);
        final JCheckBox femaleBox = new JCheckBox();

        state = new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                if (maleBox == (JCheckBox) ae.getSource())
                {
                    femaleBox.setSelected(false);
                    JOptionPane.showMessageDialog(null, "Congrats you are a Male"
                                                , "Gender : ", JOptionPane.INFORMATION_MESSAGE);                            
                }
                else if (femaleBox == (JCheckBox) ae.getSource())
                {
                    maleBox.setSelected(false);
                    JOptionPane.showMessageDialog(null, "Congrats you are a Female"
                                            , "Gender : ", JOptionPane.INFORMATION_MESSAGE);                        
                }
            }
        };

        maleBox.addActionListener(state);
        femaleBox.addActionListener(state);
        add(maleLabel);
        add(maleBox);
        add(femaleLabel);
        add(femaleBox);
    }
}