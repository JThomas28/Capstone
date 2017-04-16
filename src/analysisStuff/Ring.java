package analysisStuff;

public class Ring
{
	private int width;
	private Boolean largerThanAverage;
	private int start, end;
	
	public Ring(int startX, int endX, int width)
	{
		this.width = width;
		this.start = startX;
		this.end = endX;
		this.largerThanAverage = null;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getStart()
	{
		return start;
	}
	
	public int getEnd()
	{
		return start;
	}
	
	public void setLargerThanAverage(Boolean b)
	{
		//null if equals average, true if more than average, false if less than average
		this.largerThanAverage = b;
	}
	
}
