package analysisStuff;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Detector is the object used to analyze each pixel. A detector consists of an
 * image, starting point, array of directions, and a threshold value
 * 
 * @author JonathanThomas
 * @version 4/28/17
 */
public class Detector
{
	private final int normalizeValue = 100000;
	private final String downString = "Down";
	private final String upString = "Up";
	private final String leftString = "Left";
	private final String rightString = "Right";

	private BufferedImage myImage;
	private int myThreshold;
	private Point startingPoint;
	private ArrayList<Ring> rings, betweenRings, downBetweenRings, leftBetweenRings, rightBetweenRings, upBetweenRings;
	private ArrayList<Integer> maxPointsIndex;
	private boolean right, left, up, down;

	public Detector(BufferedImage myImage, Point startingPoint, boolean[] edges, int myThreshold)
	{
		// right, left, up, down
		this.right = edges[0];
		this.left = edges[1];
		this.up = edges[2];
		this.down = edges[3];
		this.myImage = myImage;
		this.rings = new ArrayList<Ring>();
		this.betweenRings = new ArrayList<Ring>();
		this.downBetweenRings = new ArrayList<Ring>();
		this.leftBetweenRings = new ArrayList<Ring>();
		this.rightBetweenRings = new ArrayList<Ring>();
		this.upBetweenRings = new ArrayList<Ring>();
		this.maxPointsIndex = new ArrayList<Integer>();
		this.myThreshold = myThreshold;
		this.startingPoint = startingPoint;
	}
	
	public int search(int x, int y)
	{
		//x is 0 if we're moving up/down
		//x is negative if moving left, positive for moving right
		//same for y direction
		int ringCount = 0;
		Point currentPixel = startingPoint;
		do
		{
			currentPixel = new Point(currentPixel.x + x, currentPixel.y + y);
			if (isRing(currentPixel, this.myThreshold))
			{
				ringCount++;
				Point start = currentPixel;
				int width = 0;
				while (isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x + x, currentPixel.y + y))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x - y, currentPixel.y - x, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x + y, currentPixel.y + x, Color.RED.getRGB());
					width++;
					currentPixel.setLocation(currentPixel.x + x, currentPixel.y + y);
				}
				Point end = currentPixel;
				Ring currentRing = new Ring(start, end, width, getDirection(x,y));
				this.rings.add(currentRing);
			}
			else
			{
				int width = 0;
				Point start = currentPixel;
				while (!isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x + x, currentPixel.y + y))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.BLUE.getRGB());
					this.myImage.setRGB(currentPixel.x - y, currentPixel.y - x, Color.BLUE.getRGB());
					this.myImage.setRGB(currentPixel.x + y, currentPixel.y + x, Color.BLUE.getRGB());
					width++;
					currentPixel.setLocation(currentPixel.x + x, currentPixel.y + y);
				}
				Point end = currentPixel;
				Ring betweenRings = new Ring(start, end, width, getDirection(x, y));
				this.betweenRings.add(betweenRings);
			}
			this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.BLUE.getRGB());
			this.myImage.setRGB(currentPixel.x - y, currentPixel.y - x, Color.BLUE.getRGB());
			this.myImage.setRGB(currentPixel.x + y, currentPixel.y + x, Color.BLUE.getRGB());
			
		}
		while(inBounds(currentPixel.x, currentPixel.y));
		return ringCount;
	}
	
	public String getDirection(int x, int y)
	{
		switch (x)
		{		
			case 1:
				return rightString;
			case -1:
				return leftString;
			default: 
				//x is 0, moving in y
				if(y == -1)
					return upString;
				else
					return downString;
		}
	}

	public int findAge()
	{
		int sum = 0;
		int numOfDirections = 0;
		if (this.right)
		{
			sum += search(1, 0);
			int max = 0;
			int index = 0;
			splitBetweenRings();
			for (int i = 0; i < rightBetweenRings.size(); i++)
			{
				Ring curr = rightBetweenRings.get(i);
				if (curr.getDirection().equals(rightString))
				{
					if (curr.getWidth() > max)
					{
						max = curr.getWidth();
						index = i;
					}
				}
			}
			if (index > 0)
			{
				maxPointsIndex.add(index);
			}
			numOfDirections++;
		}

		if (this.left)
		{
			sum += search(-1, 0);
			int max = 0;
			int index = 0;
			splitBetweenRings();
			for (int i = 0; i < leftBetweenRings.size(); i++)
			{
				Ring curr = leftBetweenRings.get(i);
				if (curr.getDirection().equals(leftString))
				{
					if (curr.getWidth() > max)
					{
						max = curr.getWidth();
						index = i;
					}
				}
			}
			if (index > 0)
			{
				maxPointsIndex.add(index);
			}
			numOfDirections++;
		}

		if (this.up)
		{
			sum += search(0, -1);
			int max = 0;
			int index = 0;
			splitBetweenRings();
			for (int i = 0; i < upBetweenRings.size(); i++)
			{
				Ring curr = upBetweenRings.get(i);
				if (curr.getDirection().equals(upString))
				{
					if (curr.getWidth() > max)
					{
						max = curr.getWidth();
						index = i;
					}
				}
			}
			if (index > 0)
			{
				maxPointsIndex.add(index);
			}
			numOfDirections++;
		}

		if (this.down)
		{
			sum += search(0, 1);
			int max = 0;
			int index = 0;
			splitBetweenRings();
			for (int i = 0; i < downBetweenRings.size(); i++)
			{
				Ring curr = downBetweenRings.get(i);
				if (curr.getDirection().equals(downString))
				{
					if (curr.getWidth() > max)
					{
						max = curr.getWidth();
						index = i;
					}
				}
			}
			if (index > 0)
			{
				maxPointsIndex.add(index);
			}
			numOfDirections++;
		}
		return sum / numOfDirections;
	}

	public ArrayList<Integer> getMaxPoints()
	{
		return maxPointsIndex;
	}

	private void splitBetweenRings()
	{
		for (Ring ring : betweenRings)
		{
			if (ring.getDirection().equals(downString))
			{
				downBetweenRings.add(ring);
			}
			else if (ring.getDirection().equals(leftString))
			{
				leftBetweenRings.add(ring);
			}
			else if (ring.getDirection().equals(rightString))
			{
				rightBetweenRings.add(ring);
			}
			else
			{
				upBetweenRings.add(ring);
			}
		}
	}

	private boolean isRing(Point pixel, int threshold)
	{
		int moddedThreshold = threshold / normalizeValue;
		int moddedPixel = myImage.getRGB(pixel.x, pixel.y) / normalizeValue;
		
		if (moddedThreshold > moddedPixel)// darker pixel
			return true;
		return false;
	}

	public BufferedImage getColorizedImage()
	{
		return this.myImage;
	}

	private boolean inBounds(int x, int y)
	{
		if(x > 0 && x < 499 && y < 499 && y > 0)
			return true;
		else
			return false;
	}
}
