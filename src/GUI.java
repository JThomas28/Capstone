import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

@SuppressWarnings("serial")
public class GUI extends JFrame implements Constants
{
	private class GUIPanel extends JPanel
	{
		//Backround fields
		JPanel panelHoldingBackgroundImage = new JPanel();
		JLabel backgroundImageLabel = new JLabel();
		
		//component fields
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

			//componentPanel.setBackground(Color.WHITE);
			componentPanel.setLayout(new GridLayout(8, 1));
			componentPanel.setOpaque(true);

			panelHoldingComponentPanel.add(componentPanel);
			backgroundImageLabel.add(panelHoldingComponentPanel);

			// components
			JLabel chooseFileText = new JLabel(CHOOSE_IMAGE_TEXT);
			JButton uploadButton = new JButton(BROWSE);
			JButton goButton = new JButton(GO);
			//JLabel fileFormatsAllowed = new JLabel(AVAILABLE_FILE_FORMATS);
			JTextField pathToImage = new JTextField(15);
			JLabel pathTextFieldLabel = new JLabel(PATH_TEXTFIELD);
			JLabel enterZipCode = new JLabel(ZIPCODE_TEXT);
			enterZipCode.setHorizontalAlignment(getWidth()/2);
			JTextField zipcode = new JTextField(5);
			

			//fileupload button listener
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

			//begin analysis
			goButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(pathToImage.getText().endsWith(".jpg") || 
							pathToImage.getText().endsWith(".png"))//valid image
						secondGUI(pathToImage.getText());
					else
						JOptionPane.showMessageDialog(getContentPane(),NOT_VALID_FILE);
				}
			});
			
			//add components to panel
			componentPanel.add(chooseFileText);
			componentPanel.add(uploadButton);
			componentPanel.add(pathTextFieldLabel);
			componentPanel.add(pathToImage);
			//componentPanel.add(fileFormatsAllowed);
			componentPanel.add(enterZipCode);
			componentPanel.add(zipcode);
			componentPanel.add(goButton);
			add(masterPanel);
		}

		public void secondGUI(String picture)
		{
			//TODO add background image to second GUI
			removeAll();
			setLayout(new GridLayout(2,1));

			JPanel panel = new JPanel();
			JPanel panel2 = new JPanel();

			panel.setLayout(new GridLayout(5, 1));
			panel2.setLayout(new FlowLayout());
			
			add(new JLabel(new ImageIcon(picture)));

			panel.add(new JLabel("Estimated age: 155 years"));
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
					//new GUI();
				}
			});

			add(panel);
			panel.add(panel2);
			//p.add(panel);
			//add(p);
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