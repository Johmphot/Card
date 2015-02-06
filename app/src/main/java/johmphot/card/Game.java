package johmphot.card;

import java.util.ArrayList;
import java.util.Random;

public class Game
{
    int playerNum = 2;
    Card[] playerCard = new Card[playerNum];
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

    public int findLoser(Card[] playerCard) //return loser id
    {
        Card min = playerCard[0];
        for(int i=1;i<playerNum;i++)
        {
            min = findMin(min,playerCard[i]);
        }
        for(int i=0;i<playerNum;i++)
        {
            if(min==playerCard[i])
            {
                return i;
            }
        }
        return -1;
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
