package johmphot.card;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Johmphot on 3/28/15.
 */
public class MultiplayerGame
{
    Card playerCard = null;
    Card opponentCard = null;
    Card nextCard = null; //buffer
    CardSet set = new CardSet();
    ArrayList<Card> deck;

    public void startServer()
    {
        deck = set.getDeck();
        Collections.shuffle(deck);
        getNextCard();
    }

    public Card draw()
    {
        if (nextCard!=null)
        {
            Card x = nextCard;
            return x;
        }
        return null;
    }

    public void getNextCard()
    {
        if(deck.size()!=0)
        {
            Card x = deck.get(0);
            deck.remove(0);
            nextCard = x;
        }
    }

    public Card findMin (Card x, Card y)
    {
        int value_x = x.getValue();
        int value_y = y.getValue();
        if(value_x>value_y)
        {
            return y;
        }
        else
        {
            return x;
        }
    }
}
