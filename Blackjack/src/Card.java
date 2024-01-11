public class Card {
   private int value, x;
   private String suit;
   public Card() {
      int temp;
      temp=(int)(Math.random()*52);//0,..51
      value = temp % 13 +1; //1,2,3...13
      x = temp/13; //0,1,2,3
   }
   public int getValue() {
      int y=value;
      if (value>10) y=10; //value 11,12, 13 J, Q, K ÀüºÎ´Ù 10
      return y;
   }
   public String getsuit() {
      switch(x) {
      case 0: return suit="Clubs";
      case 1: return suit="Spades";
      case 2: return suit="Hearts";
      default: return suit="Diamonds";
      }
   }
   public String filename() {
      return getsuit()+""+value+".png";
   }
}