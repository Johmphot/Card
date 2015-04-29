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
    boolean isYourTurn = true;
    boolean canAttack = true;
    boolean useSuicide = false;
    boolean haveShield = false;
    boolean opponentHaveShield = false;

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
        useSuicide = false;
        haveShield = false;

        shieldExpire();

        for(int i=0;i<4;i++)
        {
            if(playerCard[i]==null)
            {
                playerCard[i] = draw();
            }
        }
    }
    public void endTurn()
    {
        isYourTurn = false;
        canAttack = false;
        useSuicide = false;
        opponentHaveShield = false;
    }

    /**
     * For player's0 side
     */
    public void attack()
    {
        if(!opponentHaveShield)
        {
            opponentHP = opponentHP-1;
        }
        canAttack = false;
    }
    public void heal()
    {
        playerHP = playerHP+1;
    }
    public void meesuk()
    {
        canAttack = true;
    }
    public void swapHP()
    {
        int tmp = playerHP;
        playerHP = opponentHP;
        opponentHP = tmp;
    }
    public void suicide()
    {
        playerHP = playerHP-3;
        opponentHP = opponentHP-2;
    }

    public void shield()
    {
        haveShield = true;
    }

    public void shieldExpire()
    {
        haveShield = false;
    }

    /**
     * For opponent's side
     */
    public void opponentAttack()
    {
        if (!haveShield)
        {
            playerHP = playerHP-1;
        }
    }
    public void opponentHeal()
    {
        opponentHP = opponentHP+1;
    }

    public void opponentSuicide()
    {
        playerHP = playerHP-2;
        opponentHP = opponentHP-3;
    }

    public void opponentShield()
    {
        opponentHaveShield = true;
    }

    public void opponentShieldExpire()
    {
        opponentHaveShield = false;
    }

}
