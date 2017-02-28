import java.io.IOException;
import java.net.URL;

import javax.swing.*;

public class openWebPage
{
	public static void main(String [] args) throws IOException
	{
		URL url = new URL("https://www.wunderground.com/history/");
		//URL urlConnect = url.openConnection();
		JEditorPane webpage = new JEditorPane(url);//"https://www.wunderground.com/history/");
		
		JFrame webpageFrame = new JFrame("Weather History");
		webpageFrame.add(new JScrollPane(webpage));
		webpage.setEditable(false);
		
		webpageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		webpageFrame.setVisible(true);
	}
}
