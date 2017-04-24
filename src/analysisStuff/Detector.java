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

	private int searchRight()
	{
		int ringCount = 0;
		Point currentPixel = startingPoint;
		do
		{
			currentPixel = new Point(currentPixel.x + 1, currentPixel.y);
			if (isRing(currentPixel, this.myThreshold))
			{
				ringCount++;
				Point start = currentPixel;
				while (isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x + 1, currentPixel.y))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x, currentPixel.y - 1, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x, currentPixel.y + 1, Color.RED.getRGB());
					currentPixel.setLocation(currentPixel.x + 1, currentPixel.y);
				}
				Point end = currentPixel;
				Ring currentRing = new Ring(start, end, end.x - start.x, "Right");
				this.rings.add(currentRing);
			}
			else
			{
				int width = 0;
				Point start = currentPixel;
				while (!isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x + 1, currentPixel.y))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.BLUE.getRGB());
					this.myImage.setRGB(currentPixel.x, currentPixel.y - 1, Color.BLUE.getRGB());
					this.myImage.setRGB(currentPixel.x, currentPixel.y + 1, Color.BLUE.getRGB());
					width++;
					currentPixel.setLocation(currentPixel.x + 1, currentPixel.y);
				}
				Point end = currentPixel;
				Ring betweenRings = new Ring(start, end, width, "Right");
				this.betweenRings.add(betweenRings);
			}
			this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.blue.getRGB());
			this.myImage.setRGB(currentPixel.x, currentPixel.y - 1, Color.blue.getRGB());
			this.myImage.setRGB(currentPixel.x, currentPixel.y + 1, Color.blue.getRGB());
		} while (currentPixel.x < 499);
		return ringCount;
	}

	private int searchLeft()
	{
		int ringCount = 0;
		Point currentPixel = startingPoint;
		do
		{
			currentPixel = new Point(currentPixel.x - 1, currentPixel.y);
			if (isRing(currentPixel, this.myThreshold))
			{
				ringCount++;
				Point start = currentPixel;
				while (isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x - 1, currentPixel.y))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x, currentPixel.y - 1, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x, currentPixel.y + 1, Color.RED.getRGB());
					currentPixel.setLocation(currentPixel.x - 1, currentPixel.y);
				}
				Point end = currentPixel;
				Ring currentRing = new Ring(start, end, end.x - start.x, "Left");
				this.rings.add(currentRing);
			}
			else
			{
				int width = 0;
				Point start = currentPixel;
				while (!isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x - 1, currentPixel.y))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.blue.getRGB());
					this.myImage.setRGB(currentPixel.x, currentPixel.y - 1, Color.blue.getRGB());
					this.myImage.setRGB(currentPixel.x, currentPixel.y + 1, Color.blue.getRGB());
					width++;
					currentPixel.setLocation(currentPixel.x - 1, currentPixel.y);
				}
				Point end = currentPixel;
				Ring betweenRings = new Ring(start, end, width, "Left");
				this.betweenRings.add(betweenRings);
			}
			this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.blue.getRGB());
			this.myImage.setRGB(currentPixel.x, currentPixel.y - 1, Color.blue.getRGB());
			this.myImage.setRGB(currentPixel.x, currentPixel.y + 1, Color.blue.getRGB());
		} while (currentPixel.x > 0);

		return ringCount;
	}

	private int searchUp()
	{
		int ringCount = 0;
		Point currentPixel = startingPoint;
		do
		{
			currentPixel = new Point(currentPixel.x, currentPixel.y - 1);
			if (isRing(currentPixel, this.myThreshold))
			{
				ringCount++;
				Point start = currentPixel;
				while (isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x, currentPixel.y - 1))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x - 1, currentPixel.y, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x + 1, currentPixel.y, Color.RED.getRGB());
					currentPixel.setLocation(currentPixel.x, currentPixel.y - 1);
				}
				Point end = currentPixel;
				Ring currentRing = new Ring(start, end, end.y - start.y, "Up");
				this.rings.add(currentRing);
			}
			else
			{
				int width = 0;
				Point start = currentPixel;
				while (!isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x, currentPixel.y - 1))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.BLUE.getRGB());
					this.myImage.setRGB(currentPixel.x - 1, currentPixel.y, Color.BLUE.getRGB());
					this.myImage.setRGB(currentPixel.x + 1, currentPixel.y, Color.BLUE.getRGB());
					width++;
					currentPixel.setLocation(currentPixel.x, currentPixel.y - 1);
				}
				Point end = currentPixel;
				Ring betweenRings = new Ring(start, end, width, "Up");
				this.betweenRings.add(betweenRings);
			}
			this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.BLUE.getRGB());
			this.myImage.setRGB(currentPixel.x - 1, currentPixel.y, Color.BLUE.getRGB());
			this.myImage.setRGB(currentPixel.x + 1, currentPixel.y, Color.BLUE.getRGB());
		} while (currentPixel.y > 0);
		return ringCount;
	}

	private int searchDown()
	{
		int ringCount = 0;
		Point currentPixel = startingPoint;
		do
		{
			currentPixel = new Point(currentPixel.x, currentPixel.y + 1);
			if (isRing(currentPixel, this.myThreshold))
			{
				ringCount++;
				Point start = currentPixel;
				while (isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x, currentPixel.y + 1))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x - 1, currentPixel.y, Color.RED.getRGB());
					this.myImage.setRGB(currentPixel.x + 1, currentPixel.y, Color.RED.getRGB());
					currentPixel.setLocation(currentPixel.x, currentPixel.y + 1);
				}
				Point end = currentPixel;
				Ring currentRing = new Ring(start, end, end.y - start.y, "Down");
				this.rings.add(currentRing);
			}
			else
			{
				int width = 0;
				Point start = currentPixel;
				while (!isRing(currentPixel, this.myThreshold) && inBounds(currentPixel.x, currentPixel.y + 1))
				{
					this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.BLUE.getRGB());
					this.myImage.setRGB(currentPixel.x - 1, currentPixel.y, Color.BLUE.getRGB());
					this.myImage.setRGB(currentPixel.x + 1, currentPixel.y, Color.BLUE.getRGB());
					width++;
					currentPixel.setLocation(currentPixel.x, currentPixel.y + 1);
				}
				Point end = currentPixel;
				Ring betweenRings = new Ring(start, end, width, "Down");
				this.betweenRings.add(betweenRings);
			}

			this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.BLUE.getRGB());
			this.myImage.setRGB(currentPixel.x - 1, currentPixel.y, Color.BLUE.getRGB());
			this.myImage.setRGB(currentPixel.x + 1, currentPixel.y, Color.BLUE.getRGB());
		} while (currentPixel.y < 499);
		return ringCount;
	}

	public int findAge()
	{
		int sum = 0;
		int numOfDirections = 0;
		if (this.right)
		{
			sum += searchRight();
			int max = 0;
			int index = 0;
			splitBetweenRings();
			for (int i = 0; i < rightBetweenRings.size(); i++)
			{
				Ring curr = rightBetweenRings.get(i);
				if (curr.getDirection().equals("Right"))
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
			sum += searchLeft();
			int max = 0;
			int index = 0;
			splitBetweenRings();
			for (int i = 0; i < leftBetweenRings.size(); i++)
			{
				Ring curr = leftBetweenRings.get(i);
				if (curr.getDirection().equals("Left"))
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
			sum += searchUp();
			int max = 0;
			int index = 0;
			splitBetweenRings();
			for (int i = 0; i < upBetweenRings.size(); i++)
			{
				Ring curr = upBetweenRings.get(i);
				if (curr.getDirection().equals("Up"))
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
			sum += searchDown();
			int max = 0;
			int index = 0;
			splitBetweenRings();
			for (int i = 0; i < downBetweenRings.size(); i++)
			{
				Ring curr = downBetweenRings.get(i);
				if (curr.getDirection().equals("Down"))
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
			if (ring.getDirection().equals("Down"))
			{
				downBetweenRings.add(ring);
			}
			else if (ring.getDirection().equals("Left"))
			{
				leftBetweenRings.add(ring);
			}
			else if (ring.getDirection().equals("Right"))
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
		if (x > 0 && x < 500 && y < 500 && y > 0)
			return true;
		else
			return false;
	}
}
