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
	private ArrayList<Ring> rings, betweenRings;
	private ArrayList<Integer> maxPointsIndex;
	private boolean right, left, up, down;

	/**
	 * Detector's constructor. initializes all local variables
	 * 
	 * @param myImage
	 *            - image to analyze
	 * @param startingPoint
	 *            - point chosen as center point
	 * @param edges
	 *            - boolean array telling which direction to search in
	 * @param myThreshold
	 *            - threshold value chosen by user
	 */
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
		this.maxPointsIndex = new ArrayList<Integer>();
		this.myThreshold = myThreshold;
		this.startingPoint = startingPoint;
	}

	/**
	 * This is the search algorithm. It searches, pixel by pixel, for pixels
	 * that are darker than the threshold pixel. If it finds one, it increments
	 * the ring count. Also adds the width of non-rings to an arraylist to be
	 * used later for growth analysis
	 * 
	 * @param x
	 *            - positive if we're searching right, negative if searching
	 *            left, and 0 if we're searching vertically
	 * @param y
	 *            - positive if searching down (pixel's y value INCREASES from
	 *            top of pic to bottom), negative if searching up, and 0 if
	 *            searching vertically
	 * @return estimated age of the tree
	 */
	public int search(int x, int y)
	{
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
				Ring currentRing = new Ring(start, end, width, getDirection(x, y));
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

		} while (inBounds(currentPixel.x, currentPixel.y));
		return ringCount;
	}

	/**
	 * Returns a string (Right, Left, Up, or Down) based on the x and y values
	 * passed in
	 * 
	 * @param x
	 *            - negative if measuring left, positive if right, 0 otherwise
	 * @param y
	 *            - negative if measuring up, positive if down, 0 otherwise
	 * @return string representing the direction we're measuring
	 */
	public String getDirection(int x, int y)
	{
		switch (x)
		{
			case 1:
				return rightString;
			case -1:
				return leftString;
			default:
				// x is 0, moving in y
				if (y == -1)
					return upString;
				else
					return downString;
		}
	}

	/**
	 * Find age returns the age of the tree represented by an image. Searches in
	 * all chosen directions and averages results together if multiple
	 * directions were chosen
	 * 
	 * @return estimated age of tree
	 */
	public int findAge()
	{
		int sum = 0;
		int numOfDirections = 0;

		if (this.right)
		{
			sum += search(1, 0);
			splitBetweenRings();
			numOfDirections++;
		}

		if (this.left)
		{
			sum += search(-1, 0);
			splitBetweenRings();
			numOfDirections++;
		}

		if (this.up)
		{
			sum += search(0, -1);
			splitBetweenRings();
			numOfDirections++;
		}

		if (this.down)
		{
			sum += search(0, 1);
			splitBetweenRings();
			numOfDirections++;
		}
		return sum / numOfDirections;
	}

	/**
	 * returns array containing index of the widest areas between rings. Used to
	 * estimate more than average growth years
	 * 
	 * @return arraylist representing more than average growth years
	 */
	public ArrayList<Integer> getMaxPoints()
	{
		return maxPointsIndex;
	}

	/**
	 * puts the index of the widest non-ring in the maxPointsIndex array. Used
	 * later to backdate estimated greater than average growth years
	 */
	private void splitBetweenRings()
	{
		int max = 0, tmpIndex = 0;
		for (int i = 0; i < betweenRings.size(); i++)
		{
			if (betweenRings.get(i).getWidth() > max)
			{
				max = betweenRings.get(i).getWidth();
				tmpIndex = i;
			}
			betweenRings.remove(i);
		}
		maxPointsIndex.add(tmpIndex);
	}

	/**
	 * IsRing tests if a value is darker than our threshold. It divides all
	 * values by 100000 to reduce the range of values and produce more accurate
	 * results
	 * 
	 * @param pixel
	 *            - pixel to test
	 * @param threshold
	 *            - threshold to test pixel against
	 * @return boolean - true if the pixel is darker than the threshold (is a
	 *         ring), false otherwise
	 */
	private boolean isRing(Point pixel, int threshold)
	{
		int moddedThreshold = threshold / normalizeValue;
		int moddedPixel = myImage.getRGB(pixel.x, pixel.y) / normalizeValue;

		if (moddedThreshold > moddedPixel)// darker pixel
			return true;
		return false;
	}

	/**
	 * Returns the colored image. Used in GUI to get the image with rings and
	 * non-rings marked
	 * 
	 * @return bufferedImage representing colored image
	 */
	public BufferedImage getColorizedImage()
	{
		return this.myImage;
	}

	/**
	 * inbounds returns a boolean signifying whether or not the point passed in
	 * is, in fact, on the 500 by 500 pixel image we are analyzing
	 * 
	 * @param x
	 *            - x value/position
	 * @param y
	 *            - y value/position
	 * @return true if the point is on the image, false otherwise
	 */
	private boolean inBounds(int x, int y)
	{
		if (x > 0 && x < 499 && y < 499 && y > 0)
			return true;
		else
			return false;
	}
}