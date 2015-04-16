package johmphot.card;

import java.io.Serializable;
import java.lang.String;

public class Card implements Serializable
{
    private static final long serialVersionUID = 1L;

    public int value, image ;
	public String name;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

    public Card(int value, int image, String name)
    {
		this.value = value;
		this.image = image;
		this.name = name;
	}

}