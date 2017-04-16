package analysisStuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Detector
{

	private BufferedImage myImage;
	private int myThreshold;
	Point startingPoint, endingPoint;
	private ArrayList<Ring> rings;
	private boolean right, left, up, down;

	public Detector(BufferedImage myImage, Point startingPoint, boolean [] edges, int myThreshold)
		{
		//right, left, up, down
		this.right = edges[0];
		this.left = edges[1];
		this.up = edges[2];
		this.down = edges[3];
		this.myImage = myImage;
		this.rings = new ArrayList<Ring>();
		this.myThreshold = myThreshold;
		this.startingPoint = startingPoint;
		}

	private void searchRight()
	{
		
	}
	private void searchLeft()
	{
		
	}
	private void searchUp()
	{
		
	}
	private void searchDown()
	{
		
	}
	public int findAge()
	{
		if(this.right)
			searchRight();
		if(this.left)
			searchLeft();
		if(this.up)
			searchUp();
		if(this.down)
			searchDown();
		Point currentPixel = new Point(this.startingPoint.x, this.startingPoint.y);
		int ringCount = 0;
		if (startingPoint.x < endingPoint.x)
		{
			// measuring left to right
			if (startingPoint.y < endingPoint.y)
			{
				// moving down
				while (currentPixel.x < endingPoint.x)
				{
					currentPixel.setLocation(currentPixel.x + 1, currentPixel.y);
					if (isRing(currentPixel, this.myThreshold))
					{
						ringCount++;
						Point start = currentPixel;
						while (isRing(currentPixel, this.myThreshold) && // myImage.getRGB(currentPixel.x,
																			// currentPixel.y)
																			// -
																			// 50000
																			// <
																			// this.myThreshold
								inBounds(currentPixel.x, currentPixel.y))
						{
							// this.myImage.setRGB(currentPixel.x,
							// currentPixel.y, 4, 4, colorRed, 0, 0);
							this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.RED.getRGB());
							currentPixel.setLocation(currentPixel.x + 1, currentPixel.y);
						}
						Point end = currentPixel;
						Ring currentRing = new Ring(start.x, end.x, end.x - start.x);
						this.rings.add(currentRing);
					}
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.blue.getRGB());
				}
			}
		}
		// zoomImage(startingPoint.x - 100, 400, 0, 500);
		return ringCount;
	}

	private int averageRingWidth()
	{
		int sum = 0;
		int average = 0;
		for (Ring ring : rings)
		{
			sum += ring.getWidth();
		}
		average = sum / rings.size();
		return average;
	}

	private boolean isRing(Point pixel, int threshold)
	{
		int moddedThreshold = threshold / 60000;
		int moddedPixel = myImage.getRGB(pixel.x, pixel.y) / 60000;
		if (moddedThreshold > moddedPixel)
			return true;
		return false;
	}

	private void zoomImage(int x1, int x2, int y1, int y2)
	{
		BufferedImage subImage = this.myImage.getSubimage(x1, y1, x2 - x1, y2 - y1);
		Graphics2D graphics2D = (Graphics2D) this.myImage.getGraphics();
		graphics2D.drawImage(subImage, 0, 0, 500, 500, 0, 0, x2 - x1, y2 - y1, null);
		// clean up
		graphics2D.dispose();
	}

	public BufferedImage getColorizedImage()
	{
		return this.myImage;
	}

	private boolean inBounds(int x, int y)
	{
		if (x >= 0 && x <= 500 && y <= 500 && y >= 0)
			return true;
		else
			return false;
	}

}
