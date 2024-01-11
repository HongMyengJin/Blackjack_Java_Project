import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MyPanel extends JPanel {
   private ImageIcon icon = new ImageIcon("cards/table.jpg");
   private Image img = icon.getImage(); 
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(img, 0, 0, getWidth(), getHeight(), this);

   }
}


public class blackjack extends JFrame implements ActionListener{
   public Player player = new Player("player");
   public Player dealer = new Player("dealer");
   
   private JButton jbtn50 = new JButton("Bet 50");
   private JButton jbtn10 = new JButton("Bet 10");
   private JButton jbtn1 = new JButton("Bet 1");             
   private JButton jbtnHit = new JButton("Hit");
   private JButton jbtnStay = new JButton("Stay");
   private JButton jbtnDeal = new JButton("Deal");
   private JButton jbtnAgain = new JButton("Again");
   
   private JLabel jlblBetMoney = new JLabel("$0");
   private JLabel jlblPlayerMoney = new JLabel("You have $1000");
   private JLabel jlblPlayerPts = new JLabel("");
   private JLabel jlblDealerPts = new JLabel("");
   private JLabel jlblStatus = new JLabel(" ");
       
   private Font fontstyle = new Font("Times",Font.BOLD,24);
   private Font fontstyle2 = new Font("Times",Font.BOLD,16);
   
   private int nCardsDealer;
   private int nCardsPlayer;
   private int betMoney = 0;
   private int playerMoney = 1000;//초기자본 1000달러
   private JLabel[] jlblCardsPlayer = new JLabel[7];
   private JLabel[] jlblCardsDealer = new JLabel[7];
   
   MyPanel tablePanel = new MyPanel();
   private Clip chipclip, flipclip, againclip, winclip,loseclip;
   
