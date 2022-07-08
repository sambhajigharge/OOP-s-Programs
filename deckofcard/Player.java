package objectOrientedProgram.deckofcard;

public class Player {
    MyQueue<Cards> cardsQue;
    int[][] playerCards;

    public Player() {
        cardsQue = new MyQueue<Cards>();
        playerCards = new int[4][13];
    }

    public void addCard(int suit, int rank) {
        playerCards[suit][rank] = 1;
    }

    public Cards getCard() {
        return cardsQue.dequeue();
    }

    //Enqueues cards in ascending order
    public void enqueueCards() {
        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 0; rank < 13; rank++) {
                if (playerCards[suit][rank] == 1) {
                    cardsQue.enqueue(new Cards(suit, rank));
                }
            }
        }
    }
}

