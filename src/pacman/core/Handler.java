package pacman.core;

import pacman.entities.EntityManager;
import pacman.input.KeyManager;
import pacman.states.StateManager;

public class Handler {

	private Game game;
	private Board board;
	private StateManager stateManager;
	private EntityManager entityManager;
	private KeyManager keyManager;

	public Handler(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public Board getBoard() {
		return board;
	}

	public StateManager getStateManager() {
		return stateManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setStateManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setKeyManager(KeyManager keyManager) {
		this.keyManager = keyManager;
	}
}
