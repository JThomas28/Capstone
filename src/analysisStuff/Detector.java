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
	private Point startingPoint;
	private ArrayList<Ring> rings;
	private ArrayList<Ring> rightRings, leftRings, upRings, downRings;
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
				// this.rightRings.add(currentRing);
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
			this.myImage.setRGB(currentPixel.x, currentPixel.y, Color.BLUE.getRGB());
			this.myImage.setRGB(currentPixel.x - 1, currentPixel.y, Color.BLUE.getRGB());
			this.myImage.setRGB(currentPixel.x + 1, currentPixel.y, Color.BLUE.getRGB());
		} while (currentPixel.y < 499);
		return ringCount;
	}

	public int findAge()
	{
		// TODO starting point is getting changed. so if we go right first then
		// up,
		// it works for going right, but then starting point is 499 and we go up
		// the right edge of the picture
		int sum = 0;
		int numOfDirections = 0;
		if (this.right)
		{
			sum += searchRight();
			numOfDirections++;
		}

		if (this.left)
		{
			sum += searchLeft();
			numOfDirections++;
		}

		if (this.up)
		{
			sum += searchUp();
			numOfDirections++;
		}

		if (this.down)
		{
			sum += searchDown();
			numOfDirections++;
		}
		return sum / numOfDirections;
	}

	public void getLargestFive()
	{
		// int[] large = new int[5];
		int max = 0;

		if (rings.size() > 5)
		{
			for (int j = 0; j < 5; j++)
			{
				for (int i = 0; i < rings.size(); i++)
				{
					Ring currRing = rings.get(j);
					if (currRing.getWidth() > max)
					{
						max = currRing.getWidth();
					}
				}

			}
		}
	}

	private void repaintRing(Ring ring)
	{
		String direction = ring.getDirection();

		if (direction.equals("Up"))
		{
			Point initial = ring.getStart();
			Point end = ring.getEnd();
			while (initial.y > end.y)
			{
				this.myImage.setRGB(initial.x, initial.y, Color.GREEN.getRGB());
				initial.setLocation(initial.x, initial.y--);
			}
		}
		else if (direction.equals("Down"))
		{
			Point initial = ring.getStart();
			Point end = ring.getEnd();
			while (initial.y < end.y)
			{
				this.myImage.setRGB(initial.x, initial.y, Color.GREEN.getRGB());
				initial.setLocation(initial.x, initial.y++);
			}
		}
		else if (direction.equals("Left"))
			;
		{
			Point initial = ring.getStart();
			Point end = ring.getEnd();
			while (initial.x > end.x)
			{
				this.myImage.setRGB(initial.x, initial.y, Color.GREEN.getRGB());
				initial.setLocation(initial.x, initial.y++);
			}
		}
		// else
		// {
		// //right
		// Point initial = ring.getStart();
		// Point end = ring.getEnd();
		// while(initial.x < end.x)
		// {
		// this.myImage.setRGB(initial.x, initial.y, Color.GREEN.getRGB());
		// initial.setLocation(initial.x, initial.y++);
		// }
		// }
	}

	private boolean isRing(Point pixel, int threshold)
	{
		int moddedThreshold = threshold / 100000;
		int moddedPixel = myImage.getRGB(pixel.x, pixel.y) / 100000;
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