   blackjack(){
      setupSound();
      JFrame gameFrame = new JFrame("BlackJack");
      gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      tablePanel.setLayout(null);
       jbtn50.setBounds(50,500,80,40);
       jbtn10.setBounds(150,500,80,40);
       jbtn1.setBounds(250,500,80,40);
       jbtnHit.setBounds(400,500,80,40);
       jbtnStay.setBounds(500,500,80,40);
       jbtnDeal.setBounds(600,500,80,40);
       jbtnAgain.setBounds(700,500,80,40);
           
        jlblBetMoney.setBounds(200,450,100,50);
        jlblBetMoney.setFont(fontstyle);
        jlblBetMoney.setForeground(Color.ORANGE);
        tablePanel.add(jlblBetMoney);
        
        jlblPlayerMoney.setBounds(500,450,200,50);
        jlblPlayerMoney.setFont(fontstyle);
        jlblPlayerMoney.setForeground(Color.ORANGE);
        tablePanel.add(jlblPlayerMoney);
        
        jlblPlayerPts.setBounds(300,300,100,50);
        jlblPlayerPts.setFont(fontstyle2);
        jlblPlayerPts.setForeground(Color.WHITE);
        tablePanel.add(jlblPlayerPts);
        
        jlblDealerPts.setBounds(300,100,100,50);
        jlblDealerPts.setFont(fontstyle2);
        jlblDealerPts.setForeground(Color.WHITE);
        tablePanel.add(jlblDealerPts);
        
        jlblStatus.setBounds(500,300,200,50);
        jlblStatus.setFont(fontstyle);
        jlblStatus.setForeground(Color.WHITE);
        tablePanel.add(jlblStatus);

       tablePanel.add(jbtn50);
       tablePanel.add(jbtn10);
       tablePanel.add(jbtn1);
       tablePanel.add(jbtnHit);
       tablePanel.add(jbtnStay);
       tablePanel.add(jbtnDeal);
       tablePanel.add(jbtnAgain);
       
       jbtn50.addActionListener(this);
       jbtn10.addActionListener(this);
       jbtn1.addActionListener(this);
       jbtnHit.addActionListener(this);
       jbtnStay.addActionListener(this);
       jbtnDeal.addActionListener(this);
       jbtnAgain.addActionListener(this);
       
       jbtnHit.setEnabled(false);
       jbtnStay.setEnabled(false);
       jbtnDeal.setEnabled(false);
       jbtnAgain.setEnabled(false);
           
      gameFrame.add(tablePanel);
      gameFrame.setSize(800,600);
      gameFrame.setVisible(true);
   }
   private void setupSound() {
      String flipSound="sounds/cardFlip1.wav";
      try {
         AudioInputStream flipAudioInputStream =
               AudioSystem.getAudioInputStream(new File(flipSound));
         flipclip = AudioSystem.getClip();
         flipclip.open(flipAudioInputStream);
      }
      catch(Exception e) {
         System.out.println(e);
      }
      String chipSound="sounds/chip.wav";
      try {
         AudioInputStream chipAudioInputStream =
               AudioSystem.getAudioInputStream(new File(chipSound));
         chipclip = AudioSystem.getClip();
         chipclip.open(chipAudioInputStream);
      }
      catch(Exception e) {
         System.out.println(e);
      }

      String loseSound="sounds/wrong.wav";
      try {
         AudioInputStream loseAudioInputStream =
               AudioSystem.getAudioInputStream(new File(loseSound));
         loseclip = AudioSystem.getClip();
         loseclip.open(loseAudioInputStream);
      }
      catch(Exception e) {
         System.out.println(e);
      }

      String againSound="sounds/ding.wav";
      try {
         AudioInputStream againAudioInputStream =
               AudioSystem.getAudioInputStream(new File(againSound));
         againclip = AudioSystem.getClip();
         againclip.open(againAudioInputStream);
      }
      catch(Exception e) {
         System.out.println(e);
      }

      String winSound="sounds/win.wav";
      try {
         AudioInputStream winAudioInputStream =
               AudioSystem.getAudioInputStream(new File(winSound));
         winclip = AudioSystem.getClip();
         winclip.open(winAudioInputStream);
      }
      catch(Exception e) {
         System.out.println(e);
      }

   }
   private void hitPlayer(int n) {
      Card newCard = new Card();
      player.addCard(newCard);
      jlblCardsPlayer[player.inHand()-1] = 
            new JLabel(new ImageIcon("cards/"+newCard.filename()));
      jlblCardsPlayer[player.inHand()-1].setBounds(250+n*30,350, 80, 100);
      tablePanel.add(jlblCardsPlayer[player.inHand()-1]);
      jlblPlayerPts.setText(""+player.value());
      tablePanel.updateUI();
      
      flipclip.stop();
      flipclip.setFramePosition(0);
      flipclip.start();
   }
   private void hitDealerDown() {
      Card newCard = new Card(); 
      dealer.addCard(newCard); 
      jlblCardsDealer[dealer.inHand()-1] = new JLabel(new
      ImageIcon("cards/b2fv.png"));
      jlblCardsDealer[dealer.inHand()-1].setBounds(250,150,80,100);
      tablePanel.add(jlblCardsDealer[dealer.inHand()-1]); 
      tablePanel.updateUI();
      }
      private void hitDealer(int n) {
      Card newCard = new Card(); 
      dealer.addCard(newCard); 
      jlblCardsDealer[dealer.inHand()-1] = new JLabel(new ImageIcon("cards/" + newCard.filename()));
      jlblCardsDealer[dealer.inHand()-1].setBounds(280+n*30,150,80,100);
      tablePanel.add(jlblCardsDealer[dealer.inHand()-1]); 
      tablePanel.updateUI();
      } 
      private void checkWinner() { 
         
         tablePanel.remove(jlblCardsDealer[0]); 
         jlblCardsDealer[0] = new JLabel(new ImageIcon("cards/" + dealer.cards[0].filename()));
         jlblCardsDealer[0].setBounds(250,150,80,100);
         tablePanel.add(jlblCardsDealer[0]);
         while (dealer.value() < 17) {
            hitDealer(nCardsDealer - 1 );
            nCardsDealer ++;
         }
         jlblDealerPts.setText(""+dealer.value());
         if (player.value() > 21) {
            jlblStatus.setText("Player Busts"); 
            loseclip.stop();
            loseclip.setFramePosition(0); 
            loseclip.start();
         } 
         else if (dealer.value() > 21) {
            jlblStatus.setText("Dealers Busts"); 
            winclip.stop();
            winclip.setFramePosition(0); 
            winclip.start();
            playerMoney += betMoney * 2;
         }
         else if(player.value() > dealer.value()) {
            jlblStatus.setText("Player Win"); 
            winclip.stop();
            winclip.setFramePosition(0); 
            winclip.start();
            playerMoney += betMoney * 2;
         }
         else if(player.value() == dealer.value()) {
            jlblStatus.setText("Push"); 
            playerMoney += betMoney;
         }
         else if(player.value() < dealer.value()) {
            jlblStatus.setText("Sorry You lost"); 
            loseclip.stop();
            loseclip.setFramePosition(0); 
            loseclip.start();
         }
         betMoney = 0;
         jlblPlayerMoney.setText("You have $"+playerMoney);
         jlblBetMoney.setText("$"+betMoney);
         tablePanel.updateUI(); 
         }

