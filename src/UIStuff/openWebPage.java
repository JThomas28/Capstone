package UIStuff;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class openWebPage
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			Desktop.getDesktop().browse(new URI("https://www.ncdc.noaa.gov/temp-and-precip/state-temps/"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}
}
