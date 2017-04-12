package analysisStuff;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class FindRings
{
	private BufferedImage myImage;
	private int myThreshold;
	private int ringCount = 0;
	Point startingPoint, endingPoint;
	
	public FindRings(BufferedImage myImage, Point startingPoint, Point endingPoint, int myThreshold)
	{
		this.myImage = myImage;
		this.myThreshold = myThreshold;
		this.startingPoint = startingPoint;
		this.endingPoint = endingPoint;
	}
	
	public int returnAge()
	{
		findAge();
		return ringCount;
	}
	private void findAge()
	{
		Point currentPixel = new Point(this.startingPoint.x, this.startingPoint.y);
		//int ringCount = 0;
		if(startingPoint.x < endingPoint.x)
		{
			//measuring left to right
			if(startingPoint.y < endingPoint.y)
			{
				//moving down
				for(int startX = currentPixel.x; startX < endingPoint.x; startX++)
				{
					currentPixel.setLocation(currentPixel.x + 1, currentPixel.y);
					if(isRing(currentPixel, this.myThreshold))
					{
						ringCount++;
						while(myImage.getRGB(currentPixel.x, currentPixel.y) - 50000 < this.myThreshold)
						{
							currentPixel.setLocation(currentPixel.x + 1, currentPixel.y);
						}
					}
				}
			}
		}
		//return ringCount;
	}
	private boolean isRing(Point pixel, int threshold)
	{
		if(myImage.getRGB(pixel.x, pixel.y) < threshold - 50000)
		{
			return true;
		}
		else
			return false;
	}
}
