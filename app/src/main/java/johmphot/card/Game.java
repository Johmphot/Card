package johmphot.card;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Johmphot on 2/6/15.
 */
public class Game
{
    int playerNum = 1;
    Card[] playerCard = new Card[1];
    CardSet set = new CardSet();
    ArrayList<Card> deck = set.getDeck();

    public void start()
    {
        for(int i=0;i<playerCard.length;i++)
        {
            playerCard[i] = draw();
        }
    }

    public Card draw()
    {
        Random r = new Random();
        int i = (r.nextInt(deck.size()-1));
        return deck.get(i);
    }

    public void draw(int playerID)
    {
        playerCard[playerID] = draw();
    }

    public Card getPlayerCard(int playerID)
    {
        return playerCard[playerID];
    }
}
