public class Player {
   final static int MAX_CARDS = 52;
   public Card[] cards = new Card[MAX_CARDS];
   private int N = 0;
   private String name;
   public Player(String name) {
      this.name = name;
   }
   public int inHand() {
      return N; //손에 있는 카드의 개수
   }
   public void addCard(Card c) {
      cards[N++] = c;
   }
   public void reset() {
      N=0;
   }
   public int value() { //점수 계산하는 메소드
      //카드 숫자를 더하는 데 Ace는 1, 11 될 수 있다.
      int result = 0; //점수
      int Aces = 0;   //Ace 카드 의 개수
      for (int i=0; i<N; i++) {
         if (cards[i].getValue() == 1) {
            Aces ++;
            result += 11;
         }
         else
            result += cards[i].getValue();         
      }
      while (result > 21 && Aces > 0) {
         result -= 10;
         Aces --;
      }
      return result;
   }
}