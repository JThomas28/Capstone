package analysisStuff;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class AnalysisGUI extends JPanel
{
	public AnalysisGUI(BufferedImage image)
	{
		
	}
	
	private BufferedImage resizeImage(BufferedImage originalImage, int type)
	{
		BufferedImage resizedImage = new BufferedImage(500, 500, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 500, 500, null);
		g.dispose();

		return resizedImage;
	}
}
