package johmphot.card;

import java.util.ArrayList;
import java.util.Collection;
import johmphot.card.Card;
import johmphot.card.R;

public class CardSet {

	Card attackCard = new Card(1, R.drawable.attack,"Attack");
    Card healCard = new Card(2, R.drawable.defense, "Heal");

    final int numAttack = 30;
    final int numHeal = 15;
	
	ArrayList<Card> deck = new ArrayList<Card>();
	public ArrayList<Card> getDeck() {
		return deck;
	}

	public CardSet(){
		//Add all the cards to the deck
        for(int i=0;i<numAttack;i++)
        {
            deck.add(attackCard);
        }
        for(int i=0;i<numHeal;i++)
        {
            deck.add(healCard);
        }
	}
	
}
