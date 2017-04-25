package analysisStuff;

import java.awt.Point;

/**
 * A Ring is an object with a width, start/end points, and direction. The
 * direction corresponds to which direction we were moving in when we found the
 * ring
 * 
 * @author JonathanThomas
 * @version 4/28/17
 */
public class Ring
{
	private int width;
	private Point start, end;
	private String direction;

	/**
	 * Ring's constructor. A ring has a starting and ending point, a width, as
	 * well as a direction
	 * 
	 * @param start
	 *            - point we first detected the ring
	 * @param end
	 *            - last point we detected the ring's darker pixels
	 * @param width
	 *            - the length, in amount of pixels, of the ring
	 * @param direction
	 *            - direction we were measuring in when the ring was detected
	 */
	public Ring(Point start, Point end, int width, String direction)
	{
		this.width = width;
		this.start = start;
		this.end = end;
		this.direction = direction;
	}

	/**
	 * returns the ring's width
	 * 
	 * @return ring width in pixels
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * returns the direction we were going when the ring was detected
	 * 
	 * @return string representing direction (Left, Right, Up, or Down)
	 */
	public String getDirection()
	{
		return this.direction;
	}

	/**
	 * returns the starting point
	 * 
	 * @return point we first detected the ring
	 */
	public Point getStart()
	{
		return start;
	}

	/**
	 * returns the ending point
	 * 
	 * @return last pixel position that was darker than the threshold
	 */
	public Point getEnd()
	{
		return end;
	}
}
