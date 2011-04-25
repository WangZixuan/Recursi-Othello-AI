package othello;

import gamePlayer.Action;
import gamePlayer.InvalidActionException;

/**
 * An Action in the board game Othello.
 * @author Ashoat Tevosyan
 * @author Peter Brook
 * @since Sat April 23 2011
 * @version CSE 473
 */
public class OthelloAction implements Action<OthelloState> {
	
	// Which player are we?
	private boolean player;
	// The x-coordinate of the move.
	private byte x;
	// The y-coordinate of the move.
	private byte y;
	
	/**
	 * Instantiate this object. 
	 * @param player True if player one; false otherwise.
	 * @param x      The x-coordinate of the move.
	 * @param y      The y-coordinate of the move.
	 */
	public OthelloAction(boolean player, byte x, byte y) {
		this.player = player;
		this.x = x;
		this.y = y;
	}
	
	/** {@inheritDoc }*/
	@Override
	public boolean validOn(OthelloState input) {
		if (this.player != input.move) return false;
		return input.moveIsValid(this.x, this.y, this.player);
		
	}
	
	/** {@inheritDoc }*/
	@Override
	public OthelloState applyTo(OthelloState input) throws InvalidActionException {
		if (this.player != input.move) return null;
		return input.childOnMove(x, y);
	}
	
	/**
	 * Returns a String representation of this OthelloAction.
	 * @return A String representation of this OthelloAction.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Player ").append(this.player ? "1" : "0").append(" is moving to ");
		builder.append("(").append(this.x).append(", ").append(this.y).append(").");
		return builder.toString();
	}

}