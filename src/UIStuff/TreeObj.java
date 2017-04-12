package UIStuff;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class TreeObj
{
	private int age, estDeathYear;
	private String name;
	private Image treeImage;
	
	public TreeObj(String name, Image myImage)
	{
		this.name = name;
		this.treeImage = myImage;
	}
	
	public Image getPicture()
	{
		return treeImage;
	}
	
	public void setPicture(Image newPic)
	{
		treeImage = newPic;
	}
}
