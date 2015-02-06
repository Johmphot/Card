package johmphot.card;

import java.util.ArrayList;
import java.util.Collection;
import johmphot.card.Card;
import johmphot.card.R;

public class CardSet {

	//Clubs
	Card clubs2 = new Card(2, R.drawable.clubs2, "clubs2", "clubs");
	Card clubs3 = new Card(3, R.drawable.clubs3, "clubs3", "clubs");
	Card clubs4 = new Card(4, R.drawable.clubs4, "clubs4", "clubs");
	Card clubs5 = new Card(5, R.drawable.clubs5, "clubs5", "clubs");
	Card clubs6 = new Card(6, R.drawable.clubs6, "clubs6", "clubs");
	Card clubs7 = new Card(7, R.drawable.clubs7, "clubs7", "clubs");
	Card clubs8 = new Card(8, R.drawable.clubs8, "clubs8", "clubs");
	Card clubs9 = new Card(9, R.drawable.clubs9, "clubs9", "clubs");
	Card clubs10 = new Card(10, R.drawable.clubs10, "clubs10", "clubs");
	Card clubsj = new Card(11, R.drawable.clubsj, "clubsj", "clubs");
	Card clubsq = new Card(12, R.drawable.clubsq, "clubsq", "clubs");
	Card clubsk = new Card(13, R.drawable.clubsk, "clubsk", "clubs");
	Card clubsa = new Card(14, R.drawable.clubsa, "clubsa", "clubs");
	
	//Diamonds
	Card diamonds2 = new Card(2, R.drawable.diamonds2, "diamonds2", "diamonds");
	Card diamonds3 = new Card(3, R.drawable.diamonds3, "diamonds3", "diamonds");
	Card diamonds4 = new Card(4, R.drawable.diamonds4, "diamonds4", "diamonds");
	Card diamonds5 = new Card(5, R.drawable.diamonds5, "diamonds5", "diamonds");
	Card diamonds6 = new Card(6, R.drawable.diamonds6, "diamonds6", "diamonds");
	Card diamonds7 = new Card(7, R.drawable.diamonds7, "diamonds7", "diamonds");
	Card diamonds8 = new Card(8, R.drawable.diamonds8, "diamonds8", "diamonds");
	Card diamonds9 = new Card(9, R.drawable.diamonds9, "diamonds9", "diamonds");
	Card diamonds10 = new Card(10, R.drawable.diamonds10, "diamonds10", "diamonds");
	Card diamondsj = new Card(11, R.drawable.diamondsj, "diamondsj", "diamonds");
	Card diamondsq = new Card(12, R.drawable.diamondsq, "diamondsq", "diamonds");
	Card diamondsk = new Card(13, R.drawable.diamondsk, "diamondsk", "diamonds");
	Card diamondsa = new Card(14, R.drawable.diamondsa, "diamondsa", "diamonds");
	
	//Hearts
	Card hearts2 = new Card(2, R.drawable.hearts2, "hearts2", "hearts");
	Card hearts3 = new Card(3, R.drawable.hearts3, "hearts3", "hearts");
	Card hearts4 = new Card(4, R.drawable.hearts4, "hearts4", "hearts");
	Card hearts5 = new Card(5, R.drawable.hearts5, "hearts5", "hearts");
	Card hearts6 = new Card(6, R.drawable.hearts6, "hearts6", "hearts");
	Card hearts7 = new Card(7, R.drawable.hearts7, "hearts7", "hearts");
	Card hearts8 = new Card(8, R.drawable.hearts8, "hearts8", "hearts");
	Card hearts9 = new Card(9, R.drawable.hearts9, "hearts9", "hearts");
	Card hearts10 = new Card(10, R.drawable.hearts10, "hearts10", "hearts");
	Card heartsj = new Card(11, R.drawable.heartsj, "heartsj", "hearts");
	Card heartsq = new Card(12, R.drawable.heartsq, "heartsq", "hearts");
	Card heartsk = new Card(13, R.drawable.heartsk, "heartsk", "hearts");
	Card heartsa = new Card(14, R.drawable.heartsa, "heartsa", "hearts");
	
	//Spades
	Card spades2 = new Card(2, R.drawable.spades2, "spades2", "spades");
	Card spades3 = new Card(3, R.drawable.spades3, "spades3", "spades");
	Card spades4 = new Card(4, R.drawable.spades4, "spades4", "spades");
	Card spades5 = new Card(5, R.drawable.spades5, "spades5", "spades");
	Card spades6 = new Card(6, R.drawable.spades6, "spades6", "spades");
	Card spades7 = new Card(7, R.drawable.spades7, "spades7", "spades");
	Card spades8 = new Card(8, R.drawable.spades8, "spades8", "spades");
	Card spades9 = new Card(9, R.drawable.spades9, "spades9", "spades");
	Card spades10 = new Card(10, R.drawable.spades10, "spades10", "spades");
	Card spadesj = new Card(11, R.drawable.spadesj, "spadesj", "spades");
	Card spadesq = new Card(12, R.drawable.spadesq, "spadesq", "spades");
	Card spadesk = new Card(13, R.drawable.spadesk, "spadesk", "spades");
	Card spadesa = new Card(14, R.drawable.spadesa, "spadesa", "spades");
	
	ArrayList<Card> deck = new ArrayList<Card>();
	public ArrayList<Card> getDeck() {
		return deck;
	}

	public CardSet(){
		//Add all the cards to the deck
		deck.add(clubs2);
		deck.add(clubs3);
		deck.add(clubs4);
		deck.add(clubs5);
		deck.add(clubs6);
		deck.add(clubs7);
		deck.add(clubs8);
		deck.add(clubs9);
		deck.add(clubs10);
		deck.add(clubsj);
		deck.add(clubsq);
		deck.add(clubsk);
		deck.add(clubsa);
		
		deck.add(diamonds2);
		deck.add(diamonds3);
		deck.add(diamonds4);
		deck.add(diamonds5);
		deck.add(diamonds6);
		deck.add(diamonds7);
		deck.add(diamonds8);
		deck.add(diamonds9);
		deck.add(diamonds10);
		deck.add(diamondsj);
		deck.add(diamondsq);
		deck.add(diamondsk);
		deck.add(diamondsa);
		
		deck.add(hearts2);
		deck.add(hearts3);
		deck.add(hearts4);
		deck.add(hearts5);
		deck.add(hearts6);
		deck.add(hearts7);
		deck.add(hearts8);
		deck.add(hearts9);
		deck.add(hearts10);
		deck.add(heartsj);
		deck.add(heartsq);
		deck.add(heartsk);
		deck.add(heartsa);

		deck.add(spades2);
		deck.add(spades3);
		deck.add(spades4);
		deck.add(spades5);
		deck.add(spades6);
		deck.add(spades7);
		deck.add(spades8);
		deck.add(spades9);
		deck.add(spades10);
		deck.add(spadesj);
		deck.add(spadesq);
		deck.add(spadesk);
		deck.add(spadesa);
	}
	
}
