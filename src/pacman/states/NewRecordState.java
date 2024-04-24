package pacman.states;

import pacman.core.Handler;
import pacman.graphics.TextRenderer;

import java.awt.*;

public class NewRecordState extends State {

	private final int MIN_NAME_LENGTH = 3, MAX_NAME_LENGTH = 8;
	private final int T1 = 2500; // typing becomes enabled after 2.5 seconds
	private final int T2 = 3000; // name rule message stays visible for 3 seconds

	private long previousTime;
	private long timer;
	private long delta;
	private long nameRuleTimerStart;

	private boolean newRecordVisible;
	private boolean typingVisible;
	private boolean nameRuleVisible;
	private boolean typingEnabled;

	private char[] name;
	private int nameLength;

	public NewRecordState(Handler handler) {
		super(handler);
	}

	@Override
	public void startImpl() {
		this.timer = 0;
		this.delta = 0;
		this.previousTime = System.currentTimeMillis();
		this.nameRuleTimerStart = 0;

		this.newRecordVisible = true;
		this.typingVisible = false;
		this.nameRuleVisible = false;
		this.typingEnabled = false;

		this.name = new char[MAX_NAME_LENGTH];
		this.nameLength = 0;
	}

	@Override
	public void updateImpl() {
		long currentTime = System.currentTimeMillis();
		this.timer += currentTime - this.previousTime;
		this.delta += currentTime - this.previousTime;
		this.previousTime = currentTime;

		if (!this.typingEnabled) {
			if (this.delta < 300) {
				this.newRecordVisible = true;
			} else if (this.delta > 500) {
				this.delta = 0;
			} else {
				this.newRecordVisible = false;
			}
			if (this.timer >= T1) {
				this.typingEnabled = true;
				this.newRecordVisible = true;
			}
		} else {
			this.readCharacter();
			this.deleteCharacter();

			if (this.nameRuleVisible) {
				if (currentTime - this.nameRuleTimerStart >= T2) {
					this.nameRuleVisible = false;
				}
			}

			if (this.delta < 300) {
				this.typingVisible = true;
			} else if (this.delta > 500) {
				this.delta = 0;
			} else {
				this.typingVisible = false;
			}

			if (this.handler.getKeyManager().enter) {
				if (this.isNameValid()) {
					this.handler.getGame().saveNewHighScore(this.getName());
					this.handler.getStateManager().startMenuState();
				} else {
					this.nameRuleVisible = true;
					this.nameRuleTimerStart = currentTime;
				}
			}
		}
	}

	@Override
	public void renderImpl(Graphics g) {
		if (this.newRecordVisible) {
			TextRenderer.drawTextCenterAligned(g, "new record", 180, 35);
		}

		if (this.typingEnabled) {
			if (this.typingVisible) {
				TextRenderer.drawTextCenterAligned(g, "type your name", 250, 16);
			}

			if (this.nameRuleVisible) {
				TextRenderer.drawTextCenterAligned(g, "name must be between 3 and 8 characters long\nand cannot start with a digit", 510, 9);
			}

			String currentName = this.getName();
			currentName += this.typingVisible ? '_' : ' ';
			TextRenderer.drawTextCenterAligned(g, currentName, 280, 20);
		}
	}

	private void readCharacter() {
		char inputChar = 0;

		if (this.handler.getKeyManager().underscore) {
			inputChar = '_';
		}
		for (int i = 0; i < 10; i++) {
			if (this.handler.getKeyManager().digits[i]) {
				inputChar = (char) ('0' + i);
				break;
			}
		}
		for (int i = 0; i < 26; i++) {
			if (this.handler.getKeyManager().letters[i]) {
				inputChar = (char) ('A' + i);
				break;
			}
		}

		if (inputChar != 0) {
			if (this.nameLength < MAX_NAME_LENGTH) {
				this.name[this.nameLength++] = inputChar;
			} else {
				this.nameRuleVisible = true;
				this.nameRuleTimerStart = System.currentTimeMillis();
			}
		}
	}

	private void deleteCharacter() {
		if (this.handler.getKeyManager().backspace && this.nameLength > 0) {
			this.nameLength--;
		}
	}

	private boolean isNameValid() {
		if (this.name[0] >= '0' && this.name[0] <= '9') {
			return false;
		}
		return this.nameLength >= MIN_NAME_LENGTH && this.nameLength <= MAX_NAME_LENGTH;
	}

	private String getName() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.nameLength; i++) {
			builder.append(this.name[i]);
		}
		return builder.toString();
	}
}
