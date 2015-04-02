package johmphot.card;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Johmphot on 3/28/15.
 */
public class MultiplayerGame
{
    int playerNum = 2;
    Card playerCard = null;
    Card opponentCard = null;
    CardSet set = new CardSet();
    ArrayList<Card> deck = set.getDeck();

    public void start()
    {
        Collections.shuffle(deck);
    }

    public Card draw()
    {
        if(deck.size()!=0)
        {
            Card x = deck.get(0);
            deck.remove(0);
            return x;
        }
        return null;
    }

    public Card getPlayerCard()
    {
        return playerCard;
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