   public void actionPerformed(ActionEvent e) {
      if (e.getSource()==jbtnHit) {
         hitPlayer(nCardsPlayer++);
         if (player.value() > 21) {
            checkWinner();
            jbtn50.setEnabled(false); 
            jbtn10.setEnabled(false);
            jbtn1.setEnabled(false); 
            jbtnHit.setEnabled(false);
            jbtnStay.setEnabled(false); 
            jbtnDeal.setEnabled(false);
            jbtnAgain.setEnabled(true); 
            }
         }
      if (e.getSource()==jbtnStay) {
         checkWinner();
         jbtn50.setEnabled(false); 
         jbtn10.setEnabled(false);
         jbtn1.setEnabled(false); 
         jbtnHit.setEnabled(false);
         jbtnStay.setEnabled(false); 
         jbtnDeal.setEnabled(false);
         jbtnAgain.setEnabled(true); 
      }
      if (e.getSource()==jbtnDeal) {
         hitPlayer(0);
         hitPlayer(1);
         nCardsPlayer +=2;
         hitDealerDown();
         hitDealer(0);
         nCardsDealer +=2;
         jbtnDeal.setEnabled(false);
         jbtnHit.setEnabled(true);
         jbtnStay.setEnabled(true);
      }
      if (e.getSource()==jbtn50) {
         betMoney += 50;
         playerMoney -= 50;
         if (playerMoney < 0) {
            betMoney -= 50;
            playerMoney +=50;
         }
         jlblBetMoney.setText("$"+betMoney);
         jlblPlayerMoney.setText("You have $"+playerMoney);
          jbtnDeal.setEnabled(true);
          
         chipclip.stop();
         chipclip.setFramePosition(0);
         chipclip.start();
      }
      if (e.getSource()==jbtn10) {
         betMoney += 10;
         playerMoney -= 10;
         if (playerMoney < 0) {
            betMoney -= 10;
            playerMoney +=10;
         }
         jlblBetMoney.setText("$"+betMoney);
         jlblPlayerMoney.setText("You have $"+playerMoney);
          jbtnDeal.setEnabled(true);
          
         chipclip.stop();
         chipclip.setFramePosition(0);
         chipclip.start();
      }
      if (e.getSource()==jbtn1) {
         betMoney += 1;
         playerMoney -= 1;
         if (playerMoney < 0) {
            betMoney -= 1;
            playerMoney +=1;
         }
         jlblBetMoney.setText("$"+betMoney);
         jlblPlayerMoney.setText("You have $"+playerMoney);
          jbtnDeal.setEnabled(true);
          
         chipclip.stop();
         chipclip.setFramePosition(0);
         chipclip.start();
      }
      if (e.getSource()==jbtnAgain) {
         for (int i=0 ; i< nCardsPlayer ; i++)
            tablePanel.remove(jlblCardsPlayer[i]);
         for (int i=0 ; i< nCardsDealer ; i++)
            tablePanel.remove(jlblCardsDealer[i]);
         player.reset();
         dealer.reset();
         jlblDealerPts.setText("");
         jlblPlayerPts.setText("");
         jlblStatus.setText("");         
         nCardsPlayer = 0;
         nCardsDealer = 0;
         jbtn50.setEnabled(true); 
         jbtn10.setEnabled(true);
         jbtn1.setEnabled(true); 
         jbtnHit.setEnabled(false);
         jbtnStay.setEnabled(false); 
         jbtnDeal.setEnabled(false);
         jbtnAgain.setEnabled(false);
         againclip.stop();
         againclip.setFramePosition(0);
         againclip.start();
         tablePanel.updateUI();
      }
   }
   public static void main(String[] args) {
      new blackjack();
   }
}
