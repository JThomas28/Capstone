package analysisStuff;

import java.awt.Point;

/**
 * A Ring is an object with a width, start/end points, and direction. The
 * direction corresponds to which direction we were moving in when we found the
 * ring
 * 
 * @author JonathanThomas
 *
 */
public class Ring
{
	private int width;
	private Point start, end;
	private String direction;

	public Ring(Point start, Point end, int width, String direction)
	{
		this.width = width;
		this.start = start;
		this.end = end;
		this.direction = direction;
	}

	public int getWidth()
	{
		return width;
	}

	public String getDirection()
	{
		return this.direction;
	}

	public Point getStart()
	{
		return start;
	}

	public Point getEnd()
	{
		return end;
	}
}
