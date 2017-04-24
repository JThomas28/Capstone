package UIStuff;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class is responsible for opening the webpage
 * 	 containing temperature data for every state in the US
 * @author JonathanThomas
 * @version 4/28/17
 *
 */
public class openWebPage implements Constants
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			Desktop.getDesktop().browse(new URI(WEBPAGE));
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
