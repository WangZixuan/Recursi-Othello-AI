package othello;

import gamePlayer.Action;
import gamePlayer.Decider;
import gamePlayer.InvalidActionException;
import gamePlayer.State.Status;
import gamePlayer.algorithms.NegaMaxDecider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

class GamePanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	private OthelloState board;
	private Decider playerOne;
	private Decider playerTwo;
	private boolean turn;
	private boolean inputEnabled;
	
	public GamePanel(Decider p1, Decider p2, OthelloState board) {
		this.board = board;
		this.playerOne = p1;
		this.playerTwo = p2;
		this.turn = true;
		addMouseListener(this);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Cursor savedCursor = getCursor();
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				computerMove();
				setCursor(savedCursor);
			}
		});
		setBackground(Color.green);
	}
	
	protected void drawPanel(Graphics g) {
		// int currentWidth = getWidth();
		// int currentHeight = getHeight();
		for (int i = 1; i < 8; i++) {
			g.drawLine(i * OthelloGUI.Square_L, 0, i * OthelloGUI.Square_L,
					OthelloGUI.Height);
		}
		g.drawLine(OthelloGUI.Width, 0, OthelloGUI.Width, OthelloGUI.Height);
		for (int i = 1; i < 8; i++) {
			g.drawLine(0, i * OthelloGUI.Square_L, OthelloGUI.Width, i
					* OthelloGUI.Square_L);
		}
		g.drawLine(0, OthelloGUI.Height, OthelloGUI.Width, OthelloGUI.Height);
		System.out.println("Redrawing board\n"+board);
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				switch (board.at(j, i)) {
				case '3':
					g.setColor(Color.white);
					g.fillOval(1 + i * OthelloGUI.Square_L, 1 + j
							* OthelloGUI.Square_L, OthelloGUI.Square_L - 1,
							OthelloGUI.Square_L - 1);
					break;
				case '2':
					g.setColor(Color.black);
					g.fillOval(1 + i * OthelloGUI.Square_L, 1 + j
							* OthelloGUI.Square_L, OthelloGUI.Square_L - 1,
							OthelloGUI.Square_L - 1);
					break;
				}
	}
	
	@Override
	protected void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
		drawPanel(arg0);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(OthelloGUI.Width, OthelloGUI.Height);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (inputEnabled) {
			int j = e.getX() / OthelloGUI.Square_L;
			int i = e.getY() / OthelloGUI.Square_L;
			if ((i < 8) && (j < 8) && (board.at(i, j) == '0')) {
				try {
					board = new OthelloAction(false, (byte)i, (byte)j).applyTo(board);
					inputEnabled = false;
				} catch (InvalidActionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				/*
				 * score_black.setText(Integer.toString(board.getCounter(TKind.black
				 * )));
				 * score_white.setText(Integer.toString(board.getCounter(TKind
				 * .white)));
				 */
				repaint();
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Cursor savedCursor = getCursor();
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						computerMove();
						setCursor(savedCursor);
					}
				});
			} else
				JOptionPane.showMessageDialog(this, "Illegal move", "Reversi",
						JOptionPane.ERROR_MESSAGE);
		}
	}

	public void computerMove() {
		if (board.getStatus() != Status.Ongoing) {
			showWinner();
			return;
		}
		
		OthelloAction action = (OthelloAction) playerOne.decide(board);
		try {
			board = action.applyTo(board);
			System.out.println(board);
		} catch (InvalidActionException e) {
			throw new RuntimeException("Invalid action!");
		}
		repaint();
		// Next person's turn
		this.turn = !this.turn;
		inputEnabled = true;
		/*
		 * Move move = new Move(); if
		 * (board.findMove(TKind.white,gameLevel,move)) {
		 * board.move(move,TKind.white);
		 * score_black.setText(Integer.toString(board.getCounter(TKind.black)));
		 * score_white.setText(Integer.toString(board.getCounter(TKind.white)));
		 * repaint(); if (board.gameEnd()) showWinner(); else if
		 * (!board.userCanMove(TKind.black)) {
		 * JOptionPane.showMessageDialog(this,
		 * "You pass...","Reversi",JOptionPane.INFORMATION_MESSAGE);
		 * javax.swing.SwingUtilities.invokeLater(new Runnable() { public void
		 * run() { computerMove(); } }); } } else if
		 * (board.userCanMove(TKind.black)) JOptionPane.showMessageDialog(this,
		 * "I pass...","Reversi",JOptionPane.INFORMATION_MESSAGE); else
		 * showWinner();
		 */
	}
	
	private void showWinner() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

public class OthelloGUI2 extends JFrame {

	private JLabel score_black;
	private JLabel score_white;
	private JPanel gamePanel;

	static final int Square_L = 33; // length in pixel of a square in the grid
	static final int Width = 8 * Square_L; // Width of the game board
	static final int Height = 8 * Square_L; // Width of the game board

	public OthelloGUI2() {
		score_black = new JLabel("2"); // the game start with 2 black pieces
		score_black.setForeground(Color.blue);
		score_black.setFont(new Font("Dialog", Font.BOLD, 16));
		score_white = new JLabel("2"); // the game start with 2 white pieces
		score_white.setForeground(Color.red);
		score_white.setFont(new Font("Dialog", Font.BOLD, 16));

		OthelloState start = new OthelloState();
		start.setStandardStartState();
		
		gamePanel = new GamePanel(new NegaMaxDecider(true, 10), new OthelloPlayer(false), start);
		
		
		gamePanel.setMinimumSize(new Dimension(OthelloGUI.Width,
						OthelloGUI.Height));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel status = new JPanel();
		status.setLayout(new BorderLayout());
		status.add(score_black, BorderLayout.WEST);
		status.add(score_white, BorderLayout.EAST);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				gamePanel, status);
		
		splitPane.setOneTouchExpandable(false);
		getContentPane().add(splitPane);
		
		pack();
		setVisible(true);
		setResizable(false);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				OthelloGUI2 frame = new OthelloGUI2();
			}
		});

	}

}