package johmphot.card;

import java.util.ArrayList;

public class CardSet {

	Card attackCard = new Card(1, R.drawable.redcard,"Attack");
    Card healCard = new Card(2, R.drawable.bluecard, "Heal");
    Card meesukCard = new Card (3, R.drawable.meesukcard, "Meesuk");
    Card swapHPCard = new Card (4, R.drawable.greencard, "SwapHP");

    final int numAttack = 30;
    final int numHeal = 15;
    final int numMeesuk = 5;
    final int numSwapHP = 5;
	
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
	}
	
}
