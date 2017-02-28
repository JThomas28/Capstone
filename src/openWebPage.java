import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
//import java.net.URL;

//import javax.swing.*;

public class openWebPage
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			Desktop.getDesktop().browse(new URI("https://www.wunderground.com/history/"));
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
