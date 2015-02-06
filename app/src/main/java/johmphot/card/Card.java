package johmphot.card;

import java.lang.String;public class Card {

	public int value, cardReference ;
	public String name, category;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getCardReference() {
		return cardReference;
	}
	public void setCardReference(int cardReference) {
		this.cardReference = cardReference;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Card(int value, int cardReference, String name, String category) {
		//super();
		this.value = value;
		this.cardReference = cardReference;
		this.name = name;
		this.category = category;
	}
	
}