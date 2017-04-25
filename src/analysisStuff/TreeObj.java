package analysisStuff;

import java.awt.Image;

/**
 * TreeObj is a tree object class. A tree has an age and an image
 * 
 * @author JonathanThomas
 * @version 4/28/17
 */
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
