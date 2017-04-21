package UIStuff;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class TreeObj
{
	private Image treeImage;
	private int age;
	
	public TreeObj(int age, Image myImage)
	{
		this.age = age;
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
	
	public int getAge()
	{
		return this.age;
	}
	
	public void setAge(int age)
	{
		this.age = age;
	}
}
