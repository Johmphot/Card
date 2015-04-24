package johmphot.card.bluetooth;

import java.util.ArrayList;
import java.util.Collections;

import johmphot.card.Card;
import johmphot.card.CardSet;


public class MultiplayerGame
{
    Card[] playerCard = new Card[4];
    int playerHP = 4;
    int opponentHP = 4;
    Card buffer = null;
    CardSet set = new CardSet();
    ArrayList<Card> deck;
    boolean isYourTurn = false;
    boolean canAttack = false;

    public void start()
    {
        deck = set.getDeck();
        Collections.shuffle(deck);
        for(int i=0;i<4;i++)
        {
            playerCard[i]=draw();
        }
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

    public void startTurn()
    {
        isYourTurn = true;
        canAttack = true;
        for(int i=0;i<4;i++)
        {
            if(playerCard[i]==null)
            {
                playerCard[i] = draw();
            }
        }
        heal();
    }
    public void endTurn()
    {
        isYourTurn = false;
        canAttack = false;
    }

    public void attack()
    {
        opponentHP = opponentHP-1;
        canAttack = false;
    }
    public void heal()
    {
        playerHP = playerHP+1;
    }
    public void meesuk()
    {

    }
    public void swapHP()
    {
        int tmp = playerHP;
        playerHP = opponentHP;
        opponentHP = tmp;
    }

    public void opponentAttack()
    {
        playerHP = playerHP-1;
    }
    public void opponentHeal()
    {
        opponentHP = opponentHP+1;
    }
    public void opponentMeesuk()
    {

    }
}
