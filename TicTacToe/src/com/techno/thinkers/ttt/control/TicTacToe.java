package com.techno.thinkers.ttt.control;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.techno.thinkers.ttt.model.Node;

public class TicTacToe extends JFrame implements ChangeListener, ActionListener {
  private JSlider slider;
  private JButton oButton, xButton;
  private Board board;
  private int lineThickness=4;
  private Node orginalNode;
  private Node currentNode;
  private Color oColor=Color.BLUE, xColor=Color.RED;
  static final char BLANK='-', O='O', X='X';
  private char position[]={  // Board position (BLANK, O, or X)
    BLANK, BLANK, BLANK,
    BLANK, BLANK, BLANK,
    BLANK, BLANK, BLANK};
  private int wins=0, losses=0, draws=0;  // game count by user

  // Start the game
  public static void main(String args[]) {
    new TicTacToe();
  }

  // Initialize
  public TicTacToe() {
    super("Tic Tac Toe demo");
    try {
		FileInputStream fis = new FileInputStream("D:\\Training\\Node.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		// write object to file
		this.orginalNode=(Node) ois.readObject();
		this.currentNode=orginalNode;
		System.out.println("Done");
		// closing resources
		ois.close();
		ois.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
    JPanel topPanel=new JPanel();
    topPanel.setLayout(new FlowLayout());
    topPanel.add(new JLabel("Line Thickness:"));
    topPanel.add(slider=new JSlider(SwingConstants.HORIZONTAL, 1, 20, 4));
    slider.setMajorTickSpacing(1);
    slider.setPaintTicks(true);
    slider.addChangeListener(this);
    topPanel.add(oButton=new JButton("O Color"));
    topPanel.add(xButton=new JButton("X Color"));
    oButton.addActionListener(this);
    xButton.addActionListener(this);
    add(topPanel, BorderLayout.NORTH);
    add(board=new Board(), BorderLayout.CENTER);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(500, 500);
    setVisible(true);
  }

  // Change line thickness
  public void stateChanged(ChangeEvent e) {
    lineThickness = slider.getValue();
    board.repaint();
  }

  // Change color of O or X
  public void actionPerformed(ActionEvent e) {
    if (e.getSource()==oButton) {
      Color newColor = JColorChooser.showDialog(this, "Choose a new color for O", oColor);
      if (newColor!=null)
        oColor=newColor;
    }
    else if (e.getSource()==xButton) {
      Color newColor = JColorChooser.showDialog(this, "Choose a new color for X", xColor);
      if (newColor!=null)
        xColor=newColor;
    }
    board.repaint();
  }

  // Board is what actually plays and displays the game
  private class Board extends JPanel implements MouseListener {
    private Random random=new Random();
    private int rows[][]={{0,2},{3,5},{6,8},{0,6},{1,7},{2,8},{0,8},{2,6}};
      // Endpoints of the 8 rows in position[] (across, down, diagonally)

    public Board() {
      addMouseListener(this);
    }

    // Redraw the board
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      int w=getWidth();
      int h=getHeight();
      Graphics2D g2d = (Graphics2D) g;

      // Draw the grid
      g2d.setPaint(Color.WHITE);
      g2d.fill(new Rectangle2D.Double(0, 0, w, h));
      g2d.setPaint(Color.BLACK);
      g2d.setStroke(new BasicStroke(lineThickness));
      g2d.draw(new Line2D.Double(0, h/3, w, h/3));
      g2d.draw(new Line2D.Double(0, h*2/3, w, h*2/3));
      g2d.draw(new Line2D.Double(w/3, 0, w/3, h));
      g2d.draw(new Line2D.Double(w*2/3, 0, w*2/3, h));

      // Draw the Os and Xs
      for (int i=0; i<9; ++i) {
        double xpos=(i%3+0.5)*w/3.0;
        double ypos=(i/3+0.5)*h/3.0;
        double xr=w/8.0;
        double yr=h/8.0;
        if (position[i]==O) {
          g2d.setPaint(oColor);
          g2d.draw(new Ellipse2D.Double(xpos-xr, ypos-yr, xr*2, yr*2));
        }
        else if (position[i]==X) {
          g2d.setPaint(xColor);
          g2d.draw(new Line2D.Double(xpos-xr, ypos-yr, xpos+xr, ypos+yr));
          g2d.draw(new Line2D.Double(xpos-xr, ypos+yr, xpos+xr, ypos-yr));
        }
      }
    }

    // Draw an O where the mouse is clicked
    public void mouseClicked(MouseEvent e) {
      int xpos=e.getX()*3/getWidth();
      int ypos=e.getY()*3/getHeight();
      int pos=xpos+3*ypos;
      if (pos>=0 && pos<9 && position[pos]==BLANK) {
    	  System.out.println("Hello");
        position[pos]=O;
        repaint();
        System.out.println(orginalNode.getCode());
        putX();  // computer plays
        repaint();
      }
    }
    

    // Ignore other mouse events
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // Computer plays X
    void putX() {
      
      // Check if game is over
    	if (won(O))
            newGame(O);
          else if (isDraw())
            newGame(BLANK);

          // Play X, possibly ending the game
          else {
            nextMove();
            if (won(X))
              newGame(X);
            else if (isDraw())
              newGame(BLANK);
          }
    }

    // Return true if player has won
    boolean won(char player) {
      for (int i=0; i<8; ++i)
        if (testRow(player, rows[i][0], rows[i][1]))
          return true;
      return false;
    }

    // Has player won in the row from position[a] to position[b]?
    boolean testRow(char player, int a, int b) {
      return position[a]==player && position[b]==player 
          && position[(a+b)/2]==player;
    }

    // Play X in the best spot
    void nextMove() {
    	System.out.println("Player X-->"+new String(position));
    	System.out.println(orginalNode);
    	if(currentNode.getList()!=null) {
    		Node sucessNode=TUtil.findHigestSucess(currentNode.getList());
    		if(sucessNode.getSucess()>=0) {
				position=TUtil.toCharArray(sucessNode.getCode());
				currentNode=sucessNode;
		}else {
			selectNode(null);
		}
    		
    	}else {
    		selectNode(null);
    	}
    	
      
    }
    public void selectNode(ArrayList<Node> listNode) {

		int n=new Random().nextInt(position.length);
		if(listNode==null) {
			listNode=new ArrayList<>();	
		}
		
		while(position[n]!=BLANK) {
			 n=new Random().nextInt(position.length);
		}
		position[n]=X;
		Node node =new Node();
		node.setCode(position);
		node.setPosition(position);
		node.setParent(currentNode);
		listNode.add(node);
		currentNode.setList(listNode);
		currentNode=node;
		System.out.println(n);
		
	
    }


    // Are all 9 spots filled?
    boolean isDraw() {
      for (int i=0; i<9; ++i)
        if (position[i]==BLANK)
          return false;
      return true;
    }

    // Start a new game
    void newGame(char winner) {
    	
      repaint();

      // Announce result of last game.  Ask user to play again.
      String result;
      if (winner==O) {
    	  
    	  currentNode.setSucess(-1);
        ++wins;
        result = "You Win!";
      }
      else if (winner==X) {
    	  currentNode.setSucess(+1);
        ++losses;
        result = "I Win!";
      }
      else {
    	  currentNode.setSucess(0);
        result = "Tie";
        ++draws;
      }
      if (JOptionPane.showConfirmDialog(null, 
          "You have "+wins+ " wins, "+losses+" losses, "+draws+" draws\n"
          +"Play again?", result, JOptionPane.YES_NO_OPTION)
          !=JOptionPane.YES_OPTION) {
        System.exit(0);
      }
      TUtil.changeParentSuccess(currentNode);
      currentNode=orginalNode;
      /*try {
			FileOutputStream fos = new FileOutputStream("D:\\Training\\Node.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// write object to file
			oos.writeObject(orginalNode);
			System.out.println("Done");
			// closing resources
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
      // Clear the board to start a new game
      for (int j=0; j<9; ++j)
        position[j]=BLANK;

      /*// Computer starts first every other game
      if ((wins+losses+draws)%2 == 1)
        nextMove();*/
    }
  } // end inner class Board
} // end class TicTacToe