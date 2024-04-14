package pacman.core;

import pacman.states.StateManager;

public class Handler {

	private Game game;
	private Board board;
	private StateManager stateManager;

	public Handler(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}

	public void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}

	public StateManager getStateManager() {
		return stateManager;
	}
}
