import java.io.IOException;

import javax.swing.*;

public class openWebPage
{
	public static void main(String [] args) throws IOException
	{
		JEditorPane webpage = new JEditorPane("https://www.wunderground.com/history/");
		
		JFrame webpageFrame = new JFrame("Weather History");
		webpageFrame.add(new JScrollPane(webpage));
		
		webpageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		webpageFrame.setVisible(true);
	}
}
