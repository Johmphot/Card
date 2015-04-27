package johmphot.card;

import java.util.ArrayList;

public class CardSet {

	Card attackCard = new Card(1, R.drawable.redcard,"Attack");
    Card healCard = new Card(2, R.drawable.bluecard, "Heal");
    Card meesukCard = new Card (3, R.drawable.meesukcard, "Meesuk");
    Card swapHPCard = new Card (4, R.drawable.greencard, "SwapHP");
    Card suicideCard = new Card (5, R.drawable.c4card, "Suicide");
    Card discardCard = new Card (6, R.drawable.firecard, "Discard");
    Card shieldCard = new Card (7, R.drawable.shieldcard, "Shield");

    final int numAttack = 15;
    final int numHeal = 15;
    final int numMeesuk = 5;
    final int numSwapHP = 5;
    final int numSuicide = 5;
    final int numDiscard = 10;
    final int numShield = 10;
	
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
        for(int i=0;i<numMeesuk;i++)
        {
            deck.add(meesukCard);
        }
        for(int i=0;i<numSwapHP;i++)
        {
            deck.add(swapHPCard);
        }
        for(int i=0;i<numSuicide;i++)
        {
            deck.add(suicideCard);
        }
        for(int i=0;i<numDiscard;i++)
        {
            deck.add(discardCard);
        }
        for(int i=0;i<numShield;i++)
        {
            deck.add(shieldCard);
        }
	}
	
}
